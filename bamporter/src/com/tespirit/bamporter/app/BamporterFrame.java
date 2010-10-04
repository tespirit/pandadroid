package com.tespirit.bamporter.app;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.editor.AnimationEditor;
import com.tespirit.bamporter.editor.Editor;
import com.tespirit.bamporter.editor.NodeEditor;
import com.tespirit.bamporter.editor.TreeNodeEditor;
import com.tespirit.bamporter.io.BambooHandler;
import com.tespirit.bamporter.io.IOManager;
import com.tespirit.bamporter.opengl.Renderer;

public class BamporterFrame extends JFrame{
	private static final long serialVersionUID = 5177383861730200564L;

	private JMenu mFileMenu;
	private JMenuBar mMenu;
	private JFileChooser mFileDialog;
	private JTree mTree;
	private DefaultMutableTreeNode mRoot;
	private DefaultTreeModel mTreeModel;
	private JScrollPane mEditorView;
	private JMenuItem mSaveAllButton;
	private JMenuItem mSaveNodeButton;
	private JMenuItem mSaveAnimationButton;
	private Renderer mRenderer;
	private List<Editor> mEditors;
	
	private BambooAsset mBamboo;	
	
	private static final String TITLE = "Bamporter";
	
	public BamporterFrame() {
		this.mEditors = new ArrayList<Editor>();
		initComponents();
	}

