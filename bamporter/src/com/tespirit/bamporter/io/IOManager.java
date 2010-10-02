package com.tespirit.bamporter.io;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import com.tespirit.bamboo.io.Bamboo;
import com.tespirit.bamboo.io.BambooAsset;

public class IOManager {
	private static Map<String, FileHandler> mFileHandlers = new HashMap<String, FileHandler>();
	private static List<FileFilter> mFilters = new ArrayList<FileFilter>();
	
	public static void init(){
		BambooHandler.init();
		ColladaHandler.init();
	}
	
	public static void registerFileHandler(FileHandler fh){
		mFileHandlers.put(fh.getExtension(), fh);
		mFilters.add(fh.getFilter());
	}
	
	public static List<FileFilter> getFilters(){
		return mFilters;
	}
	
	public static BambooAsset open(File file) throws Exception{
		String ext = file.getName();
		int dotIndex = ext.lastIndexOf('.');
		if(dotIndex != -1){
			ext = ext.substring(dotIndex);
		}
		if(mFileHandlers.containsKey(ext)){
			return mFileHandlers.get(ext).open(file);
		} else {
			throw new Exception("The ext '"+ext+"' is not supported.");
		}
	}
	
	public static void saveBamboo(BambooAsset asset, File file) throws Exception{
		FileOutputStream stream = new FileOutputStream(file);
		try{
			Bamboo.saveBamboo(asset, stream);
			stream.close();
		} catch(Exception e){
			stream.close();
			throw e;
		}
	}
	
	public static void saveSceneGraph(BambooAsset asset, File file) throws Exception{
		FileOutputStream stream = new FileOutputStream(file);
		try{
			Bamboo.saveNodes(asset.getRootSceneNodes(), stream);
			stream.close();
		} catch(Exception e){
			stream.close();
			throw e;
		}
	}
	
	public static void saveAnimation(BambooAsset asset, File file) throws Exception{
		FileOutputStream stream = new FileOutputStream(file);
		try{
			Bamboo.saveAnimations(asset.getAnimations(), stream);
			stream.close();
		} catch(Exception e){
			stream.close();
			throw e;
		}
	}
}
