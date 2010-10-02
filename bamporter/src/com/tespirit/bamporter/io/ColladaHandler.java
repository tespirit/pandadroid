package com.tespirit.bamporter.io;

import java.io.File;
import java.io.FileInputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamporter.app.Assets;

public class ColladaHandler extends FileHandler{
	private static ColladaHandler mFileHandler;
	
	public static void init(){
		if(mFileHandler == null){
			mFileHandler = new ColladaHandler();
		}
	}
	
	private static XmlPullParserFactory mXmlParserFactory;
	
	public BambooAsset open(File file) throws Exception{
		FileInputStream stream = new FileInputStream(file);
		try{
			XmlPullParser parser = mXmlParserFactory.newPullParser();
			parser.setInput(stream, null);
			BambooAsset asset = new Collada(parser);
			stream.close();
			return asset;
		} catch(Exception e){
			stream.close();
			throw e;
		}
	}
	
	private ColladaHandler() {
		super("COLLADA", "dae");
		try{
			mXmlParserFactory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
			mXmlParserFactory.setNamespaceAware(true);
			IOManager.registerFileHandler(this);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private class Collada extends com.tespirit.bamboo.io.Collada{
		public Collada(XmlPullParser input) throws Exception{			
			super(input);
			for(String texturePath : this.mTexturePaths){
				Assets.getInstance().addTexturePath(texturePath);
			}
		}
	}
}
