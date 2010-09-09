package com.tespirit.panda3d;

import android.app.Activity;
import android.os.Bundle;

import com.tespirit.panda3d.material.Texture;
import com.tespirit.panda3d.render.*;
import com.tespirit.panda3d.scenegraph.*;
import com.tespirit.panda3d.geometry.*;

import android.opengl.GLSurfaceView;
import android.view.*;

public class Pandamonium extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
    	GLSurfaceView view = new GLSurfaceView(this);
    	Renderer r = new Renderer(this);
    	
    	Camera camera = new Camera();
    	camera.getTransform().rotateX(10);
    	camera.getTransform().translate(0, -2, -10);
    	r.setCamera(camera);
    	
    	LightManager lights = new LightManager();
    	lights.createSimple();
    	r.setLightManager(lights);
    	
    	r.setSceneGraph(createTestSG());
    	
   		view.setRenderer(r);
   		setContentView(view);
    }
    
    public Node createTestSG(){
    	Group g = new Group();
    	
    	Texture t1 = new Texture();
    	t1.setTexture("test.bmp");
    	
    	Model m1 = new Model();
    	m1.setGeometry(new Plane());
    	m1.setMaterial(t1);

    	//adjust matrices
    	m1.getTransform().rotateY(45.0f);
    	
    	g.appendChild(m1);
    	
    	Model m2 = new Model();
    	m2.setGeometry(new Box());
    	
    	//adjust matrix
    	m2.getTransform().translate(-3.0f, 0.0f, 0.0f);
    	m2.getTransform().scale(0.5f);
    	
    	g.appendChild(m2);
    	
    	g.getTransform().translate(1.0f, 0.0f, 0.0f);
    	g.getTransform().rotateX(20.0f);
    	g.getTransform().rotateY(-20.0f);
    	
    	
    	return g;
    }
}