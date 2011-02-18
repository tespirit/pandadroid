package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.BamLog;
import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.creation.Lights;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamboo.render.Updater;
import com.tespirit.bamboo.scenegraph.Camera;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.pandadroid.R;
import com.tespirit.pandadroid.debug.Debug;
import com.tespirit.pandadroid.renderer.GL1Renderer;
import com.tespirit.pandadroid.renderer.GL2Renderer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

public class PandadroidView extends GLSurfaceView {
	private com.tespirit.bamboo.render.RenderManager mRenderer;
	private boolean mDebug;
	
	public PandadroidView(Context context){
		super(context);
		this.mDebug = false;
		this.init(context, new Color4());
	}
	
	public PandadroidView(Context context, boolean debug){
		super(context);
		this.mDebug = debug;
		this.init(context, new Color4());
	}
	
	public PandadroidView(Context context, AttributeSet attrs){
		super(context, attrs);
		
		//setup custom attributes
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.PandadroidView);
		this.mDebug = a.getBoolean(R.styleable.PandadroidView_debug, false);
		int color = a.getColor(R.styleable.PandadroidView_background_color, 0x000000FF);
		
		Color4 bgColor = new Color4();
		bgColor.set(Color.red(color), 
					Color.green(color), 
					Color.blue(color),
					Color.alpha(color));
		a.recycle();
		
		this.init(context, bgColor);
		this.mRenderer.setCamera(new Camera());
	}
	
	private void init(Context context, Color4 bgColor){
		BamLog.setLogger(new BamLog.Logger() {
			private static final String TAG = "PandaDroid";
			
			@Override
			public void warning(String message) {
				Log.w(TAG, message);
			}
			
			@Override
			public void info(String message) {
				Log.i(TAG, message);
			}
			
			@Override
			public void error(Exception exception) {
				Log.e(TAG, "An exception has occured:", exception);
			}
			
			@Override
			public void error(String message) {
				Log.e(TAG, message);
			}
			
			@Override
			public void debug(String message) {
				Log.d(TAG, message);
			}
		});
		
		Assets.init(context);
		this.initRenderer(context, bgColor);
	}
	
	private void initRenderer(Context context, Color4 bgColor){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = activityManager.getDeviceConfigurationInfo();
        if(info.reqGlEsVersion >= 0x20000){
        	this.setEGLContextClientVersion(2);
        	GL2Renderer renderer = new GL2Renderer(bgColor);
        	this.mRenderer = renderer;
        	this.setRenderer(renderer);
        } else {
        	GL1Renderer renderer = this.mDebug ? Debug.init(this) : new GL1Renderer(bgColor);
        	this.mRenderer = renderer;
        	this.setRenderer(renderer);
        }
	}
	
	public com.tespirit.bamboo.render.RenderManager getRenderer(){
		return this.mRenderer;
	}
	
	
	public Camera getCamera(){
		return this.mRenderer.getCamera();
	}
	
	public void setCamera(Camera camera){
		this.mRenderer.setCamera(camera);
	}
	
	public void addSingleUpdater(Updater updater){
		this.mRenderer.addSingleUpdater(updater);
	}
	
	public void removeSingleUpdater(Updater updater){
		this.mRenderer.removeSingleUpdater(updater);
	}
	
	public Player addAnimation(Animation animation){
		Player player = new Player();
		player.setAnimation(animation);
		this.mRenderer.addUpdater(player);
		return player;
	}
	
	public void addSceneNode(Node node){
		this.mRenderer.addScene(node);
	}
	
	public void removeBamboo(BambooAsset bamboo){
		this.mRenderer.removeScenes(bamboo.getScenes());
		this.mRenderer.removeUpdaters(bamboo.getPlayers());
	}
	
	/**
	 * This will conveniently return an array of players linked up to any animations
	 * that were imported.
	 * @param bamboo
	 * @return
	 */
	public void addBamboo(BambooAsset bamboo){
		this.mRenderer.addScenes(bamboo.getScenes());
		for(Updater updater : bamboo.getPlayers()){
			this.mRenderer.addUpdater(updater);
		}
	}
	
	public void createDefaultLight(){
		Lights.addDefaultLight(this.mRenderer);
	}
}
