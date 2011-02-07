package com.tespirit.bamporter.standardEditors;

import java.io.File;

import com.tespirit.bamboo.surfaces.Texture;
import com.tespirit.bamporter.app.Assets;
import com.tespirit.bamporter.app.BamporterFrame;
import com.tespirit.bamporter.app.Preferences;
import com.tespirit.bamporter.properties.ResourceProperty;

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
			this.addProperty(new ResourceProperty("Diffuse", Assets.getTextureFilterManger()){

				@Override
				public void setValue(File value) {
					mTexture.setDiffuseTextureName(value.getName());
					Assets.addTexturePath(value.getParent());
					BamporterFrame.getInstance().getRenderManger().refreshResource(mTexture);
				}

				@Override
				public File getValue() {
					return new File(Preferences.getSaveDirectory(), mTexture.getDiffuseTextureName());
				}
			});
		}	
	}
}
