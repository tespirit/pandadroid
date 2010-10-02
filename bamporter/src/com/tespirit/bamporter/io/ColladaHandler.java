package com.tespirit.bamporter.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
		return new Collada(file);
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
		public Collada(File file) throws Exception{			
			InputStreamReader input = new InputStreamReader(new FileInputStream(file)); 
			
			XmlPullParser parser = mXmlParserFactory.newPullParser();
			parser.setInput(input);
			this.init(parser);
			parser = null;
			input.close();
			for(String texturePath : this.mTexturePaths){
				Assets.getInstance().addTexturePath(texturePath);
			}
		}
	}
}
