package com.tespirit.pandadroid.io;


import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.pandadroid.app.Assets;

public class Bamboo extends com.tespirit.bamboo.io.Bamboo{
	
	public static Node loadNode(String name) throws Exception{
		return com.tespirit.bamboo.io.Bamboo.loadNode(Assets.getManager().openStream(name));
	}
	
	public static Animation loadAnimation(String name) throws Exception{
		return com.tespirit.bamboo.io.Bamboo.loadAnimation(Assets.getManager().openStream(name));
	}

	public Bamboo(String name) throws Exception {
		super(Assets.getManager().openStream(name));
	}

}
