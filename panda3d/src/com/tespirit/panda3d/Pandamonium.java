package com.tespirit.panda3d;

import android.app.Activity;
import android.os.Bundle;

import com.tespirit.panda3d.surfaces.Texture;
import com.tespirit.panda3d.render.Camera;
import com.tespirit.panda3d.render.LightGroup;
import com.tespirit.panda3d.scenegraph.*;
import com.tespirit.panda3d.app.Panda3dView;
import com.tespirit.panda3d.render.Renderer;
import com.tespirit.panda3d.primitives.Box;
import com.tespirit.panda3d.primitives.Plane;

import android.view.*;

public class Pandamonium extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
    	Panda3dView view = new Panda3dView(this);
    	
    	Renderer r = view.getRenderer();
    	
    	Camera camera = new Camera();
    	camera.getPivot().rotateX(10);
    	camera.zoom(-10);
    	r.setCamera(camera);
    	
    	LightGroup lights = new LightGroup();
    	lights.createBasic();
    	r.setLightGroup(lights);
    	
    	r.setSceneGraph(createTestSG());
    	
   		setContentView(view);
    }
    
    public Node createTestSG(){
    	Group g = new Group();
    	
    	Texture t1 = new Texture();
    	t1.setDiffuseTextureName("test.bmp");
    	
    	Model m1 = new Model();
    	m1.setPrimative(new Plane());
    	m1.setSurface(t1);

    	//adjust matrices
    	m1.getTransform().rotateY(45.0f);
    	
    	g.appendChild(m1);

    	Model m2 = new Model();
    	m2.setPrimative(new Box());
    	
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