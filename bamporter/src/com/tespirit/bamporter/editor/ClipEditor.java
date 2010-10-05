package com.tespirit.bamporter.editor;

import java.awt.Component;

import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamporter.app.BamporterEditor;

public class ClipEditor extends TreeNodeEditor{
	private Clip mClip;
	private ClipEditorPanel mProperties;
	
	
	public ClipEditor(Clip clip){
		this.mClip = clip;
		this.mProperties = new ClipEditorPanel();
		if(this.mClip.getName() != null){
			this.mProperties.mName.setText(this.mClip.getName());
		}
		
		this.mProperties.mStart.getModel().setValue(this.mClip.getStart());
		this.mProperties.mEnd.getModel().setValue(this.mClip.getEnd());
	}
	
	public void deleteClip(){
		AnimationEditor parent = (AnimationEditor)this.getParent();
		parent.removeClip(this.mClip);
		this.removeFromParent();
		BamporterEditor.getInstance().reloadNavigator();
		this.recycle();
	}
	
	public void updateClip(){
		
	}
	
	public void updateClipName(){
		
	}

	@Override
	public Component getPropertyPanel() {
		AnimationEditor parent = (AnimationEditor)this.getParent();
		this.mProperties.mDelete.setEnabled(parent.getClipCount()  > 1);
		return this.mProperties;
	}

	@Override
	public void recycle() {
		this.mProperties = null;
		this.mClip = null;
	}
	
	@Override
	public String toString(){
		return "<Clip> " + this.mClip.getName();
	}

}
