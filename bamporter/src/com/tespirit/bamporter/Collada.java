package com.tespirit.bamporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class Collada extends com.tespirit.bamboo.io.Collada{
	private static XmlPullParserFactory xmlParserFactory;
	
	public Collada(String fileName) throws Exception{
		this(new File(fileName));
	}
	
	public Collada(File file) throws Exception{
		if(Collada.xmlParserFactory == null){
			Collada.xmlParserFactory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
			xmlParserFactory.setNamespaceAware(true);
		}
		
		InputStreamReader input = new InputStreamReader(new FileInputStream(file)); 
		
		XmlPullParser parser = Collada.xmlParserFactory.newPullParser();
		parser.setInput(input);
		this.init(parser);
		parser = null;
		input.close();
	}
}
