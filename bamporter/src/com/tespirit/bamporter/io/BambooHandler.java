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

	private class Bamboo extends com.tespirit.bamboo.io.Bamboo{
		public Bamboo(File file) throws Exception{
			super(new FileInputStream(file));
		}
	}

	@Override
	public BambooAsset open(File file) throws Exception {
		return new Bamboo(file);
	}
}
