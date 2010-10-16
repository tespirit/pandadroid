package com.tespirit.bamporter.standardEditors;

import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamporter.properties.ColorProperty;

public class ColorEditor extends SurfaceEditor{
	@Override
	public Editor createEditor(Object object) {
		return new Editor((Color)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Color.class;
	}
	
	public class Editor extends SurfaceEditor.Editor{

		/**
		 * 
		 */
		private static final long serialVersionUID = 503760691076126045L;

		private Color mColor;
		
		protected Editor(Color color) {
			super(color);
			this.mColor = color;
			this.addProperty(new ColorProperty.Bind("Diffuse Color", this.mColor.getColor()));
		}	
	}
}
