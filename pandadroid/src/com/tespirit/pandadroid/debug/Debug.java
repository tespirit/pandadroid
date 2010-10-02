package com.tespirit.pandadroid.debug;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import com.tespirit.bamboo.animation.*;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Vector3d;
import com.tespirit.pandadroid.app.PandadroidView;
import com.tespirit.pandadroid.opengl1x.Renderer;

import android.text.Selection;
import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.TextView;

public class Debug {
	private static RendererDebug renderer;
	private static ArrayList<Player> animations = new ArrayList<Player>();
	private static int currentAnimation = -1;
	private static TextView console;
	private static ArrayBlockingQueue<String> printBuffer = new ArrayBlockingQueue<String>(1000);
	private static DebugKeys dk = new DebugKeys();
	
	public static Renderer init(PandadroidView pv){
		Debug.renderer = new RendererDebug();
		pv.setOnKeyListener(Debug.dk);
		Debug.print("Panda3d debug mode active!");
		return Debug.renderer;
	}
	
	public static void addDebugListeners(View view){
		view.setOnKeyListener(Debug.dk);
	}
	
	public static void setConsole(TextView console){
		Debug.console = console;
		Debug.console.setOnKeyListener(Debug.dk);
		Debug.console.setMovementMethod(new ScrollingMovementMethod());
		//print back logged output
		if(!Debug.printBuffer.isEmpty()){
			Debug.console.append(Debug.printBuffer.poll());
			while(!Debug.printBuffer.isEmpty()){
				Debug.console.append("\n"+Debug.printBuffer.poll());
			}
		}
	}
	
	public static void print(Iterator<Node> nodes){
		while(nodes.hasNext()){
			Debug.print(nodes.next());
		}
	}
	
	public static void print(Node node){
		Debug.printNode(node, "", 0);
	}
	
	private static void printNode(Node node, String spacing, int depth){
		String className = node.toString();
		int index = className.lastIndexOf('.');
		if(index != -1){
			className = className.substring(index+1);
		}
		index = className.indexOf('@');
		if(index != -1){
			className = className.substring(0, index);
		}
		String name = node.getName();
		if(name == null){
			name = "<no name>";
		}
		
		String type = "- " + className + ": " + name;

		Debug.print(spacing+depth+type);
		
		spacing = spacing+"  ";
		
		for(int i = 0; i < node.getChildCount(); i++){
			Debug.printNode(node.getChild(i), spacing, depth+1);
		}
	}
	
	public static void print(boolean val){
		Debug.print(Boolean.toString(val));
	}
	
	public static void print(float val){
		Debug.print(Float.toString(val));
	}
	
	public static void print(int val){
		Debug.print(Integer.toString(val));
	}
	
	public static void print(long val){
		Debug.print(Long.toString(val));
	}
	
	public static void print(Vector3d val){
		String text = "[ " + val.getX() + ", " + val.getX() + ", " + val.getZ() + ", " + val.get(3)+ " ]";
		Debug.print(text);
	}
	
	public static void print(Exception e){
		StackTraceElement[] st = e.getStackTrace();
		Debug.print("Exception: " + e.getMessage());
		for(int i = 0; i < st.length; i++){
			Debug.print(st[i]);
		}
	}
	
	public static void print(StackTraceElement ste){
		String output = "    at " + 
						ste.getClassName() + 
						"." + 
						ste.getMethodName() + 
						"(" + 
						ste.getFileName() + 
						":" + 
						ste.getLineNumber() + 
						")";
		Debug.print(output);
	}
	
	public static void print(Matrix3d val){
		Debug.print(val.getXAxis());
		Debug.print(val.getYAxis());
		Debug.print(val.getZAxis());
		Debug.print(val.getTranslation());
	}
	
	public static void print(String text){
		if(Debug.console != null){
			Debug.console.append("\n"+text);
			Debug.console.requestFocus();
			Spannable log = (Spannable) Debug.console.getText();
			Selection.setSelection(log, log.length());

		} else {
			Debug.printBuffer.add(text);
		}
	}
	
	public static void drawPoint(Vector3d p){
		if(Debug.renderer != null){
			Debug.renderer.renderPoint(new RendererDebug.Point(p));
		}
	}
	
	public static void drawPoint(Vector3d p, Matrix3d m){
		if(Debug.renderer != null){
			Debug.renderer.renderPoint(new RendererDebug.Point(p, m));
		}
	}
	
	public static void addTestAnimation(Player player){
		Debug.currentAnimation = 0;
		Debug.animations.add(player);
	}
	
	public static void selectNode(Node node){
		Debug.renderer.selected = node;
		if(node != null){
			Debug.print(node.getName());
			if(node.getTransform() != null){
				Debug.print(node.getTransform());
			}
		}
	}
	
	static class DebugKeys implements OnKeyListener{
		
		public DebugKeys(){
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction() == KeyEvent.ACTION_UP){
				switch(keyCode){
				case KeyEvent.KEYCODE_D:
					Debug.renderer.renderDebug = !Debug.renderer.renderDebug;
					break;
				case KeyEvent.KEYCODE_1:
					Debug.renderer.renderAxis = !Debug.renderer.renderAxis;
					Debug.print("Show axis: "+Debug.renderer.renderAxis);
					break;
				case KeyEvent.KEYCODE_2:
					Debug.renderer.renderBoundingBox = !Debug.renderer.renderBoundingBox;
					Debug.print("Show bounding box: "+Debug.renderer.renderBoundingBox);
					break;
				case KeyEvent.KEYCODE_3:
					Debug.renderer.renderNormals = !Debug.renderer.renderNormals;
					Debug.print("Show normals: "+Debug.renderer.renderNormals);
					break;
				case KeyEvent.KEYCODE_4:
					Debug.renderer.renderRenderables = !Debug.renderer.renderRenderables;
					Debug.print("Show objects: "+Debug.renderer.renderRenderables);
					break;
				case KeyEvent.KEYCODE_5:
					Debug.renderer.renderLightPoint = !Debug.renderer.renderLightPoint;
					Debug.print("Show light location: "+Debug.renderer.renderLightPoint);
					break;
				case KeyEvent.KEYCODE_6:
					Debug.renderer.lightsOn = !Debug.renderer.lightsOn;
					Debug.print("Lights on: "+Debug.renderer.lightsOn);
					break;
				case KeyEvent.KEYCODE_TAB:
					Debug.print(Debug.renderer.getRootIterator());
					break;
				case KeyEvent.KEYCODE_N:
					if(currentAnimation != -1){
						animations.get(currentAnimation).play();
						Debug.print("Playing animation " + currentAnimation);
					}
					break;
				case KeyEvent.KEYCODE_M:
					if(currentAnimation != -1){
						animations.get(currentAnimation).pause();
						Debug.print("Stopping animation " + currentAnimation);
					}
					break;
				case KeyEvent.KEYCODE_COMMA:
					if(currentAnimation > 0){
						currentAnimation--;
						Debug.print("Previous animation: "+currentAnimation);
					}
					break;
				case KeyEvent.KEYCODE_PERIOD:
					if(currentAnimation < animations.size() - 1){
						currentAnimation++;
						Debug.print("Next animation: "+currentAnimation);
					}
					break;
				}
				
			}

			return true;
		}
	}
}
