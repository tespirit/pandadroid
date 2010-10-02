package com.tespirit.bamporter.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

public class Assets {
	
	private static Assets mAssets;
	
	public static Assets getInstance(){
		if(mAssets == null){
			mAssets = new Assets();
		}
		return mAssets;
	}
	
	private Map<String, File> mAbsolutePaths;
	private List<File> mPaths;
	GLProfile mGlProfile;
	
	private Assets(){
		this.mAbsolutePaths = new HashMap<String, File>();
		this.mPaths = new ArrayList<File>();
	}
	
	public void setGlProfile(GLProfile glProfile){
		this.mGlProfile = glProfile;
	}
	
	//use this to add a path, either absolute name for a texture or a directory.
	//absolute paths will be checked first, then directories.
	public void addTexturePath(String path){
		File file = new File(path);
		if(file.isDirectory()){
			this.mPaths.add(file);
		} else if(file.isFile()){
			this.mAbsolutePaths.put(file.getName(), file);
		}
	}
	
	public void exportTextures(String fileName, String outPath){
		
	}
	
	public TextureData openTexture(String fileName) throws Exception{
		File file = getTexturePath(fileName);
		InputStream stream = new FileInputStream(file);
		return TextureIO.newTextureData(this.mGlProfile, stream, false, this.getExtension(file));
	}
	
	private String getExtension(File file){
		int index = file.getName().lastIndexOf('.');
		if(index != -1 && index < file.getName().length()){
			return file.getName().substring(index+1);
		} else {
			return "";
		}
	}
	
	private File getTexturePath(String textureName) throws Exception{
		File file = new File(textureName);
		if(file.isFile()){
			return file;
		}
		if(this.mAbsolutePaths.containsKey(file.getName())){
			return this.mAbsolutePaths.get(file.getName());
		} else { //search file paths!
			for(File path : this.mPaths){
				File fullPath = new File(path.getPath(),file.getName());
				if(fullPath.exists()){
					return fullPath;
				}
			}
		}
		throw new IOException("The file " + textureName + " could not be located.");
	}
}
