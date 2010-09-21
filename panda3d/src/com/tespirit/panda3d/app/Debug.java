package com.tespirit.panda3d.app;

import java.util.ArrayList;

import com.tespirit.panda3d.animation.Animation;
import com.tespirit.panda3d.opengl1x.RendererDebug;
import com.tespirit.panda3d.opengl1x.Renderer;
import com.tespirit.panda3d.vectors.Matrix3d;
import com.tespirit.panda3d.vectors.Vector3d;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

public class Debug {
	private static RendererDebug renderer;
	private static ArrayList<Animation> animations = new ArrayList<Animation>();
	private static int currentAnimation = -1;
	
	public static Renderer init(Panda3dView pv){
		Debug.renderer = new RendererDebug();
		pv.setOnKeyListener(new DebugKeys());
		return Debug.renderer;
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
					break;
				case KeyEvent.KEYCODE_2:
					Debug.renderer.renderBB = !Debug.renderer.renderBB;
					break;
				case KeyEvent.KEYCODE_3:
					Debug.renderer.renderNormals = !Debug.renderer.renderNormals;
					break;
				case KeyEvent.KEYCODE_4:
					Debug.renderer.renderRenderables = !Debug.renderer.renderRenderables;
					break;
				case KeyEvent.KEYCODE_5:
					Debug.renderer.renderLightPoint = !Debug.renderer.renderLightPoint;
					break;
				case KeyEvent.KEYCODE_6:
					Debug.renderer.lightsOn = !Debug.renderer.lightsOn;
					break;
				case KeyEvent.KEYCODE_N:
					if(currentAnimation != -1){
						animations.get(currentAnimation).play();
					}
					break;
				case KeyEvent.KEYCODE_M:
					if(currentAnimation != -1){
						animations.get(currentAnimation).stop();
					}
					break;
				case KeyEvent.KEYCODE_COMMA:
					if(currentAnimation > 0){
						currentAnimation--;
					}
					break;
				case KeyEvent.KEYCODE_PERIOD:
					if(currentAnimation < animations.size() - 1){
						currentAnimation++;
					}
					break;
				}
			}

			return true;
		}
	}
}
