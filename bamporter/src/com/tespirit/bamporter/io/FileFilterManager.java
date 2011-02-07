package com.tespirit.bamporter.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileFilterManager {
	
	List<FileFilter> mFilters;
	
	class AllValidFilter extends FileFilter{
		@Override
		public boolean accept(File file) {
			for(FileFilter filter : mFilters){
				if(filter.accept(file)){
					return true;
				}
			}
			return false;
		}

		@Override
		public String getDescription() {
			return "All valid files";
		}
	}
	
	public static class ExtensionFilter extends FileFilter{
		String mName;
		String mExtension;
		
		public ExtensionFilter(String name, String extension){
			extension = '.'+extension.toLowerCase();
			this.mName = name + " (*"+extension+")";
			this.mExtension = extension;
		}
		
		@Override
		public boolean accept(File file) {
			if(file.isDirectory()){
				return true;
			} else {
				return file.getName().toLowerCase().endsWith(this.mExtension);
			}
		}

		@Override
		public String getDescription() {
			return mName;
		}
		
		public String getExtension(){
			return this.mExtension;
		}
	}
	
	public FileFilterManager(){
		this.mFilters = new ArrayList<FileFilter>();
	}

	
	
	public void addExtension(String name, String extension){
		this.mFilters.add(new ExtensionFilter(name, extension));
	}
	
	public void addFilter(FileFilter filter){
		this.mFilters.add(filter);
	}
	
	public void setFilters(JFileChooser fileChooser){
		for(FileFilter filter : this.mFilters){
			fileChooser.addChoosableFileFilter(filter);
		}
		fileChooser.addChoosableFileFilter(new AllValidFilter());
	}
}
