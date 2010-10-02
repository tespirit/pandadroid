package com.tespirit.bamporter.io;

import java.io.File;
import java.io.FileInputStream;

import com.tespirit.bamboo.io.BambooAsset;

public class BambooHandler extends FileHandler{
	private static BambooHandler mFileHandler;
	
	public static void init(){
		if(mFileHandler == null){
			mFileHandler = new BambooHandler();
		}
	}
	
	public static FileHandler getInstance(){
		return mFileHandler;
	}
	
	private BambooHandler() {
		super("Bamboo Asset", "bam");
		IOManager.registerFileHandler(this);
	}
	
	@Override
	public BambooAsset open(File file) throws Exception {
		FileInputStream stream = new FileInputStream(file);
		try{
			BambooAsset asset = new Bamboo(stream);
			stream.close();
			return asset;
		} catch(Exception e){
			stream.close();
			throw e;
		}
	}

	private class Bamboo extends com.tespirit.bamboo.io.Bamboo{
		public Bamboo(FileInputStream stream) throws Exception{
			super(stream);
		}
	}
}
