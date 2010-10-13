package com.tespirit.bamporter.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.creation.Primitives;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamboo.particles.SpriteParticleEmitter;
import com.tespirit.bamboo.particles.RandomParticleGenerator;
import com.tespirit.bamboo.particles.StandardParticleSystem;
import com.tespirit.bamboo.render.UpdateManager;
import com.tespirit.bamboo.scenegraph.Model;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.app.Assets.SaveTypes;
import com.tespirit.bamporter.editor.*;
import com.tespirit.bamporter.io.BambooHandler;
import com.tespirit.bamporter.opengl.Renderer;
import com.tespirit.bamporter.tools.AnimationEdit;

public class BamporterFrame extends JFrame{
	private static final long serialVersionUID = 5177383861730200564L;

	private static BamporterFrame mEditor;
	
	public static BamporterFrame getInstance(){
		if(mEditor == null){
			mEditor = new BamporterFrame();
		}
		return mEditor;
	}
	
	private JMenuItem mSaveAllButton;
	private JMenuItem mSaveScenesButton;
	private JMenuItem mSaveAnimationsButton;
	
	private JTree mNavigator;
	private JScrollPane mPropertyPane;
	private Renderer mRenderer;
	private JFileChooser mFileOpen;
	private JFileChooser mFileSave;
	private BambooAsset mBamboo;
	
	//BAMporter objects
	private DefaultMutableTreeNode mRoot;
	private DefaultMutableTreeNode mSceneNodes;
	private DefaultMutableTreeNode mAnimations;
	private DefaultMutableTreeNode mParticles;
	
	private static final String TITLE = "BAMporter";
	
	private BamporterFrame() {
		initComponents();
	}

