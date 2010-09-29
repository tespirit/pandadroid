package com.tespirit.bamporter;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Channel;
import com.tespirit.bamboo.io.Bamboo;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.io.BambooHandler;
import com.tespirit.bamporter.io.IOManager;

public class Bamporter extends JFrame {

	private static final long serialVersionUID = 1L;
	private JMenuItem openButton;
	private JMenuItem saveAllButton;
	private JMenuItem saveNodeButton;
	private JMenuItem saveAnimationButton;
	private JMenuItem exitButton;
	private JMenu fileMenu;
	private JMenuBar menu;
	private JFileChooser fileDialog;
	private JScrollPane treeView;
	private JTree tree;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	private GridLayout layout;
	private JSplitPane splitPane;
	private JEditorPane output;
	private JScrollPane outputView;
	private static final String PREFERRED_LOOK_AND_FEEL = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";

	private BambooAsset bamboo;	
	
	public Bamporter() {
		initComponents();
	}

	private void initComponents() {
		setTitle("Bamporter");
		
		IOManager.init();
		
		openButton = new JMenuItem();
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
		exitButton = new JMenuItem();
		exitButton.setText("Exit");
		fileMenu = new JMenu();
		fileMenu.setText("File");
		menu = new JMenuBar();
		fileDialog = new JFileChooser();
		root = new DefaultMutableTreeNode("Workspace");
		tree = new JTree();
		treeView = new JScrollPane();
		output = new JEditorPane();
		output.setEditable(false);
		outputView = new JScrollPane();
		
		layout = new GridLayout(1,0);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		setLayout(layout);
		treeView.setViewportView(tree);
		outputView.setViewportView(output);
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
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if(treeNode != null){
			Object data = treeNode.getUserObject();
			if(data instanceof Node){
				this.selectNode((Node)data);
			} else if(data instanceof Animation){
				this.selectAnimation((Animation)data);
			} else if(data instanceof Channel){
				this.selectChannel((Channel)data);
			}
		}
	}
	
	private void selectNode(Node node){
		String text = node.getNodeInfo();
		if(node.getTransform() != null){
			text += "\nLocalTransform:";
			for(int j = 0; j < 4; j++){
				text+= "\n\t";
				for(int i = 0; i < 4; i++){
					text += node.getTransform().getValue(j, i) + "\t";
				}
			}
		}
		if(node.getBoundingBox() != null){
			text += "\nBoundingBox:\n\t";
			for(int i = 0; i < 3; i++){
				text += node.getBoundingBox().getMin().get(i) + "\t";
			}
			text += "\n\t";
			for(int i = 0; i < 3; i++){
				text += node.getBoundingBox().getMax().get(i) + "\t";
			}
		}
		output.setText(text);
	}
	
	private void selectAnimation(Animation animation){
		
	}
	
	private void selectChannel(Channel channel){
		
	}

	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	/**
	 * Main entry of the class.
	 * Note: This class is only created so that you can easily preview the result at runtime.
	 * It is not expected to be managed by the designer.
	 * You can modify it as you like.
	 */
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Bamporter frame = new Bamporter();
				frame.setDefaultCloseOperation(Bamporter.EXIT_ON_CLOSE);
				frame.setTitle("Bamporter");
				frame.getContentPane().setPreferredSize(frame.getSize());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
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
				sceneGraph.add(createSceneGraphTree(bamboo.getSceneGraph()));
				root.add(sceneGraph);
				saveNodeButton.setEnabled(true);
				saveAllButton.setEnabled(true);
			} else {
				saveNodeButton.setEnabled(false);
				saveAllButton.setEnabled(false);
			}
			if(bamboo.getAnimation() != null){
				
				root.add(createAnimationTree(bamboo.getAnimation()));
				saveAnimationButton.setEnabled(true);
				saveAllButton.setEnabled(true);
			} else {
				saveAnimationButton.setEnabled(false);
			}

			treeModel.reload();
		}
	}
	
	private DefaultMutableTreeNode createSceneGraphTree(Node node){
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node.toString());
		treeNode.setUserObject(node);
		for(int i = 0; i < node.getChildCount(); i++){
			treeNode.add(this.createSceneGraphTree(node.getChild(i)));
		}
		return treeNode;
	}
	
	private DefaultMutableTreeNode createAnimationTree(Animation a){
		DefaultMutableTreeNode animation = new DefaultMutableTreeNode("Animation");
		animation.setUserObject(a);
		for(int i = 0; i < a.getChannelCount(); i++){
			DefaultMutableTreeNode channel = new DefaultMutableTreeNode("Channel "+i);
			channel.setUserObject(a.getChannel(i));
			animation.add(channel);
		}
		return animation;
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