	private void initComponents() {
		setTitle(TITLE);
		
		IOManager.init();
		
		JMenuItem openButton = new JMenuItem();
		openButton.setText("Open");
		mSaveAllButton = new JMenuItem();
		mSaveAllButton.setText("Save All");
		mSaveAllButton.setEnabled(false);
		mSaveNodeButton = new JMenuItem();
		mSaveNodeButton.setText("Save SceneGraph");
		mSaveNodeButton.setEnabled(false);
		mSaveAnimationButton = new JMenuItem();
		mSaveAnimationButton.setText("Save Animation");
		mSaveAnimationButton.setEnabled(false);
		JMenuItem exitButton = new JMenuItem();
		exitButton.setText("Exit");
		mFileMenu = new JMenu();
		mFileMenu.setText("File");
		mMenu = new JMenuBar();
		mFileDialog = new JFileChooser();
		mRoot = new DefaultMutableTreeNode("Workspace");
		mTree = new JTree();
		JScrollPane treeView = new JScrollPane();
		mEditorView = new JScrollPane();
		
		GridLayout layout = new GridLayout(1,0);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane splitPaneEditor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		setLayout(layout);
		treeView.setViewportView(mTree);
		mTreeModel = new DefaultTreeModel(mRoot);
		mTree.setModel(mTreeModel);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(splitPaneEditor);
		splitPaneEditor.setBottomComponent(mEditorView);
		
		mRenderer = new Renderer();
		
		splitPaneEditor.setTopComponent(mRenderer.getView());
		
		Dimension minimumSize = new Dimension(50, 100);
		mEditorView.setMinimumSize(minimumSize);
	    treeView.setMinimumSize(minimumSize);
	    mRenderer.getView().setMinimumSize(minimumSize);
	    splitPane.setDividerLocation(200); 
	    splitPane.setPreferredSize(new Dimension(800, 600));
	    splitPaneEditor.setDividerLocation(400);
	    splitPaneEditor.setPreferredSize(new Dimension(600, 600));
	    
		add(splitPane);
		
		mMenu.add(mFileMenu);
		mFileMenu.add(openButton);
		mFileMenu.add(mSaveAllButton);
		mFileMenu.add(mSaveNodeButton);
		mFileMenu.add(mSaveAnimationButton);
		mFileMenu.addSeparator();
		mFileMenu.add(exitButton);
		
		mTree.addTreeSelectionListener(new TreeSelectionListener(){

			@Override
			public void valueChanged(TreeSelectionEvent event) {
				selectTreeNode(event);
			}
			
		});
		
		openButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				openButtonAction(event);
			}
		});
		
		mSaveAllButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				saveAllButtonAction(event);
			}
		});
		
		mSaveNodeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				saveNodeButtonAction(event);
			}
		});
		
		mSaveAnimationButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				saveAnimationButtonAction(event);
			}
		});
		
		setJMenuBar(mMenu);
		setSize(800, 600);
	}
	
	private void selectTreeNode(TreeSelectionEvent event) {
		Object node = mTree.getLastSelectedPathComponent();
		if(node instanceof TreeNodeEditor){
			this.mEditorView.setViewportView(((TreeNodeEditor)node).getEditorPanel());
			this.mEditorView.setEnabled(true);
		} else {
			this.mEditorView.setEnabled(false);
		}
	}
	
	private void openButtonAction(ActionEvent event) {
		if(this.showOpenDialog() == JFileChooser.APPROVE_OPTION){
			try{
				File file = mFileDialog.getSelectedFile();
				mBamboo = IOManager.open(file);
				setTitle(TITLE + " - " + file.getName());
				
				Assets.getInstance().addTexturePath(file.getParent());
			} catch (Exception e){
				e.printStackTrace();
				this.alertError("I couldn't open the file. Either there's a bug or the file is not a valid format.");
				return;
			}
			this.loadBamboo();
		}
	}
	
	private void loadBamboo(){
		for(Editor e : this.mEditors){
			e.recycle();
		}
		this.mRoot.removeAllChildren();
		this.mRenderer.addNode(this.mBamboo.getRootSceneNodes());
		DefaultMutableTreeNode sceneGraph = new DefaultMutableTreeNode("SceneGraph");
		DefaultMutableTreeNode animations = new DefaultMutableTreeNode("Animations");
		for(Node node : this.mBamboo.getRootSceneNodes()){
			NodeEditor ne = new NodeEditor(node, this.mRenderer);
			this.mEditors.add(ne);
			sceneGraph.add(ne);
		}
		for(Animation animation : this.mBamboo.getAnimations()){
			AnimationEditor ae = new AnimationEditor(animation, this.mRenderer);
			this.mEditors.add(ae);
			animations.add(ae);
		}
		
		if(this.mBamboo.getRootSceneNodes().size() > 0){
			this.mRoot.add(sceneGraph);
			this.mSaveNodeButton.setEnabled(true);
			this.mSaveAllButton.setEnabled(true);
		} else {
			this.mSaveNodeButton.setEnabled(false);
			this.mSaveAllButton.setEnabled(false);
		}
		if(this.mBamboo.getAnimations().size() > 0){
			this.mRoot.add(animations);
			mSaveAnimationButton.setEnabled(true);
			mSaveAllButton.setEnabled(true);
		} else {
			mSaveAnimationButton.setEnabled(false);
		}

		mTreeModel.reload();
	}
	
	private void saveAllButtonAction(ActionEvent event) {
		if(this.showSaveDialog() == JFileChooser.APPROVE_OPTION){
			try{
				IOManager.saveBamboo(this.mBamboo, mFileDialog.getSelectedFile());
			} catch (Exception e){
				e.printStackTrace();
				this.alertError("An error happened while saving. Please contact support :(");
			}
		}
	}

	private void saveNodeButtonAction(ActionEvent event) {
		if(this.showSaveDialog() == JFileChooser.APPROVE_OPTION){
			try{
				IOManager.saveSceneGraph(this.mBamboo, mFileDialog.getSelectedFile());
			} catch (Exception e){
				e.printStackTrace();
				this.alertError("An error happened while saving. Please contact support :(");
			}
		}
	}

	private void saveAnimationButtonAction(ActionEvent event) {
		if(this.showSaveDialog() == JFileChooser.APPROVE_OPTION){
			try{
				IOManager.saveAnimation(this.mBamboo, mFileDialog.getSelectedFile());
			} catch (Exception e){
				e.printStackTrace();
				this.alertError("An error happened while saving. Please contact support :(");
			}
		}
	}

	
	private void alertError(String message){
		JOptionPane.showMessageDialog(null, message, "Uh oh!", JOptionPane.ERROR_MESSAGE);
	}
	
	private int showSaveDialog(){
		mFileDialog.resetChoosableFileFilters();
		FileFilter prev = mFileDialog.getFileFilter();
		mFileDialog.setFileFilter(BambooHandler.getInstance().getFilter());
		int result = mFileDialog.showSaveDialog(this);
		mFileDialog.setFileFilter(prev);
		return result;
	}
	
	private int showOpenDialog(){
		mFileDialog.resetChoosableFileFilters();
		for(FileFilter filter : IOManager.getFilters()){
			mFileDialog.addChoosableFileFilter(filter);
		}
		return mFileDialog.showOpenDialog(this);
	}
}
