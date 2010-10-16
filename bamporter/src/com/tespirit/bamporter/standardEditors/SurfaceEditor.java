package com.tespirit.bamporter.standardEditors;

import com.tespirit.bamboo.surfaces.Surface;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.PropertyTreeNodeEditor;
import com.tespirit.bamporter.properties.ComboProperty;

public class SurfaceEditor implements Factory{

	@Override
	public Editor createEditor(Object object) {
		return new Editor((Surface)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Surface.class;
	}
	
	public class Editor extends PropertyTreeNodeEditor{

		/**
		 * 
		 */
		private static final long serialVersionUID = 503760691076126045L;

		private Surface mSurface;
		
		private class BlendDisplay{
			byte mValue;
			String mName;
			private BlendDisplay(String name, byte value){
				this.mName = name;
				this.mValue = value;
			}
			@Override
			public String toString(){
				return this.mName;
			}
		}
		
		protected Editor(Surface surface) {
			super(surface);
			this.mSurface = surface;
			ComboProperty<BlendDisplay> blending = this.addProperty(new ComboProperty<BlendDisplay>("Blending"){
				@Override
				public void setValue(BlendDisplay value) {
					mSurface.setBlending(value.mValue);
				}
			});
			
			blending.addItem(new BlendDisplay("None", Surface.BLEND_NONE));
			blending.addItem(new BlendDisplay("Alpha", Surface.BLEND_ALPHA));
			blending.addItem(new BlendDisplay("Additive", Surface.BLEND_ADD));
			blending.selectIndex(this.mSurface.getBlendMethod());
		}
		
	}
	
}
