package com.tespirit.pandadroid;

import android.app.Activity;
import android.os.Bundle;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Channel;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.animation.JointOrient;
import com.tespirit.bamboo.animation.JointRotateY;
import com.tespirit.bamboo.animation.JointRotateZ;
import com.tespirit.bamboo.primitives.Box;
import com.tespirit.bamboo.primitives.Plane;
import com.tespirit.bamboo.render.LightGroup;
import com.tespirit.bamboo.scenegraph.*;
import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamboo.surfaces.Texture;

import com.tespirit.pandadroid.R;
import com.tespirit.pandadroid.app.PandadroidView;
import com.tespirit.pandadroid.app.TranslateAbsolute;
import com.tespirit.pandadroid.debug.Debug;
import com.tespirit.pandadroid.io.Collada;

import android.view.*;
import android.widget.TextView;

public class Pandamonium extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	setContentView(R.layout.main);
        
        PandadroidView view = (PandadroidView)findViewById(R.id.pandadroid);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        Debug.setConsole((TextView)findViewById(R.id.console));


    	view.createTouchRotateCamera(-2);
        //view.createMotionSensorCamera(-4);
    	
    	view.setTouchDownController(new TranslateAbsolute(view));
    	
    	LightGroup lights = new LightGroup();
    	lights.createBasic();
    	view.setLightGroup(lights);
    	
    	try{
    		AnimationStuff a = this.loadAnimation("test_anim.dae");
    		view.setSceneGraph(a.j);
    		view.addTimeUpdate(a.a);
    	//	view.setSceneGraph(this.loadCollada());	
    	} catch(Exception e){
    		Debug.print(e);
    		view.setSceneGraph(this.createTestSG());
    	}
    	/*
    	AnimationStuff a = this.createTestAnimation();
    	view.addTimeUpdate(a.a);
    	view.setSceneGraph(a.j);
*/
    }
    
    public AnimationStuff loadAnimation(String file) throws Exception{
    	Collada ca = new Collada(file);
    	AnimationStuff a = new AnimationStuff();
    	a.a = ca.getAnimation();
    	a.j = ca.getSceneGraph();
    	a.a.attachSkeleton((Joint)a.j);
    	Debug.addTestAnimation(a.a);
    	return a;
    }
    
    public Node loadCollada() throws Exception{
    	Collada jC = new Collada("jocelyn.dae");
    	Collada dC = new Collada("doug.dae");
		Group g = new Group();
		Node j = jC.getSceneGraph();
		j.getTransform().translate(-0.4f, 0, 0);
		g.appendChild(j);
		
		Node d = dC.getSceneGraph();
		d.getTransform().translate(0.4f, 0, 0);
		g.appendChild(d);
		
		Debug.print(g);
		
		return g;
    }
    
    class AnimationStuff{
    	Animation a;
    	Node j;
    }
    
    public AnimationStuff createTestAnimation(){
    	JointRotateY j1 = new JointRotateY();
    	JointRotateZ j2 = new JointRotateZ();
    	JointRotateZ j3 = new JointRotateZ();
    	JointOrient j4 = new JointOrient();
    	
    	j1.appendChild(j2);
    	j2.appendChild(j3);
    	j3.appendChild(j4);
    	
    	
    	j2.getTransform().translate(0, 1, 0);
    	j3.getTransform().translate(0, 2, 0);
    	j4.getTransform().translate(0, 0.5f, 0);
    	
    	Channel c1 = new Channel();
    	for(int i = 0; i < 11; i++){
    		c1.addKeyFrame(new Channel.KeyFrame(30*i, 500*i));
    	}
    	
    	Channel c2 = new Channel();
    	c2.addKeyFrame(new Channel.KeyFrame(45, 1000));
    	c2.addKeyFrame(new Channel.KeyFrame(60, 1500));
    	c2.addKeyFrame(new Channel.KeyFrame(80, 1800));
    	
    	Channel c3 = new Channel();
    	c3.addKeyFrame(new Channel.KeyFrame(10, 2000));
    	c3.addKeyFrame(new Channel.KeyFrame(20, 2600));
    	c3.addKeyFrame(new Channel.KeyFrame(40, 3200));
    	c3.addKeyFrame(new Channel.KeyFrame(80, 3800));
    	
    	Animation a = new Animation(3);
    	a.addClip(new Clip(0, 500*11));
    	
    	a.addChannel(c1);
    	a.addChannel(c2);
    	a.addChannel(c3);
    	
    	a.attachSkeleton(j1);
    	
    	Debug.addTestAnimation(a);
    	
    	j1.createAllBones(0.25f);
    	
    	AnimationStuff ani = new AnimationStuff();
    	ani.j = j1;
    	ani.a = a;
    	return ani;
    }
    
    public Node createTestSG(){
    	Group g = new Group();
    	
    	Texture t1 = new Texture();
    	t1.setDiffuseTextureName("test.bmp");
    	
    	Color mat1 = new Color();
    	mat1.setColor(0.8f, 0.3f, 0.5f);
    	
    	Model m1 = new Model("plane");
    	m1.setPrimative(new Plane());
    	m1.setSurface(t1);

    	//adjust matrices
    	m1.getTransform().rotateY(45.0f);
    	
    	g.appendChild(m1);

    	Model m2 = new Model("box");
    	m2.setPrimative(new Box());
    	
    	//adjust matrix
    	m2.getTransform().translate(-3.0f, 0.0f, 0.0f);
    	m2.getTransform().scale(0.5f);
    	
    	g.appendChild(m2);
    	
    	Model m3 = new Model("box_material");
    	m3.setPrimative(new Box());
    	m3.setSurface(mat1);
    	
    	m3.getTransform().translate(-1.5f, 1.0f, 0.0f);
    	
    	g.appendChild(m3);
    	
    	g.getTransform().translate(1.0f, 0.0f, 0.0f);
    	g.getTransform().rotateX(20.0f);
    	g.getTransform().rotateY(-20.0f);
    	
    	return g;
    }
}