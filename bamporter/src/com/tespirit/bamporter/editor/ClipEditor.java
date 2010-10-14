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
import com.tespirit.bamporter.properties.SimplePanel;

public class ClipEditor implements EditorFactory.Factory{
	@Override
	public Editor createEditor(Object object) {
		return new ClipEditor.Editor((Clip)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Clip.class;
	}
	
	public class Editor extends TreeNodeEditor{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4480406979875461348L;
		private Clip mClip;
		private JTextField  mName;
		private JButton mDeleteClip;
		private JToggleButton mPlay;
		private boolean mPlayState;
		
		
		protected Editor(Clip clip){
			super(clip, false);
			this.mClip = clip;
		}
		
		public void deleteClip(){
			this.removeEditorFromParent();
			this.recycle();
		}
		
		public Clip getClip(){
			return this.mClip;
		}
		
		@Override
		protected Component generatePanel(){
			SimplePanel panel = new SimplePanel();
			
			this.mName = panel.createTextField("Name");
			JSpinner start = panel.createLongSpinner("Start", Long.MIN_VALUE, Long.MAX_VALUE, 30);
			JSpinner end = panel.createLongSpinner("End", Long.MIN_VALUE, Long.MAX_VALUE, 30);
			this.mPlay = panel.createToggleButton("Play Clip");
			this.mDeleteClip = panel.createButton("Delete");
			
			if(this.mClip.getName() != null){
				this.mName.setText(this.mClip.getName());
			}
			
			start.getModel().setValue(new Double(this.mClip.getStart()));
			end.getModel().setValue(new Double(this.mClip.getEnd()));
			
			this.mName.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					mClip.setName(mName.getText());
					updateEditor();
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
					AnimationEditor.Editor parent = (AnimationEditor.Editor)getParent();
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
			
			return panel;
		}
		
		public void setPlayState(boolean state){
			this.mPlayState = state;
		}
	
		@Override
		public Component getPropertyPanel() {
			Component panel = super.getPropertyPanel();
			this.mPlay.getModel().setSelected(this.mPlayState);
			AnimationEditor.Editor parent = (AnimationEditor.Editor)this.getParent();
			this.mDeleteClip.setEnabled(parent.getClipCount()  > 1);
			return panel;
		}
	
		@Override
		public void recycle() {
			this.mClip = null;
			this.mDeleteClip = null;
			super.recycle();
		}
		
		@Override
		public String getNodeName(){
			return this.mClip.getName();
		}
	}
}
