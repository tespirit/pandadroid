package com.tespirit.pandadroid.app;

import java.util.ArrayList;
import java.util.List;

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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class PandadroidView extends GLSurfaceView {
	private com.tespirit.bamboo.render.RenderManager mRenderer;
	private boolean mDebug;
	@SuppressWarnings("unused")
	private Context mContext;

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
		Assets.init(context);
		this.mContext = context;
		
		//TODO:smartly create a renderer based on the availible graphics api.
		this.initOpenGl1x(bgColor);
	}
	
	private void initOpenGl1x(Color4 bgColor){
		com.tespirit.pandadroid.opengl1x.Renderer gl1x;
		if(this.mDebug){
			gl1x = Debug.init(this);
		} else {
			gl1x = new com.tespirit.pandadroid.opengl1x.Renderer(bgColor);
		}
		this.mRenderer = gl1x;
		this.setRenderer(gl1x);
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
	
	/**
	 * This assumes there is 1 animation to load.
	 * @param bamboo
	 * @return
	 */
	public Player addBambooSingleAnimation(BambooAsset bamboo){
		this.mRenderer.addScenes(bamboo.getScenes());
		return this.addAnimation(bamboo.getAnimations().get(0));
	}
	
	/**
	 * This will conveniently return an array of players linked up to any animations
	 * that were imported.
	 * @param bamboo
	 * @return
	 */
	public List<Player> addBamboo(BambooAsset bamboo){
		this.mRenderer.addScenes(bamboo.getScenes());
		ArrayList<Player> players = new ArrayList<Player>(bamboo.getAnimations().size());
		for(Animation a : bamboo.getAnimations()){
			players.add(this.addAnimation(a));
		}
		return players;
	}
	
	public void createDefaultLight(){
		Lights.addDefaultLight(this.mRenderer);
	}
}
