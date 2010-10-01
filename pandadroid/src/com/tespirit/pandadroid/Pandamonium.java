package com.tespirit.pandadroid;

import android.app.Activity;
import android.os.Bundle;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Channel;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.animation.JointRotate;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamboo.modifiers.SkinModifier;
import com.tespirit.bamboo.primitives.Box;
import com.tespirit.bamboo.primitives.IndexBuffer;
import com.tespirit.bamboo.primitives.Plane;
import com.tespirit.bamboo.primitives.TriangleIndices;
import com.tespirit.bamboo.primitives.VertexBuffer;
import com.tespirit.bamboo.render.Camera;
import com.tespirit.bamboo.render.LightGroup;
import com.tespirit.bamboo.scenegraph.Group;
import com.tespirit.bamboo.scenegraph.Model;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamboo.surfaces.Texture;
import com.tespirit.bamboo.vectors.Matrix3d;

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
    		BambooAsset b = view.loadBamboo("candy.bam");
    		setupAnimation(b, "d_root1");
    	} catch(Exception e){
    		Debug.print(e);
    		view.setSceneGraph(this.createTestSG());
    	}
    }
    
    public void setupAnimation(BambooAsset b, String skeletonName){
		Debug.addTestAnimation(b.getAnimation());
		b.getAnimation().attachSkeleton((Joint)Node.getNode(skeletonName));
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
    
    private BambooAsset createDemoSkin(){
		TriangleIndices mesh = new TriangleIndices(18, 8, new int[]{VertexBuffer.POSITION,VertexBuffer.NORMAL});
		
		VertexBuffer vb = mesh.getVertexBuffer();
		
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
		vb.resetBufferPosition();
		
		IndexBuffer ib = mesh.getIndexBuffer();
		
		ib.addTriangle(0, 1, 2);
		ib.addTriangle(1, 3, 2);
		
		ib.addTriangle(2, 3, 4);
		ib.addTriangle(3, 5, 4);
		
		ib.addTriangle(4, 5, 6);
		ib.addTriangle(5, 7, 6);
		ib.resetBufferPosition();
		
		Model model = new Model();
		
		model.setPrimative(mesh);
		
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
		
		Group g = new Group();
		g.appendChild(joint1);
		g.appendChild(model);
		
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
		animation.addClip(new Clip(0,3000));
		animation.attachSkeleton(joint1);
		animation.play(0);
		
		return new SimpleAsset(g, animation);
	}
    
    class SimpleAsset implements BambooAsset{
		Animation a;
		Node s;
		
		public SimpleAsset(Node s, Animation a){
			this.s = s;
			this.a = a;
		}

		@Override
		public Animation getAnimation() {
			return a;
		}

		@Override
		public Camera[] getCameras() {
			return null;
		}

		@Override
		public LightGroup getLightGroup() {
			return null;
		}

		@Override
		public Node getSceneGraph() {
			return s;
		}
		
	}
}