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
    	Model m = new Model();
    	m.setGeometry(new Box());
    	return m;
    }
}