package com.tespirit.bamboo.io;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.w3c.dom.Node;

import android.view.animation.Animation;

/**
 * A simple io class for loading objects. This way the io can change without
 * having to adjust higher level code.
 * @author Todd Espiritu Santo
 *
 */
public class Bamboo {
	public static Node loadNode(InputStream stream) throws Exception{
		ObjectInputStream o = new ObjectInputStream(stream);
		Object obj = o.readObject();
		o.close();
		if(obj instanceof Node){
			return (Node)obj;
		} else {
			throw new Exception("This is not a valid scene graph file.");
		}
	}
	
	public static Animation loadAnimation(InputStream stream) throws Exception{
		ObjectInputStream o = new ObjectInputStream(stream);
		Object obj = o.readObject();
		o.close();
		if(obj instanceof Animation){
			return (Animation)obj;
		} else {
			throw new Exception("This is not a valid animation file.");
		}
	}
	
	public static void save(Node root, OutputStream stream) throws Exception{
		ObjectOutputStream o = new ObjectOutputStream(stream);
		o.writeObject(root);
		o.close();
	}
	
	public static void save(Animation animation, OutputStream stream) throws Exception{
		ObjectOutputStream o = new ObjectOutputStream(stream);
		o.writeObject(animation);
		o.close();
	}
}
