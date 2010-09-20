package com.tespirit.panda3d.app;

import com.tespirit.panda3d.opengl1x.RendererDebug;
import com.tespirit.panda3d.opengl1x.Renderer;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

public class Debug {
	private static DebugKeys dk;
	private static Panda3dView pv;
	
	public static Renderer init(Panda3dView pv){
		RendererDebug renderer = new RendererDebug();
		pv.setOnKeyListener(new DebugKeys(renderer));
		return renderer;
	}
	
	public static boolean handleKeyInput(int keyCode, KeyEvent event){
		if(dk != null){
			return Debug.dk.onKey(pv, keyCode, event);
		} else {
			return true;
		}
	}
	
	static class DebugKeys implements OnKeyListener{
		
		private RendererDebug renderer;
		
		public DebugKeys(RendererDebug renderer){
			this.renderer = renderer;
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction() == KeyEvent.ACTION_UP){
				switch(keyCode){
				case KeyEvent.KEYCODE_1:
					this.renderer.renderAxis = !this.renderer.renderAxis;
					break;
				case KeyEvent.KEYCODE_2:
					this.renderer.renderBB = !this.renderer.renderBB;
					break;
				case KeyEvent.KEYCODE_3:
					this.renderer.renderNormals = !this.renderer.renderNormals;
					break;
				case KeyEvent.KEYCODE_4:
					this.renderer.renderRenderables = !this.renderer.renderRenderables;
					break;
				case KeyEvent.KEYCODE_5:
					this.renderer.renderLightPoint = !this.renderer.renderLightPoint;
					break;
				case KeyEvent.KEYCODE_6:
					this.renderer.lightsOn = !this.renderer.lightsOn;
				}
			}

			return true;
		}
	}
}
