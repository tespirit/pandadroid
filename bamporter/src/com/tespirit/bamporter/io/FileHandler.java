package com.tespirit.bamporter.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.tespirit.bamboo.io.BambooAsset;

public abstract class FileHandler {
	private String mName;
	private String mExtension;
	private FileFilter mFilter;
	
	class Filter extends FileFilter{

		@Override
		public boolean accept(File arg0) {
			if(arg0.isDirectory()){
				return true;
			} else {
				return arg0.getName().toLowerCase().endsWith(mExtension);
			}
		}

		@Override
		public String getDescription() {
			return mName;
		}
	}
	
	protected FileHandler(String name, String extension){
		extension = extension.toLowerCase();
		this.mExtension = "." + extension; 
		this.mName = name + "(*."+extension+")";
		this.mFilter = new Filter();
	}
	
	public FileFilter getFilter(){
		return this.mFilter;
	}
	
	public String getExtension(){
		return this.mExtension;
	}
	
	public abstract BambooAsset open(File file) throws Exception;

}
