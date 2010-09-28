package com.tespirit.pandadroid.convert;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import com.tespirit.pandadroid.app.Assets;

import android.util.Xml;
public class Collada extends com.tespirit.bamboo.io.Collada{

	/**
	 * use this to disable normals from importing since normals are only
	 * needed if there is lighting.
	 * @param fileName
	 * @param normals
	 * @throws Exception
	 */
	public Collada(String fileName, boolean normals)throws Exception{
		super(normals);

		XmlPullParser parser = Xml.newPullParser();
		InputStream input = Assets.getManager().openStream(fileName);
		parser.setInput(input, null);
		this.init(parser);
		parser = null;
		input.close();
	}
	
	public Collada(String fileName)throws Exception{
		this(fileName, true);
	}
}
