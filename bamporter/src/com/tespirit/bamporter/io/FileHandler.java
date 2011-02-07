package com.tespirit.bamporter.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.tespirit.bamboo.io.BambooAsset;

public abstract class FileHandler {
	private FileFilterManager.ExtensionFilter mFilter;
	
	protected FileHandler(String name, String extension){
		this.mFilter = new FileFilterManager.ExtensionFilter(name, extension);
	}
	
	public FileFilter getFilter(){
		return this.mFilter;
	}
	
	public String getExtension(){
		return this.mFilter.getExtension();
	}
	
	public abstract BambooAsset open(File file) throws Exception;

}
