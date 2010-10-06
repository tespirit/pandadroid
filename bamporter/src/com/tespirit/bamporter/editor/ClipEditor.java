package com.tespirit.bamporter.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamporter.app.BamporterFrame;

public class ClipEditor extends TreeNodeEditor{
	private Clip mClip;
	private SimplePanel mPropertyPanel;
	private JButton mDeleteClip;
	private JToggleButton mPlay;
	private boolean mPlayState;
	
	
	public ClipEditor(Clip clip){
		this.mClip = clip;
	}
	
	public void deleteClip(){
		AnimationEditor parent = (AnimationEditor)this.getParent();
		parent.removeClip(this.mClip);
		this.removeFromParent();
		BamporterFrame.getInstance().reloadNavigator();
		this.recycle();
	}
	
	private void generatePanel(){
		this.mPropertyPanel = new SimplePanel();
		
		JTextField name = this.mPropertyPanel.createTextField("Name");
		JSpinner start = this.mPropertyPanel.createLongSpinner("Start", Long.MIN_VALUE, Long.MAX_VALUE, 30);
		JSpinner end = this.mPropertyPanel.createLongSpinner("End", Long.MIN_VALUE, Long.MAX_VALUE, 30);
		this.mPlay = this.mPropertyPanel.createToggleButton("Play Clip");
		this.mDeleteClip = this.mPropertyPanel.createButton("Delete");
		
		if(this.mClip.getName() != null){
			name.setText(this.mClip.getName());
		}
		
		start.getModel().setValue(this.mClip.getStart());
		end.getModel().setValue(this.mClip.getEnd());
		
		name.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mClip.setName(((JTextField)e.getSource()).getText());
			}
		});
		
		start.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner spinner = (JSpinner)e.getSource();
				SpinnerNumberModel model = (SpinnerNumberModel)spinner.getModel();
				mClip.setStart(model.getNumber().longValue());
			}
		});
		
		end.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner spinner = (JSpinner)e.getSource();
				SpinnerNumberModel model = (SpinnerNumberModel)spinner.getModel();
				mClip.setEnd(model.getNumber().longValue());
			}
		});
		
		this.mPlay.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationEditor parent = (AnimationEditor)getParent();
				mPlayState = mPlay.getModel().isSelected();
				if(mPlayState){
					if(!parent.playClip(mClip.getName())){
						mPlayState = false;
						mPlay.getModel().setSelected(false);
					}
				} else {
					parent.pauseClip();
				}
			}
			
		});
		
		this.mDeleteClip.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteClip();
			}
		});
	}
	
	public void setPlayState(boolean state){
		this.mPlayState = state;
	}

	@Override
	public Component getPropertyPanel() {
		if(this.mPropertyPanel == null){
			this.generatePanel();
		}
		this.mPlay.getModel().setSelected(this.mPlayState);
		
		AnimationEditor parent = (AnimationEditor)this.getParent();
		this.mDeleteClip.setEnabled(parent.getClipCount()  > 1);
		return this.mPropertyPanel;
	}

	@Override
	public void recycle() {
		this.mPropertyPanel = null;
		this.mClip = null;
		this.mDeleteClip = null;
	}
	
	@Override
	public String toString(){
		return "<Clip> " + this.mClip.getName();
	}

}
