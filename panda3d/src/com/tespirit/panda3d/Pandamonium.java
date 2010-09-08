package com.tespirit.panda3d;

import android.app.Activity;
import android.os.Bundle;

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
   		view.setRenderer(new Renderer(createTestSG()));
   		setContentView(view);
    }
    
    public Node createTestSG(){
    	Group g = new Group();
    	
    	/*Model m1 = new Model();
    	m1.setGeometry(new Plane());
    	m1.setMaterial(new Material());

    	//adjust colors
    	m1.getMaterial().setDiffuse(1.0f, 1.0f, 1.0f);
    	m1.getMaterial().setAmbient(1.0f, 1.0f, 1.0f);
    	m1.getMaterial().setEmission(1.0f, 1.0f, 1.0f);
    	
    	//adjust matrices
    	m1.getTransform().rotateY(45.0f);
    	
    	g.appendChild(m1);
    	*/
    	Model m2 = new Model();
    	m2.setGeometry(new Box());
    	
    	//adjust matrix
//    	m2.getTransform().translate(-3.0f, 0.0f, 0.0f);
//    	m2.getTransform().scale(0.5f);
    	
    	g.appendChild(m2);
    	
   // 	g.getTransform().translate(1.0f, 0.0f, 0.0f);
   // 	g.getTransform().rotateX(20.0f);
   // 	g.getTransform().rotateY(-20.0f);
    	
    	return g;
    }
}