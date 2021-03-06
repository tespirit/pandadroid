package com.tespirit.pandadroid;

import android.app.Activity;
import android.os.Bundle;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Channel;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.animation.JointRotate;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.controllers.MoveFlingController2d;
import com.tespirit.bamboo.creation.Primitives;
import com.tespirit.bamboo.io.Bamboo;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamboo.modifiers.SkinModifier;
import com.tespirit.bamboo.particles.ConstantGravity;
import com.tespirit.bamboo.particles.Ground;
import com.tespirit.bamboo.primitives.IndexBuffer;
import com.tespirit.bamboo.primitives.VertexIndices;
import com.tespirit.bamboo.primitives.VertexBuffer;
import com.tespirit.bamboo.scenegraph.Group;
import com.tespirit.bamboo.scenegraph.Model;
import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamboo.surfaces.Texture;
import com.tespirit.bamboo.vectors.Matrix3d;

import com.tespirit.pandadroid.R;
import com.tespirit.pandadroid.app.Assets;
import com.tespirit.pandadroid.app.DefaultTouchHandler;
import com.tespirit.pandadroid.app.PandadroidView;
import com.tespirit.pandadroid.debug.Debug;

import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Pandamonium extends Activity {
	
	private BambooAsset mAsset;
	private PandadroidView mView;
	private Button mPrev;
	private Button mReset;
	private Button mNext;
	private int mDemo;
	
	private static final int DEMO_TEST_SCENE = 0;
	private static final int DEMO_TEST_SKIN = 1;
	private static final int DEMO_DOUG = 2;
	private static final int DEMO_JOCELYN = 3;
	private static final int DEMO_CANDY = 4;
	private static final int DEMO_COUNT = 5;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	setContentView(R.layout.main);
       
    	this.mPrev = (Button)findViewById(R.id.prev);
    	this.mNext = (Button)findViewById(R.id.next);
    	this.mReset = (Button)findViewById(R.id.reset);
    	
    	this.mPrev.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mDemo = (mDemo-1)%DEMO_COUNT;
				loadAssets();
			}
    	});
    	
    	this.mNext.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mDemo = (mDemo+1)%DEMO_COUNT;
				loadAssets();
			}
    	});
    	
    	this.mReset.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				loadAssets();
			}
    	});
    	
        this.mView = (PandadroidView)findViewById(R.id.pandadroid);
        this.mView.setFocusable(true);
        this.mView.setFocusableInTouchMode(true);
        Debug.setConsole((TextView)findViewById(R.id.console));

        this.mView.createDefaultLight();
        
        DefaultTouchHandler touchHandler = new DefaultTouchHandler(this.mView);
        touchHandler.makeCameraControllable().set(1.5f, 0, 45);
        MoveFlingController2d fling = touchHandler.makeNodesFlingable();
        fling.getParticles().addForce(new ConstantGravity());
        Ground ground = new Ground();
        ground.setHeight(-2.0f);
        fling.getParticles().addForce(ground);
        
        this.mDemo = DEMO_TEST_SCENE;
        loadAssets();
    }
  
    private BambooAsset getDemo(){
    	try{
	    	switch(this.mDemo){
	    	case DEMO_TEST_SCENE:
				Debug.print("Loading Test Scene...");
	    		return this.createTestScene();
	    	case DEMO_TEST_SKIN:
				Debug.print("Loading Test Skin...");
	    		return this.createTestSkin();
	    	case DEMO_CANDY:
				Debug.print("Loading Candy Demo...");
	    		BambooAsset b = Assets.loadBamboo("candy.bam");
	    		if(b.getPlayers().size() > 0){
	    			b.getPlayers().get(0).play();
	    		}
	    		return b;
	    	case DEMO_DOUG:
	    		Debug.print("Loading Doug Demo...");
	    		return Assets.loadBamboo("doug.bam");
	    	case DEMO_JOCELYN:
	    		Debug.print("Loading Doug Demo...");
	    		return Assets.loadBamboo("jocelyn.bam");
	    	}
    	} catch(Exception e){
    		Debug.print(e);
    	}
    	return this.createTestScene();
    }
    
    private void loadAssets(){
    	if(this.mAsset != null){
    		this.mView.removeBamboo(this.mAsset);
    	}
    	this.mAsset = getDemo();
    	this.mView.addBamboo(this.mAsset);
    }
    
    public BambooAsset createTestScene(){
    	Group g = new Group();
    	
    	Texture t1 = new Texture();
    	t1.setDiffuseTextureName("test.bmp");
    	
    	Color mat1 = new Color();
    	mat1.setColor(0.8f, 0.3f, 0.5f);
    	
    	Model m1 = new Model("plane");
    	m1.setPrimitive(new Primitives.Plane());
    	m1.setSurface(t1);

    	//adjust matrices
    	m1.getTransform().rotateY(45.0f);
    	
    	g.appendChild(m1);

    	Model m2 = new Model("box");
    	m2.setPrimitive(new Primitives.Cube());
    	
    	//adjust matrix
    	m2.getTransform().translate(-3.0f, 0.0f, 0.0f);
    	m2.getTransform().scale(0.5f);
    	
    	g.appendChild(m2);
    	
    	Model m3 = new Model("box_material");
    	m3.setPrimitive(new Primitives.Cube());
    	m3.setSurface(mat1);
    	
    	m3.getTransform().translate(-1.5f, 1.0f, 0.0f);
    	
    	g.appendChild(m3);
    	
    	g.getTransform().translate(1.0f, 0.0f, 0.0f);
    	g.getTransform().rotateX(20.0f);
    	g.getTransform().rotateY(-20.0f);
    	
    	Bamboo assets = new Bamboo();
    	assets.getScenes().add(g);
    	
    	return assets;
    }
    
	private BambooAsset createTestSkin(){
		VertexIndices mesh = new VertexIndices(18, 8, new int[]{VertexBuffer.POSITION,VertexBuffer.NORMAL});
		
		VertexBuffer vb = mesh.getVertexBuffer();
		vb.lock();
		
		vb.addPosition(-0.5f,0,0);
		vb.addNormal(0,0,1);
		
		vb.addPosition(0.5f,0,0);
		vb.addNormal(0,0,1);
		
		vb.addPosition(-0.5f,1,0);
		vb.addNormal(0,0,1);
		
		vb.addPosition(0.5f,1,0);
		vb.addNormal(0,0,1);
		
		vb.addPosition(-0.5f,2,0);
		vb.addNormal(0,0,1);
		
		vb.addPosition(0.5f,2,0);
		vb.addNormal(0,0,1);
		
		vb.addPosition(-0.5f,3,0);
		vb.addNormal(0,0,1);
		
		vb.addPosition(0.5f,3,0);
		vb.addNormal(0,0,1);
		
		vb.unlock();
		
		IndexBuffer ib = mesh.getIndexBuffer();
		ib.lock();
		
		ib.addTriangle(0, 1, 2);
		ib.addTriangle(1, 3, 2);
		
		ib.addTriangle(2, 3, 4);
		ib.addTriangle(3, 5, 4);
		
		ib.addTriangle(4, 5, 6);
		ib.addTriangle(5, 7, 6);
		
		ib.unlock();
		
		Model model = new Model();
		
		model.setPrimitive(mesh);
		
		SkinModifier skin = new SkinModifier();
		
		Matrix3d[] binds = new Matrix3d[]{
			new Matrix3d(),
			new Matrix3d().translate(0,1,0).invert(),
			new Matrix3d().translate(0, 2, 0).invert()
		};
		skin.attachRig(new String[]{"1","2","3"}, binds);
		
		float[] weights = new float[]{
			1,
			1,
			0.5f, 0.5f,
			0.5f, 0.5f,
			0.5f, 0.5f,
			0.5f, 0.5f,
			1,
			1
		};
		byte[] strides = new byte[]{
			1,
			1,
			2,
			2,
			2,
			2,
			1,
			1
		};
		byte[] skeleton = new byte[]{
			0,
			0,
			0, 1,
			0, 1,
			1, 2,
			1, 2,
			2,
			2
		};
		skin.setSkeletonMap(skeleton);
		skin.setWeights(weights);
		skin.setWeightStrides(strides);
		
		mesh.addModifier(skin);
		//mesh.renderWireFrame();
		
		
		JointRotate joint1 = new JointRotate("1");
		JointRotate joint2 = new JointRotate("2");
		JointRotate joint3 = new JointRotate("3");
		joint1.appendChild(joint2);
		joint2.appendChild(joint3);
		
		joint2.getTransform().translate(0, 1, 0);
		joint3.getTransform().translate(0, 1, 0);
		
		joint2.getTransform().rotateZ(45);
		joint3.getTransform().rotateZ(45);
		
		joint1.createAllBones(0.1f);
		
		Channel c = new Channel();
		for(int i = 0; i < 30; i++){
			c.addKeyFrame(new Channel.KeyFrame(i*0.75f, i*100));
		}
		
		Channel blank = new Channel();
		Animation animation = new Animation(9);
			for(int i = 0; i < 3; i++){
			animation.addChannel(blank);
			animation.addChannel(c);
			animation.addChannel(blank);
		}
		animation.addClip(new Clip("bob", 0, 3000));
		
		Bamboo asset = new Bamboo();
		asset.getScenes().add(joint1);
		asset.getScenes().add(model);
		asset.getAnimations().add(animation);
		Player player = new Player();
		player.setAnimation(animation);
		player.setSkeleton(joint1);
		asset.getPlayers().add(player);
		player.play();
		
		return asset;
	}
}