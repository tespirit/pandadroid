package com.tespirit.panda3d;

import android.app.Activity;
import android.os.Bundle;

import com.tespirit.panda3d.surfaces.Color;
import com.tespirit.panda3d.surfaces.Texture;
import com.tespirit.panda3d.render.LightGroup;
import com.tespirit.panda3d.scenegraph.*;

import com.tespirit.panda3d.animation.Animation;
import com.tespirit.panda3d.animation.Channel;
import com.tespirit.panda3d.animation.Joint;
import com.tespirit.panda3d.animation.JointOrient;
import com.tespirit.panda3d.animation.JointRotateY;
import com.tespirit.panda3d.animation.JointRotateZ;
import com.tespirit.panda3d.app.Panda3dView;
import com.tespirit.panda3d.app.TranslateAbsolute;
import com.tespirit.panda3d.convert.ColladaAndroid;
import com.tespirit.panda3d.debug.Debug;
import com.tespirit.panda3d.render.Renderer;
import com.tespirit.panda3d.primitives.Box;
import com.tespirit.panda3d.primitives.Plane;

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
        
        Panda3dView view = (Panda3dView)findViewById(R.id.panda3d);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        Debug.setConsole((TextView)findViewById(R.id.console));


    	view.createTouchRotateCamera(-2);
        //view.createMotionSensorCamera(-4);
    	
    	view.setTouchDownController(new TranslateAbsolute(view));
    	
    	Renderer r = view.getRenderer();
    	
    	LightGroup lights = new LightGroup();
    	lights.createBasic();
    	r.setLightGroup(lights);
    	
    	try{
    		AnimationStuff a = this.loadCandy();
    		r.setSceneGraph(a.j);
    		r.addTimeUpdate(a.a);
    	//	r.setSceneGraph(this.loadCollada());	
    	} catch(Exception e){
    		Debug.print(e);
    		r.setSceneGraph(this.createTestSG());
    	}
    	
    	//AnimationStuff a = this.createTestAnimation();
    	//r.addTimeUpdate(a.a);
    	//r.setSceneGraph(a.j);

   		
   		//view.setFocusableInTouchMode(true);
    }
    
    public AnimationStuff loadCandy() throws Exception{
    	ColladaAndroid candyC = new ColladaAndroid("test_anim.dae");
    	AnimationStuff a = new AnimationStuff();
    	a.a = candyC.getAnimation();
    	a.j = candyC.getSceneGraph();
    	a.a.attachSkeleton((Joint)a.j);
    	Debug.addTestAnimation(a.a);
    	return a;
    }
    
    public Node loadCollada() throws Exception{
    	ColladaAndroid jC = new ColladaAndroid("jocelyn.dae");
    	ColladaAndroid dC = new ColladaAndroid("doug.dae");
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
    		c1.addKeyFrame(new Channel.KeyFrame(30*i, 1000*i));
    	}
    	c1.setRange(0, 1000*12);
    	
    	Channel c2 = new Channel();
    	c2.addKeyFrame(new Channel.KeyFrame(45, 1000));
    	c2.addKeyFrame(new Channel.KeyFrame(60, 1500));
    	c2.addKeyFrame(new Channel.KeyFrame(80, 1800));
    	c2.setRange(0, 2000);
    	
    	Channel c3 = new Channel();
    	c3.addKeyFrame(new Channel.KeyFrame(10, 2000));
    	c3.addKeyFrame(new Channel.KeyFrame(20, 2600));
    	c3.addKeyFrame(new Channel.KeyFrame(40, 3200));
    	c3.addKeyFrame(new Channel.KeyFrame(80, 3800));
    	c3.setRange(1400, 4400);
    	
    	Animation a = new Animation(3);
    	
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