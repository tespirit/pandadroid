package com.tespirit.bamporter.standardEditors;

import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.vectors.Range;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.PropertyTreeNodeEditor;
import com.tespirit.bamporter.editor.Util;
import com.tespirit.bamporter.properties.ButtonProperty;
import com.tespirit.bamporter.properties.LongRangeProperty;
import com.tespirit.bamporter.properties.StringProperty;

public class ClipEditor implements Factory{
	@Override
	public Editor createEditor(Object object) {
		return new ClipEditor.Editor((Clip)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Clip.class;
	}
	
	public class Editor extends PropertyTreeNodeEditor{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4480406979875461348L;
		private Clip mClip;
		
		protected Editor(Clip clip){
			super(clip, false);
			this.mClip = clip;
			this.addProperty(new StringProperty("Name"){
				@Override
				public void setValue(String value) {
					mClip.setName(value);
				}
				@Override
				public String getValue() {
					return mClip.getName();
				}
			});
			this.addProperty(new LongRangeProperty("Range",30L){
				@Override
				public void setValue(Range<Long> value) {
					mClip.setStart(value.getMin());
					mClip.setEnd(value.getMax());
				}
				@Override
				public Range<Long> getValue() {
					return new Range<Long>(mClip.getStart(), mClip.getEnd());
				}
			});
			this.addProperty(new ButtonProperty("Delete"){
				@Override
				public void onClick() {
					deleteClip();
				}
			});
		}
		
		public void deleteClip(){
			AnimationEditor.Editor parent = (AnimationEditor.Editor)this.getParent();
			if(parent.getClipCount()  == 1){
				Util.alertError("Sorry, you must have at least one clip.");
			} else {
				this.removeEditorFromParent();
				this.recycle();
			}
		}
		
		public Clip getClip(){
			return this.mClip;
		}
	
		@Override
		public void recycle() {
			this.mClip = null;
			super.recycle();
		}
		
		@Override
		public String getNodeName(){
			return this.mClip.getName();
		}
	}
}
