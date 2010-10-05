package com.tespirit.bamporter.editor;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Clip;

public class ClipEditor extends TreeNodeEditor{
	private Clip mClip;
	private Box mPanel;
	private JSpinner mStart;
	private JSpinner mEnd;
	private JLabel mId;
	private JLabel mName;
	
	
	public ClipEditor(Clip clip){
		this.mClip = clip;
		this.mPanel = Box.createVerticalBox();
		this.mStart = EditorPanels.createLongSpinner(clip.getStart(), Long.MIN_VALUE, Long.MAX_VALUE, 1);
		this.mStart.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				SpinnerNumberModel model = (SpinnerNumberModel)mStart.getModel();
				mClip.setStart(model.getNumber().longValue());
				
			}
		});
		
		this.mEnd = EditorPanels.createLongSpinner(clip.getEnd(), Long.MIN_VALUE, Long.MAX_VALUE, 1);
		this.mEnd.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				SpinnerNumberModel model = (SpinnerNumberModel)mEnd.getModel();
				mClip.setEnd(model.getNumber().longValue());
				
			}
		});
		
		this.mName = new JLabel();
		this.mId = new JLabel();
		
		this.mPanel.add(this.mName);
		this.mPanel.add(this.mId);
		this.mPanel.add(this.mStart);
		this.mPanel.add(this.mEnd);
	}

	@Override
	public Component getEditorPanel() {
		Animation owner = ((AnimationEditor)this.getParent()).getAnimation();
		this.mName.setText("Name: " + this.mClip.getName());
		this.mId.setText("Id: "+owner.getClipId(this.mClip.getName()));
		return this.mPanel;
	}

	@Override
	public void recycle() {
		this.mPanel = null;
		this.mClip = null;
	}
	
	@Override
	public String toString(){
		return "<Clip> " + this.mClip.getName();
	}

}
