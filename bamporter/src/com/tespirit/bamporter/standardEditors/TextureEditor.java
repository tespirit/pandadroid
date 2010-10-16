package com.tespirit.bamporter.standardEditors;

import com.tespirit.bamboo.surfaces.Texture;
import com.tespirit.bamporter.properties.StringProperty;

public class TextureEditor extends SurfaceEditor{
	@Override
	public Editor createEditor(Object object) {
		return new Editor((Texture)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Texture.class;
	}
	
	public class Editor extends SurfaceEditor.Editor{

		/**
		 * 
		 */
		private static final long serialVersionUID = 503760691076126045L;

		private Texture mTexture;
		
		protected Editor(Texture texture) {
			super(texture);
			this.mTexture = texture;
			this.addProperty(new StringProperty("Diffuse"){

				@Override
				public void setValue(String value) {
					mTexture.setDiffuseTextureName(value);
				}

				@Override
				public String getValue() {
					return mTexture.getDiffuseTextureName();
				}
			});
		}	
	}
}
