package com.tespirit.bamporter.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.app.Assets.SaveTypes;
import com.tespirit.bamporter.editor.AnimationEditor;
import com.tespirit.bamporter.editor.Editor;
import com.tespirit.bamporter.editor.NodeEditor;
import com.tespirit.bamporter.editor.Util;
import com.tespirit.bamporter.io.BambooHandler;
import com.tespirit.bamporter.opengl.Renderer;

/**
 * This glues together all the components. And allows for global refreshing.
 * @author Todd Espiritu Santo
 *
 */
public class BamporterEditor {
	
	private static BamporterEditor mEditor;
	
	public static BamporterEditor getInstance(){
		if(mEditor == null){
			mEditor = new BamporterEditor();
		}
		return mEditor;
	}

	private BamporterFrame2 mFrame;
	private Renderer mRenderer;
	private JFileChooser mFileOpen;
	private JFileChooser mFileSave;
	private BambooAsset mBamboo;
	private List<Editor> mEditors;

	private static final String TITLE = "Bamporter";
	
	private BamporterEditor(){
		this.mEditors = new ArrayList<Editor>();
		Assets.init();

		this.mFrame = new BamporterFrame2();
		this.mRenderer = new Renderer();
		this.mFrame.setTitle(TITLE);
		this.mFrame.mEditor.setBottomComponent(this.mRenderer.getView());

		this.mFileOpen = new JFileChooser();
		this.mFileSave = new JFileChooser();

		this.mFileSave.setFileFilter(BambooHandler.getInstance().getFilter());
		for(FileFilter filter : Assets.getFilters()){
			this.mFileOpen.addChoosableFileFilter(filter);
		}
		
		this.mFrame.mNavigator.addTreeSelectionListener(new TreeSelectionListener(){
			@Override
			public void valueChanged(TreeSelectionEvent event) {
				onSelectTreeNode(event);
			}
		});
		this.mFrame.mOpen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				onOpen(event);
			}
		});
		this.mFrame.mSaveAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				onSave(event, SaveTypes.all);
			}
		});
		
		this.mFrame.mSaveScenes.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				onSave(event, SaveTypes.scene);
			}
		});
		
		this.mFrame.mSaveAnimations.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				onSave(event, SaveTypes.animation);
			}
		});
	}
	
	private void onSelectTreeNode(TreeSelectionEvent event){
		Object node = this.mFrame.mNavigator.getLastSelectedPathComponent();
		this.mFrame.mProperties.removeAll();
		if(node instanceof Editor){
			this.mFrame.mProperties.add(((Editor)node).getPropertyPanel());
		}
	}
	
	private void onOpen(ActionEvent event){
		if(this.mFileOpen.showOpenDialog(this.mFrame) == JFileChooser.APPROVE_OPTION){
			try{
				File file = this.mFileOpen.getSelectedFile();
				this.mBamboo = Assets.open(file);
				this.mFrame.setTitle(TITLE + " - " + file.getName());
			} catch (Exception e){
				e.printStackTrace();
				Util.alertError("I couldn't open the file. Either there's a bug or the file is not a valid format.");
				return;
			}
			this.loadBamboo();
		}
	}
	
	private void onSave(ActionEvent event, SaveTypes type){
		if(this.mFileSave.showOpenDialog(this.mFrame) == JFileChooser.APPROVE_OPTION){
			try{
				File file = this.mFileSave.getSelectedFile();
				Assets.saveBamboo(this.mBamboo, file, type);
			} catch (Exception e){
				e.printStackTrace();
				Util.alertError("I couldn't save the file.");
			}
		}
	}
	
	private void loadBamboo(){
		for(Editor e : this.mEditors){
			e.recycle();
		}
		this.mEditors.clear();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.mFrame.mNavigator.getModel().getRoot();
		root.removeAllChildren();
		
		this.mRenderer.addScenes(this.mBamboo.getScenes());
		
		DefaultMutableTreeNode sceneGraph = new DefaultMutableTreeNode("Scenes");
		DefaultMutableTreeNode animations = new DefaultMutableTreeNode("Animations");
		for(Node node : this.mBamboo.getScenes()){
			NodeEditor ne = new NodeEditor(node, this.mRenderer);
			this.mEditors.add(ne);
			sceneGraph.add(ne);
		}
		for(Animation animation : this.mBamboo.getAnimations()){
			AnimationEditor ae = new AnimationEditor(animation, this.mRenderer);
			this.mEditors.add(ae);
			animations.add(ae);
		}
		
		if(this.mBamboo.getScenes().size() > 0){
			root.add(sceneGraph);
			
			this.mFrame.mSaveScenes.setEnabled(true);
			this.mFrame.mSaveAll.setEnabled(true);
		} else {
			this.mFrame.mSaveScenes.setEnabled(false);
			this.mFrame.mSaveAll.setEnabled(false);
		}
		if(this.mBamboo.getAnimations().size() > 0){
			root.add(animations);
			this.mFrame.mSaveAnimations.setEnabled(true);
			this.mFrame.mSaveAll.setEnabled(true);
		} else {
			this.mFrame.mSaveAnimations.setEnabled(false);
		}
		this.reloadNavigator();
	}
	
	public void reloadNavigator(){
		DefaultTreeModel treeModel = (DefaultTreeModel)this.mFrame.mNavigator.getModel();
		treeModel.reload();
	}
	
	public void show(){
		this.mFrame.setVisible(true);
	}
}
