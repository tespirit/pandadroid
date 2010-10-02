package com.tespirit.pandadroid.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.io.Bamboo;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamboo.io.Collada;
import com.tespirit.bamboo.scenegraph.Node;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Xml;

/**
 * This is a static class for grabbing resources used by the panda 3d engine.
 * it provides convenient functions for particular types of resources to load,
 * such as textures, models, animations, etc.
 * As this engine evolves, more special formats should be accounted for (like sound).
 * @author Todd Espiritu Santo
 *
 */
public class Assets {
	private android.content.res.AssetManager assets;
	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private static Assets manager;
	
	/**
	 * this must be called first.
	 * @param contex
	 */
	public static void init(Context contex){
		Assets.manager = new Assets(contex);
	}
	
	public static Assets getManager(){
		return Assets.manager;
	}
	
	private Assets(Context contex){
		this.assets = contex.getAssets();
	}
	
	public InputStream openStream(String name){
		InputStream stream;
		
		try{
			stream = this.assets.open(name);
		} catch (IOException e){
			return null;
		}
		return stream;
	}
	
	public Document openXmlDom(String xmlName){
        try{
        	DocumentBuilder builder = this.factory.newDocumentBuilder();
        	InputStream stream = this.openStream(xmlName);
        	Document doc = builder.parse(stream);
        	stream.close();
        	return doc;
        	
        } catch(Exception e){
        	return null;
        }
	}
	
	public Bitmap openBitmap(String textureName){
		InputStream stream = this.openStream(textureName);
		if(stream == null){
			return null;
		}
		
		Bitmap bitmap;
		
		try{
			stream = this.assets.open(textureName);
		} catch (IOException e){
			return null;
		}
		
		try{
			bitmap = BitmapFactory.decodeStream(stream);
		} catch (Exception e){
			return null;
		} finally {
			try {
				stream.close();
				stream = null;
				
			} catch (IOException e) { }
		}
		
		return bitmap;
	}
	
	public List<Node> loadNodes(String name) throws Exception{
		InputStream stream = this.openStream(name);
		try{
			List<Node> nodes = Bamboo.loadNodes(stream);
			stream.close();
			return nodes;
		}catch(Exception e){
			stream.close();
			throw e;
		}
	}
	
	public List<Animation> loadAnimations(String name) throws Exception{
		InputStream stream = this.openStream(name);
		try{
			List<Animation> animations = Bamboo.loadAnimations(stream);
			stream.close();
			return animations;
		}catch(Exception e){
			stream.close();
			throw e;
		}
	}
	
	public BambooAsset loadBamboo(String name) throws Exception{
		InputStream stream = this.openStream(name);
		try{
			BambooAsset asset = new Bamboo(stream);
			stream.close();
			return asset;
		} catch(Exception e){
			stream.close();
			throw e;
		}
	}
	
	public BambooAsset loadCollada(String name) throws Exception{
		InputStream stream = this.openStream(name);
		try{
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(stream, null);
			BambooAsset asset = new Collada(parser);
			stream.close();
			return asset;
		}catch (Exception e){
			stream.close();
			throw e;
		}
	}
}
