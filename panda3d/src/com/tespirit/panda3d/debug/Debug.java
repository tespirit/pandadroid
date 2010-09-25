package com.tespirit.panda3d.debug;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.tespirit.panda3d.animation.Animation;
import com.tespirit.panda3d.opengl1x.Renderer;
import com.tespirit.panda3d.scenegraph.Node;
import com.tespirit.panda3d.scenegraph.Group;
import com.tespirit.panda3d.scenegraph.Model;
import com.tespirit.panda3d.render.LightGroup;
import com.tespirit.panda3d.render.Light;
import com.tespirit.panda3d.render.Camera;
import com.tespirit.panda3d.animation.*;
import com.tespirit.panda3d.app.Panda3dView;
import com.tespirit.panda3d.vectors.Matrix3d;
import com.tespirit.panda3d.vectors.Vector3d;

import android.text.Selection;
import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.TextView;

public class Debug {
	private static RendererDebug renderer;
	private static ArrayList<Animation> animations = new ArrayList<Animation>();
	private static int currentAnimation = -1;
	private static TextView console;
	private static ArrayBlockingQueue<String> printBuffer = new ArrayBlockingQueue<String>(1000);
	private static DebugKeys dk = new DebugKeys();
	
	public static Renderer init(Panda3dView pv){
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
	
	public static void print(Node node){
		Debug.printNode(node, "", 0);
	}
	
	private static void printNode(Node node, String spacing, int depth){
		String type;
		if(node instanceof Group){
			type = "- Group: ";
		} else if (node instanceof Model){
			type = "- Model: ";
		} else if (node instanceof JointOrient){
			type = "- JointOrient: ";
		} else if (node instanceof JointRotate){
			type = "- JointRotate: ";
		} else if (node instanceof JointTranslate){
			type = "- JointTranslate: ";
		} else if (node instanceof Joint){
			type = "- Joint: ";
		} else if (node instanceof LightGroup){
			type = "- LightGroup: ";
		} else if (node instanceof Light){
			type = "- Light: ";
		} else if (node instanceof Camera){
			type = "- Camera: ";
		} else {
			type = "- Node: ";
		}
		String name = node.getName();
		if(name == null){
			name = "<no name>";
		}

		Debug.print(spacing+depth+type + name);
		
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
	
	public static void addTestAnimation(Animation a){
		Debug.currentAnimation = 0;
		Debug.animations.add(a);
	}
	
	static class DebugKeys implements OnKeyListener{
		
		public DebugKeys(){
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction() == KeyEvent.ACTION_UP){
				switch(keyCode){
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
					Debug.print(Debug.renderer.getSceneGraph());
					break;
				case KeyEvent.KEYCODE_N:
					if(currentAnimation != -1){
						animations.get(currentAnimation).play();
						Debug.print("Playing animation " + currentAnimation);
					}
					break;
				case KeyEvent.KEYCODE_M:
					if(currentAnimation != -1){
						animations.get(currentAnimation).stop();
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
