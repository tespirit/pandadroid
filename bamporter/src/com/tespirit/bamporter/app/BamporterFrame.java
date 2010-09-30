package com.tespirit.bamporter.app;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;

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

import com.tespirit.bamboo.io.Bamboo;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamporter.editor.AnimationEditor;
import com.tespirit.bamporter.editor.NodeEditor;
import com.tespirit.bamporter.editor.TreeNodeEditor;
import com.tespirit.bamporter.io.BambooHandler;
import com.tespirit.bamporter.io.IOManager;

public class BamporterFrame extends JFrame{
	private static final long serialVersionUID = 5177383861730200564L;

	private JMenu fileMenu;
	private JMenuBar menu;
	private JFileChooser fileDialog;
	private JTree tree;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	private JScrollPane outputView;
	private JMenuItem saveAllButton;
	private JMenuItem saveNodeButton;
	private JMenuItem saveAnimationButton;
	
	private BambooAsset bamboo;	
	
	public BamporterFrame() {
		initComponents();
	}

	private void initComponents() {
		setTitle("Bamporter");
		
		IOManager.init();
		
		JMenuItem openButton = new JMenuItem();
		openButton.setText("Open");
		saveAllButton = new JMenuItem();
		saveAllButton.setText("Save All");
		saveAllButton.setEnabled(false);
		saveNodeButton = new JMenuItem();
		saveNodeButton.setText("Save SceneGraph");
		saveNodeButton.setEnabled(false);
		saveAnimationButton = new JMenuItem();
		saveAnimationButton.setText("Save Animation");
		saveAnimationButton.setEnabled(false);
		JMenuItem exitButton = new JMenuItem();
		exitButton.setText("Exit");
		fileMenu = new JMenu();
		fileMenu.setText("File");
		menu = new JMenuBar();
		fileDialog = new JFileChooser();
		root = new DefaultMutableTreeNode("Workspace");
		tree = new JTree();
		JScrollPane treeView = new JScrollPane();
		outputView = new JScrollPane();
		
		GridLayout layout = new GridLayout(1,0);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		setLayout(layout);
		treeView.setViewportView(tree);
		treeModel = new DefaultTreeModel(root);
		tree.setModel(treeModel);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(outputView);
		
		Dimension minimumSize = new Dimension(50, 100);
	    outputView.setMinimumSize(minimumSize);
	    treeView.setMinimumSize(minimumSize);
	    splitPane.setDividerLocation(300); 
	    splitPane.setPreferredSize(new Dimension(800, 600));
		
		add(splitPane);
		
		menu.add(fileMenu);
		fileMenu.add(openButton);
		fileMenu.add(saveAllButton);
		fileMenu.add(saveNodeButton);
		fileMenu.add(saveAnimationButton);
		fileMenu.addSeparator();
		fileMenu.add(exitButton);
		
		tree.addTreeSelectionListener(new TreeSelectionListener(){

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
		
		saveAllButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				saveAllButtonAction(event);
			}
		});
		
		saveNodeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				saveNodeButtonAction(event);
			}
		});
		
		saveAnimationButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				saveAnimationButtonAction(event);
			}
		});
		
		setJMenuBar(menu);
		setSize(800, 600);
	}
	
	private void selectTreeNode(TreeSelectionEvent event) {
		Object node = tree.getLastSelectedPathComponent();
		if(node instanceof TreeNodeEditor){
			this.outputView.setViewportView(((TreeNodeEditor)node).getEditorPanel());
			this.outputView.setEnabled(true);
		} else {
			this.outputView.setEnabled(false);
		}
	}
	
	private void openButtonAction(ActionEvent event) {
		if(this.showOpenDialog() == JFileChooser.APPROVE_OPTION){
			try{
				bamboo = IOManager.open(fileDialog.getSelectedFile());
			} catch (Exception e){
				e.printStackTrace();
				this.alertError("I couldn't open the file. Either there's a bug or the file is not a valid format.");
				return;
			}
			root.removeAllChildren();
			if(bamboo.getSceneGraph() != null){
				DefaultMutableTreeNode sceneGraph = new DefaultMutableTreeNode("SceneGraph");
				sceneGraph.add(new NodeEditor(bamboo.getSceneGraph()));
				root.add(sceneGraph);
				saveNodeButton.setEnabled(true);
				saveAllButton.setEnabled(true);
			} else {
				saveNodeButton.setEnabled(false);
				saveAllButton.setEnabled(false);
			}
			if(bamboo.getAnimation() != null){
				
				root.add(new AnimationEditor(bamboo.getAnimation()));
				saveAnimationButton.setEnabled(true);
				saveAllButton.setEnabled(true);
			} else {
				saveAnimationButton.setEnabled(false);
			}

			treeModel.reload();
		}
	}
	
	private void saveAllButtonAction(ActionEvent event) {
		if(bamboo == null){
			return;
		}
		
		if(this.showSaveDialog() == JFileChooser.APPROVE_OPTION){
			try{
				FileOutputStream stream = new FileOutputStream(fileDialog.getSelectedFile());
				Bamboo.save(bamboo, stream);
			} catch (Exception e){
				e.printStackTrace();
				this.alertError("An error happened while saving. Please contact support :(");
			}
		}
	}

	private void saveNodeButtonAction(ActionEvent event) {
		if(bamboo == null){
			return;
		}
		
		if(this.showSaveDialog() == JFileChooser.APPROVE_OPTION){
			try{
				FileOutputStream stream = new FileOutputStream(fileDialog.getSelectedFile());
				Bamboo.save(bamboo.getSceneGraph(), stream);
			} catch (Exception e){
				e.printStackTrace();
				this.alertError("An error happened while saving. Please contact support :(");
			}
		}
	}

	private void saveAnimationButtonAction(ActionEvent event) {
		if(bamboo == null){
			return;
		}
		
		if(this.showSaveDialog() == JFileChooser.APPROVE_OPTION){
			try{
				FileOutputStream stream = new FileOutputStream(fileDialog.getSelectedFile());
				Bamboo.save(bamboo.getAnimation(), stream);
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
		fileDialog.resetChoosableFileFilters();
		FileFilter prev = fileDialog.getFileFilter();
		fileDialog.setFileFilter(BambooHandler.getInstance().getFilter());
		int result = fileDialog.showSaveDialog(this);
		fileDialog.setFileFilter(prev);
		return result;
	}
	
	private int showOpenDialog(){
		fileDialog.resetChoosableFileFilters();
		for(FileFilter filter : IOManager.getFilters()){
			fileDialog.addChoosableFileFilter(filter);
		}
		return fileDialog.showOpenDialog(this);
	}
	
}
