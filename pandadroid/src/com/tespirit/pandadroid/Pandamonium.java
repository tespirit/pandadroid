package com.tespirit.pandadroid;

import android.app.Activity;
import android.os.Bundle;

import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamboo.primitives.Box;
import com.tespirit.bamboo.primitives.Plane;
import com.tespirit.bamboo.render.LightGroup;
import com.tespirit.bamboo.scenegraph.Group;
import com.tespirit.bamboo.scenegraph.Model;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamboo.surfaces.Texture;

import com.tespirit.pandadroid.R;
import com.tespirit.pandadroid.app.PandadroidView;
import com.tespirit.pandadroid.app.TranslateAbsolute;
import com.tespirit.pandadroid.debug.Debug;

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
    	
    	view.setTouchDownController(new TranslateAbsolute(view));
    	
    	LightGroup lights = new LightGroup();
    	lights.createBasic();
    	view.setLightGroup(lights);
    	
    	try{
    		BambooAsset b = view.loadBamboo("test_anim.bam");
    		Debug.addTestAnimation(b.getAnimation());
    		b.getAnimation().attachSkeleton((Joint)b.getSceneGraph());
    	} catch(Exception e){
    		Debug.print(e);
    		view.setSceneGraph(this.createTestSG());
    	}
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