	private void initComponents() {
		
		this.setTitle(TITLE);
		
		Assets.init();
		
		this.setLayout(new GridLayout(1,1));
		
		//initialize the menus!
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newParticleButton = new JMenuItem("New ParticleGenerator");
		JMenuItem openButton = new JMenuItem("Open");
		this.mSaveAllButton = new JMenuItem("Save All");
		this.mSaveScenesButton = new JMenuItem("Save Scenes");
		this.mSaveAnimationsButton = new JMenuItem("Save Animations");
		JMenuItem mergeAnimationsButton = new JMenuItem("Merge Animations");
		JMenuItem exitButton = new JMenuItem("Exit");
		
		fileMenu.add(newParticleButton);
		fileMenu.add(openButton);
		fileMenu.addSeparator();
		fileMenu.add(this.mSaveAllButton);
		fileMenu.add(this.mSaveScenesButton);
		fileMenu.add(this.mSaveAnimationsButton);
		fileMenu.addSeparator();
		fileMenu.add(mergeAnimationsButton);
		fileMenu.addSeparator();
		fileMenu.add(exitButton);
		
		menuBar.add(fileMenu);
		
		JMenu settingsMenu = new JMenu("Settings");
		JMenu themeMenu = new JMenu("Theme");
		ActionListener themeAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				JMenuItem themeButton = (JMenuItem)event.getSource();
				Preferences.setTheme(themeButton.getText());
				Preferences.refreshComponent(mEditor);
				Preferences.refreshComponent(mEditor.mFileOpen);
				Preferences.refreshComponent(mEditor.mFileSave);
			}
		};
		ButtonGroup group = new ButtonGroup();
		for(String s : Preferences.getLookAndFeels()){
			JRadioButtonMenuItem themeButton = new JRadioButtonMenuItem(s);
			themeButton.setSelected(s.equals(Preferences.getTheme()));
			group.add(themeButton);
			themeButton.addActionListener(themeAction);
			themeMenu.add(themeButton);
		}
		JMenuItem bgColorButton = new JMenuItem("Scene Background");
		settingsMenu.add(themeMenu);
		settingsMenu.add(bgColorButton);
		
		menuBar.add(settingsMenu);
		
		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutButton = new JMenuItem("About");
		helpMenu.add(aboutButton);
		
		menuBar.add(helpMenu);
		
		this.setJMenuBar(menuBar);
		
		//initialize the editors!
		JSplitPane renderSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane editSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		JScrollPane navScroll = new JScrollPane();
		navScroll.setBorder(BorderFactory.createTitledBorder("Navigator"));
		this.mNavigator = new JTree();
		Preferences.applySimpleBorder(this.mNavigator);
		this.mRoot = new DefaultMutableTreeNode("root");
		this.mSceneNodes = new DefaultMutableTreeNode("Scenes");
		this.mAnimations = new DefaultMutableTreeNode("Animations");
		this.mParticles = new DefaultMutableTreeNode("Particles");
		this.mRoot.add(this.mSceneNodes);
		this.mRoot.add(this.mAnimations);
		this.mRoot.add(this.mParticles);
		

		this.mNavigator.setModel(new DefaultTreeModel(this.mRoot));
		this.mNavigator.setRootVisible(false);
		
		this.mPropertyPane = new JScrollPane();
		this.mPropertyPane.setBorder(BorderFactory.createTitledBorder("Properties"));
		
		JPanel renderView = new JPanel();
		renderView.setLayout(new GridLayout(1,1));
		renderView.setBorder(BorderFactory.createTitledBorder("Scene View"));
		
		JPanel renderBorder = new JPanel();
		renderBorder.setLayout(new GridLayout(1,1));
		Preferences.applySimpleBorder(renderBorder);
		
		this.mRenderer = new Renderer(Preferences.getRenderBGColor());
		
		Model axis = new Model();
		axis.getTransform().scale(0.25f);
		axis.setPrimative(new Primitives.Axis());
		this.mRenderer.addScene(axis);
		
		navScroll.setViewportView(this.mNavigator);
		editSplitter.setTopComponent(navScroll);
		editSplitter.setBottomComponent(this.mPropertyPane);
		
		renderSplitter.setTopComponent(editSplitter);
		
		renderBorder.add(this.mRenderer.getView());
		renderView.add(renderBorder);
		renderSplitter.setBottomComponent(renderView);
		
		editSplitter.setMinimumSize(new Dimension(100,100));
		navScroll.setMinimumSize(new Dimension(100,100));
		this.mPropertyPane.setMinimumSize(new Dimension(100,100));
		this.mNavigator.setMinimumSize(new Dimension(100, 100));
		
		renderView.setMinimumSize(new Dimension(100,100));
		renderSplitter.setMinimumSize(new Dimension(300,300));
		
		renderSplitter.setDividerLocation(300);
		renderSplitter.setOneTouchExpandable(true);
		
		editSplitter.setDividerLocation(300);
		editSplitter.setOneTouchExpandable(true);
		
		this.add(renderSplitter);
		
		this.mNavigator.addTreeSelectionListener(new TreeSelectionListener(){

			@Override
			public void valueChanged(TreeSelectionEvent event) {
				onSelectTreeNode(event);
			}
			
		});
		
		newParticleButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				createSpriteParticles();
			}
		});
		
		openButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				onOpenClicked(event);
			}
		});
		
		this.mSaveAllButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				onSaveClicked(event, SaveTypes.all);
			}
		});
		
		this.mSaveScenesButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				onSaveClicked(event, SaveTypes.scene);
			}
		});
		
		this.mSaveAnimationsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				onSaveClicked(event, SaveTypes.animation);
			}
		});
		
		mergeAnimationsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mergeAnimation();
			}
		});
		
		exitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		bgColorButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Color bg = JColorChooser.showDialog(mEditor, "Scene View Background Color", Preferences.getRenderBGColor());
				if(bg != null){
					Preferences.setRenderBGColor(bg);
					mRenderer.setBackground(bg);
				}
			}
		});
		
		aboutButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new AboutDialog(mEditor);
			}
		});
		
		this.setSize(800, 600);
		this.enableSaves();
		
		this.mFileOpen = new JFileChooser();
		this.mFileSave = new JFileChooser();
		
		this.mFileOpen.setCurrentDirectory(Preferences.getOpenDirectory());
		this.mFileSave.setCurrentDirectory(Preferences.getSaveDirectory());
		
		this.mFileSave.setFileFilter(BambooHandler.getInstance().getFilter());
		for(FileFilter filter : Assets.getFilters()){
			this.mFileOpen.addChoosableFileFilter(filter);
		}
		
	}
	
	public void clearAll(){
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> current = this.mRoot.children();
		while(current.hasMoreElements()){
			this.clear(current.nextElement());
		}
		DefaultTreeModel treeModel = (DefaultTreeModel)this.mNavigator.getModel();
		treeModel.reload();
	}
	
	private void clear(DefaultMutableTreeNode parent){
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> current = parent.children();
		while(current.hasMoreElements()){
			DefaultMutableTreeNode node = current.nextElement();
			if(node instanceof TreeNodeEditor){
				((TreeNodeEditor)node).recycle();
			}
		}
		parent.removeAllChildren();
	}
	
	protected void createSpriteParticles() {
		RandomParticleGenerator spp = new RandomParticleGenerator();
		SpriteParticleEmitter p = new SpriteParticleEmitter(spp);
		this.mRenderer.addScene(p);
		
		NodeEditor ne = new NodeEditor(p, this.mRenderer);
		this.addNodeTo(ne, this.mSceneNodes);
		
		ParticleGeneratorEditor pe = new ParticleGeneratorEditor(spp);
		this.addNodeTo(pe, this.mParticles);
		
		if(p.getParticleSysetm() instanceof StandardParticleSystem){
			ParticleSystemEditor pse = new ParticleSystemEditor((StandardParticleSystem)p.getParticleSysetm());
			this.addNodeTo(pse, this.mParticles);
		}
	}

	public void close(){
		if (this.isActive())
		{
			WindowEvent windowClosing = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(windowClosing);
		}
	}
	
	private void enableSaves(){

		if(this.mBamboo != null && this.mBamboo.getScenes().size() > 0){
			this.mSaveScenesButton.setEnabled(true);
			this.mSaveAllButton.setEnabled(true);
		} else {
			this.mSaveScenesButton.setEnabled(false);
			this.mSaveAllButton.setEnabled(false);
		}
		if(this.mBamboo != null && this.mBamboo.getAnimations().size() > 0){
			this.mSaveAnimationsButton.setEnabled(true);
			this.mSaveAllButton.setEnabled(true);
		} else {
			this.mSaveAnimationsButton.setEnabled(false);
		}
	}
	
	private void onSelectTreeNode(TreeSelectionEvent event) {
		Object node = this.mNavigator.getLastSelectedPathComponent();
		if(node instanceof Editor){
			this.mPropertyPane.setViewportView(((Editor)node).getPropertyPanel());
			this.mPropertyPane.setEnabled(true);
		} else {
			this.mPropertyPane.setEnabled(false);
		}
	}
	private void onOpenClicked(ActionEvent event){
		if(this.mFileOpen.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			try{
				File file = this.mFileOpen.getSelectedFile();
				this.mBamboo = Assets.open(file);
				this.setTitle(TITLE + " - " + file.getName());
				Preferences.setOpenDirectory(file);
			} catch (Exception e){
				e.printStackTrace();
				Util.alertError("I couldn't open the file. Either there's a bug or the file is not a valid format.");
				return;
			}
			this.loadBamboo();
		}
	}
	
	private void onSaveClicked(ActionEvent event, SaveTypes type){
		if(this.mFileSave.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
			try{
				File file = this.mFileSave.getSelectedFile();
				Assets.saveBamboo(this.mBamboo, file, type);
				Preferences.setSaveDirectory(file);
			} catch (Exception e){
				e.printStackTrace();
				Util.alertError("I couldn't save the file.");
			}
		}
	}
	
	private void mergeAnimation(){
		if(this.mFileOpen.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			try{
				File file = this.mFileOpen.getSelectedFile();
				BambooAsset merge = Assets.open(file);
				AnimationEdit.mergeAnimation(merge, this.mBamboo);
				@SuppressWarnings("unchecked")
				Enumeration<DefaultMutableTreeNode> current = this.mAnimations.children();
				while(current.hasMoreElements()){
					DefaultMutableTreeNode node = current.nextElement();
					if(node instanceof AnimationEditor){
						((AnimationEditor)node).refreshClips();
					}
				}
			} catch (Exception e){
				e.printStackTrace();
				Util.alertError("I couldn't open the file. Either there's a bug or the file is not a valid format.");
			}
		}
	}
	
	private void loadBamboo(){
		this.clearAll();
		this.mRenderer.addScenes(this.mBamboo.getScenes());
		
		for(Node node : this.mBamboo.getScenes()){
			NodeEditor ne = new NodeEditor(node, this.mRenderer);
			this.mSceneNodes.add(ne);
		}
		for(Animation animation : this.mBamboo.getAnimations()){
			AnimationEditor ae = new AnimationEditor(animation, this.mRenderer);
			this.mAnimations.add(ae);
		}
		
		this.enableSaves();
		DefaultTreeModel treeModel = (DefaultTreeModel)this.mNavigator.getModel();
		treeModel.reload();
	}
	
	public void refreshNode(DefaultMutableTreeNode node){
		DefaultTreeModel treeModel = (DefaultTreeModel)this.mNavigator.getModel();
		treeModel.nodeChanged(node);
	}
	
	public void addNodeTo(TreeNodeEditor child, DefaultMutableTreeNode mSceneNodes2){
		DefaultTreeModel treeModel = (DefaultTreeModel)this.mNavigator.getModel();
		treeModel.insertNodeInto(child, mSceneNodes2, mSceneNodes2.getChildCount());
	}
	
	public void insertNodeTo(TreeNodeEditor child, DefaultMutableTreeNode parent, int index){
		DefaultTreeModel treeModel = (DefaultTreeModel)this.mNavigator.getModel();
		treeModel.insertNodeInto(child, parent, index);
	}
	
	public void removeNode(TreeNodeEditor child){
		DefaultTreeModel treeModel = (DefaultTreeModel)this.mNavigator.getModel();
		treeModel.removeNodeFromParent(child);
	}
	
	public void selectNode(TreeNodeEditor node){
		this.mNavigator.setSelectionPath(new TreePath(node.getPath()));
	}
	
	public UpdateManager getUpdateManager(){
		return this.mRenderer;
	}
}
