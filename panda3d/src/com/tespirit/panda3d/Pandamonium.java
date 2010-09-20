package com.tespirit.panda3d;

import android.app.Activity;
import android.os.Bundle;

import com.tespirit.panda3d.surfaces.Material;
import com.tespirit.panda3d.surfaces.Texture;
import com.tespirit.panda3d.render.LightGroup;
import com.tespirit.panda3d.scenegraph.*;

import com.tespirit.panda3d.app.Debug;
import com.tespirit.panda3d.app.MatrixSelect;
import com.tespirit.panda3d.app.Panda3dView;
import com.tespirit.panda3d.app.TranslateAbsolute;
import com.tespirit.panda3d.convert.Collada;
import com.tespirit.panda3d.render.Renderer;
import com.tespirit.panda3d.primitives.Box;
import com.tespirit.panda3d.primitives.Plane;

import android.view.*;

public class Pandamonium extends Activity {
	
	boolean debug = true;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
    	Panda3dView view = new Panda3dView(this, this.debug);

    	view.createTouchRotateCamera(-2);
    	
    	//TranslateAbsolute trans = new TranslateAbsolute(view);
    	
    	view.setTouchDownController(new MatrixSelect(view, true));//trans.createSelectController());
    	
    	
    	Renderer r = view.getRenderer();
    	
    	LightGroup lights = new LightGroup();
    	lights.createBasic();
    	r.setLightGroup(lights);
    	
    	try{
    		
    		r.setSceneGraph(this.loadCollada());
    		
    	} catch(Exception e){
    		r.setSceneGraph(this.createTestSG());
    	}

   		setContentView(view);
   		view.setFocusableInTouchMode(true);
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
		
		return g;
    }
    
    public Node createTestSG(){
    	Group g = new Group();
    	
    	Texture t1 = new Texture();
    	t1.setDiffuseTextureName("test.bmp");
    	
    	Material mat1 = new Material();
    	mat1.setAmbient(0.8f, 0.3f, 0.5f);
    	mat1.setDiffuse(0.8f, 0.3f, 0.5f);
    	
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