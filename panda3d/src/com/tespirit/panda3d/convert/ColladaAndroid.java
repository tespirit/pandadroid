package com.tespirit.panda3d.convert;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import android.util.Xml;

import com.tespirit.panda3d.animation.Animation;
import com.tespirit.panda3d.animation.Channel;
import com.tespirit.panda3d.animation.Joint;
import com.tespirit.panda3d.animation.JointOrient;
import com.tespirit.panda3d.animation.JointRotate;
import com.tespirit.panda3d.animation.JointTranslate;
import com.tespirit.panda3d.core.Assets;
import com.tespirit.panda3d.primitives.IndexBuffer;
import com.tespirit.panda3d.primitives.Primitive;
import com.tespirit.panda3d.primitives.TriangleIndices;
import com.tespirit.panda3d.primitives.VertexBuffer;
import com.tespirit.panda3d.render.Camera;
import com.tespirit.panda3d.render.LightGroup;
import com.tespirit.panda3d.scenegraph.Group;
import com.tespirit.panda3d.scenegraph.Model;
import com.tespirit.panda3d.scenegraph.Node;
import com.tespirit.panda3d.surfaces.Color;
import com.tespirit.panda3d.surfaces.Surface;
import com.tespirit.panda3d.surfaces.Texture;
import com.tespirit.panda3d.vectors.Matrix3d;
import com.tespirit.panda3d.vectors.Vector3d;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;


/**
 * This is for loading collada files.
 * Right now there are a few things that the collada file must have:
 * 1. triangulated geometry (polygon and triangles are supported, 
 * though the polygon has to be a triangle).
 * 2. the up direction needs to be y.
 * 3. only 1 shader group is supported.
 * 4. node transformations must be represented as a matrix in order to import.
 * 
 * all data is scaled to be in meters (makes physics and all that easier) 
 * so as long as you model with correct units and the exporter exports unit 
 * information, your models should all be scaled correctly.
 * 
 * futute plans will get better support for different input types.
 * @author Todd Espiritu Santo
 *
 */
public class ColladaAndroid {
	private Node mRoot;
	private LightGroup mLights;
	private Camera[] mCameras;
	private Animation mAnimation;
	
	private float mScale;
	
	private boolean mImportNormals;
	
	private enum NameId{
		assets,
		unit,
		meter,
		
		scene,
		instance_visual_scene,
		
		library_animations,
		library_materials,
		library_effects,
		library_geometries,
		library_controllers,
		library_visual_scenes,
		
		animation,
		sampler,
		channel,
		
		input,
		offset,
		semantic,
		VERTEX,
		INPUT,
		OUTPUT,
		POSITION,
		NORMAL,
		COLOR,
		TEXCOORD,
		source,
		float_array,
		
		id,
		sid,
		url,
		target,
		
		visual_scene,
		node,
		type,
		JOINT,
		matrix,
		translate,
		rotate,
		scale,
		
		instance_geometry,
		geometry,
		mesh,
		vertices,
		polygons,
		triangles,
		count,
		p,
		
		bind_material,
		technique_common,
		instance_material,
		instance_effect,
		profile_COMMON,
		technique,
		diffuse,
		texture,
		init_from,
		color,	
		
		error
	};
	
	XmlPullParser mParser;
	
	private long mMinAnimationTime;
	private long mMaxAnimationTime;
	
	private Hashtable<String, Source> mSources;
	private Hashtable<String, Sampler> mSamplers;
	private Hashtable<String, Channel> mChannels;
	private ArrayList<ModelLink> mModels;
	private ArrayList<String> mChannelOrder;
	private Hashtable<String, Primitive> mPrimitives;
	private Hashtable<String, Surface> mSurfaces;
	
	private class ModelLink{
		Model mModel;
		String mSurfaceId;
		String mPrimitiveId;
	}
	
	private class Source{
		private String mId;
		private float[] mFloatSource;
		
		public Source(String id){
			this.mId = id;
		}
	}
	
	private class Sampler{
		private String mId;
		private String mInputSource;
		private String mOutputSource;
		
		public Sampler(String id){
			this.mId = id;
		}
	}
	
	/**
	 * use this to disable normals from importing since normals are only
	 * needed if there is lighting.
	 * @param fileName
	 * @param normals
	 * @throws Exception
	 */
	public ColladaAndroid(String fileName, boolean normals)throws Exception{
		this.mImportNormals = normals;
		this.init(Assets.getManager().openStream(fileName));
	}
	
	public ColladaAndroid(String fileName)throws Exception{
		this.mImportNormals = true;
		this.init(Assets.getManager().openStream(fileName));
	}
	
	private NameId getNameId(String name){
		try{
			return NameId.valueOf(mParser.getName());
		} catch(Exception e){
			return NameId.error;
		}
	}
	
	private NameId getTagId(){
		return this.getNameId(this.mParser.getName());
	}
	
	private String getAttr(NameId id){
		return this.mParser.getAttributeValue(null, id.name());
	}
	
	private String getRefAttr(NameId id){
		String attr = this.getAttr(id);
		if(attr != null && attr.startsWith("#")){
			attr = attr.substring(1);
		}
		return attr;
	}
	
	private NameId getAttrId(NameId id){
		try{
			return NameId.valueOf(this.getAttr(id));
		} catch(Exception e){
			return NameId.error;
		}
	}
	
	/**
	 * away to drill into the next node of this type.
	 * @param id
	 * @throws Exception
	 */
	private boolean moveToChildNode(NameId id, NameId parentId) throws Exception{
		while(this.mParser.next() != XmlPullParser.END_DOCUMENT){
			if(this.mParser.getEventType() == XmlPullParser.START_TAG && this.getTagId() == id){
				return true;
			} else if(this.mParser.getEventType() == XmlPullParser.END_TAG && this.getTagId() == parentId){
				return false;
			}
		}
		return false;
	}
	
	private void init(InputStream stream) throws Exception{
		
		this.mSources = new Hashtable<String, Source>();
		this.mSamplers = new Hashtable<String, Sampler>();
		this.mChannels = new Hashtable<String, Channel>();
		this.mModels = new ArrayList<ModelLink>();
		this.mChannelOrder = new ArrayList<String>();
		this.mPrimitives = new Hashtable<String, Primitive>();
		this.mSurfaces = new Hashtable<String, Surface>();
		
		this.mParser = Xml.newPullParser();
		this.mParser.setInput(stream, null);

		boolean done = false;
		int eventType = this.mParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT && !done){
        	switch(eventType){
        	case XmlPullParser.START_TAG:
        		switch(this.getTagId()){
        		case assets:
        			this.parseAssets();
        		case library_visual_scenes:
        			this.parseLibraryVisualScenes();
        			break;
        		case library_animations:
        			this.parseLibraryAnimations();
        			break;
        		case library_materials:
        			this.parseLibraryMaterials();
        			break;
        		case library_effects:
        			this.parseLibraryEffects();
        			break;
        		case library_geometries:
        			this.parseLibraryGeometries();
        			break;
        		case library_controllers:
        			this.parseLibraryControllers();
        			break;
        		}
        		break;
        	}
        	eventType = this.mParser.next();
        }
        
        //assemble linked data!
        for(int i = 0; i < this.mModels.size(); i++){
        	ModelLink ml = this.mModels.get(i);
        	Primitive p = this.mPrimitives.get(ml.mPrimitiveId);
        	if(p == null){
        		throw new Exception("Model primitive data not found: "+ml.mPrimitiveId);
        	}
        	ml.mModel.setPrimative(p);
        	ml.mModel.setSurface(this.mSurfaces.get(ml.mSurfaceId));
        }
        
        if(this.mChannelOrder.size() > 0 && this.mMaxAnimationTime >= this.mMinAnimationTime){
        	this.mAnimation = new Animation(this.mChannelOrder.size());
        	for(int i = 0; i < this.mChannelOrder.size(); i++){
        		Channel c = this.mChannels.get(this.mChannelOrder.get(i));
        		if(c == null){
        			c = new Channel();
        		}
        		c.setRange(this.mMinAnimationTime, this.mMaxAnimationTime);
        		this.mAnimation.addChannel(c);
        	}
        }
	}
	
	private void parseAssets() throws Exception{
		if(this.moveToChildNode(NameId.unit, NameId.assets)){
			String meterScale = this.getAttr(NameId.meter);
			if(meterScale != null && meterScale.length() > 0){
				this.mScale = Float.parseFloat(meterScale);
			}
		}
	}
	
	private void parseLibraryAnimations() throws Exception{
		this.mMaxAnimationTime = Long.MIN_VALUE;
		this.mMinAnimationTime = Long.MAX_VALUE;
		while(this.moveToChildNode(NameId.animation, NameId.library_animations)){
			this.parseAnimation();
		}
	}
	
	private void parseAnimation() throws Exception{
		String samplerId = null;
		String targetId = null;
		
		int eventType = this.mParser.next();
		while (eventType != XmlPullParser.END_DOCUMENT){
        	switch(eventType){
        	case XmlPullParser.START_TAG:
        		switch(this.getTagId()){
        		case animation:
        			this.parseAnimation();
        			break;
        		case channel:
        			samplerId = this.getRefAttr(NameId.source);
        			targetId = this.getAttr(NameId.target);
        			break;
        		case source:
        			this.parseSource();
        			break;
        		case sampler:
        			Sampler sampler = new Sampler(this.getAttr(NameId.id));
        			while(this.moveToChildNode(NameId.input, NameId.sampler)){
        				switch(this.getAttrId(NameId.semantic)){
            			case INPUT:
            				sampler.mInputSource = this.getAttr(NameId.source);
            				break;
            			case OUTPUT:
            				sampler.mOutputSource = this.getAttr(NameId.source);
            				break;
            			}
        			}
        			this.mSamplers.put(sampler.mId, sampler);
        			break;
        		}
        		break;
        	case XmlPullParser.END_TAG:
        		if(this.getTagId() == NameId.animation){
        			this.createChannels(samplerId, targetId);
        			return;
        		}
        		break;
        	}
        	eventType = this.mParser.next();
		}
	}
	
	private void createChannels(String samplerId, String targetId){
		if(samplerId == null || targetId == null){
			return;
		}
		String[] targetIds;
		//parse target ids
		if(targetId.endsWith("XYZ")){
			targetId = targetId.substring(0, targetId.length()-4);
			targetIds = new String[3];
			targetIds[0] = targetId + "X";
			targetIds[1] = targetId + "Y";
			targetIds[2] = targetId + "Z";
		} else {
			targetIds = new String[1];
			targetIds[0] = targetId;
		}
		
		Sampler sampler = this.mSamplers.get(samplerId);
		float[] input = this.mSources.get(sampler.mInputSource).mFloatSource;
		float[] output = this.mSources.get(sampler.mOutputSource).mFloatSource;
		
		if(targetId.indexOf(NameId.translate.toString())!= -1){
			//scale the output!
			for(int i = 0; i < output.length; i++){
				output[i] *= this.mScale;
			}
		}
		
		for(int i = 0; i < targetIds.length; i++){
			Channel channel = new Channel();
			for(int j  = 0; j < input.length; j++){
				channel.addKeyFrame(new Channel.KeyFrame(output[j*targetIds.length+i], (long)(input[j]*1000)));
			}
			this.mChannels.put(targetIds[i], channel);
		}
		
		long startTime = (long)(input[0]*1000);
		long endTime = (long)(input[input.length-1]*1000);
		if(startTime < this.mMinAnimationTime){
			this.mMinAnimationTime = startTime;
		}
		if(endTime > this.mMaxAnimationTime){
			this.mMaxAnimationTime = endTime;
		}
		
	}
	
	private void parseSource() throws Exception{
		if(this.moveToChildNode(NameId.float_array, NameId.source)){
			String id = this.getAttr(NameId.id);
			Source source = new Source(id);
			source.mFloatSource = this.parseFloatArray();
			this.mSources.put(source.mId, source);
		}
	}
	
	private String[] parseStringArray(NameId id) throws Exception{
		int eventType = this.mParser.next();
		String[] values = null;
		while (eventType != XmlPullParser.END_DOCUMENT){
			switch(eventType){
			case XmlPullParser.TEXT:
				values = this.mParser.getText().trim().split("[ \t\n\r]+");
				break;
			case XmlPullParser.END_TAG:
				switch(this.getTagId()){
        		case id:
        			return values;
        		}
				break;
			}
		}
		return null;
	}
	
	private float[] parseFloatArray() throws Exception{
		return parseFloatArray(NameId.float_array);
	}
	
	private float[] parseFloatArray(NameId id) throws Exception{
		String[] strings = this.parseStringArray(id);
		if(strings != null){
			float[] values = new float[strings.length];
			for(int i = 0; i<strings.length; i++){
				values[i] = Float.parseFloat(strings[i]);
			}
			return values;
		}
		return null;
	}
		
	private void parseLibraryVisualScenes() throws Exception{
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		while(this.moveToChildNode(NameId.visual_scene, NameId.library_visual_scenes)){
			Node node = this.parseNode();
			if(node != null){
				nodes.add(node);
			}
		}
		if(nodes.size() > 1){
			Group group = new Group();
			for(Node node : nodes){
				group.appendChild(node);
			}
			this.mRoot = group;
		} else if(nodes.size() == 1){
			this.mRoot = nodes.get(0);
		}
	}
	
	private Node parseNode() throws Exception{
		String name = this.getAttr(NameId.id);
		Matrix3d transform = new Matrix3d();
		Group group = null;
		Model model = null;
		Joint joint = null;
		Node retVal = null;
		
		int eventType = this.mParser.next();
		while (eventType != XmlPullParser.END_DOCUMENT){
			switch(eventType){
			case XmlPullParser.START_TAG:
				switch(this.getTagId()){
				case matrix:
					this.parseMatrix(transform);
					break;
				case translate:
					this.parseTranslate(transform);
					break;
				case rotate:
					this.parseRotate(transform);
					break;
				case scale:
					this.parseScale(transform);
					break;
				case instance_geometry:
					model = new Model(name);
					ModelLink modelLink = new ModelLink();
					modelLink.mModel = model;
					modelLink.mPrimitiveId = this.getRefAttr(NameId.url);
					if(this.moveToChildNode(NameId.instance_material, NameId.instance_geometry)){
						modelLink.mSurfaceId = this.getRefAttr(NameId.target);
					}
					this.mModels.add(modelLink);
					break;
				case node:
					if(this.getAttrId(NameId.type) == NameId.JOINT){
						joint = this.parseSkeleton();
					} else {
						Node child = parseNode();
						if(child != null){
							if(group == null){
								group = new Group(name);
								retVal = group;
							}
							group.appendChild(child);
						}
					}
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				switch(this.getTagId()){
				case node:
					if((model != null || joint != null) && group != null){
						group.appendChild(joint);
						group.appendChild(model);
					} else if(model != null){
						retVal = model;
					} else if(joint != null){
						retVal = joint;
					}
					
					if(retVal != null){
						retVal.getTransform().copy(transform);
					}
					return retVal;
				}
			}
		}
		
		return null;
	}
	
	Matrix3d temp = new Matrix3d();
	Vector3d tempVector = new Vector3d();
	
	private void parseMatrix(Matrix3d current) throws Exception{
		float[] vals = this.parseFloatArray(NameId.matrix);
		for(int row = 0; row < Matrix3d.SIZEROW; row++){
			for(int col = 0; col < Matrix3d.SIZEROW; col++){
				current.setValue(vals[col+Matrix3d.SIZEROW*row], row, col);
			}
		}
	}
	
	private void parseTranslate(Matrix3d current) throws Exception{
		float[] vals = this.parseFloatArray(NameId.translate);
		temp.copy(current);
		current.identity().translate(vals[0], vals[1], vals[2]);
		current.getTranslation().scale(this.mScale);
		current.multiply(temp);
	}
	
	private void parseRotate(Matrix3d current) throws Exception{
		float[] vals = this.parseFloatArray(NameId.rotate);
		temp.copy(current);
		tempVector.set(vals[0], vals[1], vals[2]);
		current.identity().rotateAxis(vals[3], tempVector);
		current.multiply(temp);
	}
	
	private void parseScale(Matrix3d current) throws Exception{
		float[] vals = this.parseFloatArray(NameId.scale);
		temp.copy(current);
		current.identity().scale(vals[0], vals[1], vals[2]);
		current.multiply(temp);
	}
	
	private Joint parseSkeleton() throws Exception{
		String name = this.getAttr(NameId.id);
		this.generateChannels(name, false, true, true, true); //translate
		this.generateChannels(name, true, true, true, true); //rotate
		
		JointTranslate joint = new JointTranslate(name);
		JointRotate subJoint = new JointRotate();
		joint.appendChild(subJoint);
		
		int eventType = this.mParser.next();
		while (eventType != XmlPullParser.END_DOCUMENT){
			switch(eventType){
			case XmlPullParser.START_TAG:
				switch(this.getTagId()){
				case matrix:
					this.parseMatrix(joint.getTransform());
					break;
				case translate:
					this.parseTranslate(joint.getTransform());
					break;
				case rotate:
					String sid = this.getAttr(NameId.sid);
					if(sid != null && sid.startsWith("jointOrient")){
						this.parseRotate(joint.getTransform());
					} else {
						this.parseRotate(subJoint.getTransform());
					}
					break;
				case node:
					if(this.getAttrId(NameId.type) == NameId.JOINT){
						Joint child = this.parseJoint();
						subJoint.appendChild(child);
					}
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == NameId.node){
					joint.createAllBones(0.05f);
					return joint;
				}
				break;
			}
		}
		return null;
	}
	
	private void generateChannels(String name, boolean rotate, boolean x, boolean y, boolean z){
		String prefix;
		String postfix;
		if(rotate){
			name = name +"/rotate";
			prefix = "";
			postfix = ".ANGLE";
		} else {
			name = name + "/translate";
			prefix = ".";
			postfix = "";
		}
		
		if(x){
			this.mChannelOrder.add(name+prefix+"X"+postfix);
		}
		if(y){
			this.mChannelOrder.add(name+prefix+"Y"+postfix);
		}
		if(z){
			this.mChannelOrder.add(name+prefix+"Z"+postfix);
		}
	}
	
	private Joint parseJoint() throws Exception{
		String name = this.getAttr(NameId.id);
		this.generateChannels(name, true, true, true, true); //rotate
		
		Joint joint = new JointRotate(name);
		JointOrient jointO = new JointOrient(name+"<orient>");
		
		int eventType = this.mParser.next();
		while (eventType != XmlPullParser.END_DOCUMENT){
			switch(eventType){
			case XmlPullParser.START_TAG:
				switch(this.getTagId()){
				case matrix:
					this.parseMatrix(jointO.getTransform());
					break;
				case translate:
					this.parseTranslate(jointO.getTransform());
					break;
				case rotate:
					String sid = this.getAttr(NameId.sid);
					if(sid != null && sid.startsWith("jointOrient")){
						this.parseRotate(jointO.getTransform());
					} else {
						this.parseRotate(joint.getTransform());
					}
					break;
				case node:
					if(this.getAttrId(NameId.type) == NameId.JOINT){
						Joint child = this.parseJoint();
						joint.appendChild(child);
					}
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == NameId.node){
					if(jointO.getTransform().isIdentity3x3()){
						joint.getTransform().multiply(jointO.getTransform());
					} else {
						jointO.appendChild(joint);
						joint = jointO;
					}
					return joint;
				}
				break;
			}
		}
		return null;
	}
	
	private void parseLibraryGeometries() throws Exception{
		while(this.moveToChildNode(NameId.geometry, NameId.library_geometries)){
			String name = this.getAttr(NameId.id);
			Inputs inputs = new Inputs();
			Vector<Integer> indices = new Vector<Integer>();
			
			if(this.moveToChildNode(NameId.mesh, NameId.geometry)){
				int eventType = this.mParser.getEventType();
				while(eventType != XmlPullParser.END_DOCUMENT){
					eventType = this.mParser.next();
					switch(eventType){
					case XmlPullParser.START_TAG:
						switch(this.getTagId()){
						case source:
							this.parseSource();
							break;
						case vertices:
							this.parseInputs(inputs, NameId.vertices);
							break;
						case polygons:
							this.parseInputs(inputs, NameId.polygons);
							this.parseP(indices, NameId.polygons);
							break;
						case triangles:
							this.parseInputs(inputs, NameId.triangles);
							this.parseP(indices, NameId.triangles);
							break;
						}
						break;
					case XmlPullParser.END_TAG:
						if(this.getTagId() == NameId.geometry){
							this.createPrimitive(name, inputs, indices);
						}
						break;
					}
				}
			}
		}
	}
	
	private void createPrimitive(String name, Inputs inputs, Vector<Integer> indices){
		float[] position = null;
		float[] normal = null;
		float[] texcoord = null;
		float[] color = null;
		
		Vector<Float> positionRemap = null;
		Vector<Float> normalRemap = null;
		Vector<Float> texcoordRemap = null;
		Vector<Float> colorRemap = null;
		
		ArrayList<Integer> types = new ArrayList<Integer>();
		
		Hashtable<String, Integer> remap = new Hashtable<String, Integer>();
		
		int count = 0;
		
		if(inputs.mPosition != null){
			position =  this.mSources.get(inputs.mPosition).mFloatSource;
			types.add(VertexBuffer.POSITION);
			positionRemap = new Vector<Float>();
			if(inputs.mPositionOffset != -1) count++;
		}
		if(inputs.mNormal != null && this.mImportNormals){
			normal = this.mSources.get(inputs.mNormal).mFloatSource;
			types.add(VertexBuffer.NORMAL);
			normalRemap = new Vector<Float>();
			if(inputs.mNormalOffset != -1) count++;
		}
		if(inputs.mTexcoord != null){
			texcoord = this.mSources.get(inputs.mTexcoord).mFloatSource;
			types.add(VertexBuffer.TEXCOORD);
			texcoordRemap = new Vector<Float>();
			if(inputs.mTexcoordOffset != -1) count++;
		}
		if(inputs.mColor != null){
			color = this.mSources.get(inputs.mColor).mFloatSource;
			types.add(VertexBuffer.COLOR);
			colorRemap = new Vector<Float>();
			if(inputs.mColorOffset != -1) count++;
		}
		Vector<Integer> indicesRemap = new Vector<Integer>();
		
		int nextIndex = 0;
		for(int i = 0; i < indices.size()/count; i++){
			String id = "";
			for(int j = 0; j < count; i++){
				id += indices.get(i*count+j)+",";
			}
			if(remap.containsKey(id)){
				indicesRemap.add(remap.get(id));
				continue;
			}
			
			remap.put(id, nextIndex);
			nextIndex++;
			
			int index;
			if(position != null){
				index = indices.get(i*count+inputs.mPositionOffset)*3;
				positionRemap.add(position[index]);
				positionRemap.add(position[index+1]);
				positionRemap.add(position[index+2]);
			}
			if(normal != null){
				index = indices.get(i*count+inputs.mNormalOffset)*3;
				normalRemap.add(normal[index]);
				normalRemap.add(normal[index+1]);
				normalRemap.add(normal[index+2]);
			}
			if(texcoord != null){
				index = indices.get(i*count+inputs.mTexcoordOffset)*2;
				texcoordRemap.add(texcoord[index]);
				texcoordRemap.add(texcoord[index+1]);
			}
			if(color != null){
				index = indices.get(i*count+inputs.mColorOffset)*4;
				colorRemap.add(color[index]);
				colorRemap.add(color[index+1]);
				colorRemap.add(color[index+2]);
				colorRemap.add(color[index+3]);
			}
		}
		
		//finally, assemble the primitive!
		int[] typesArray = new int[types.size()];
		for(int i = 0; i < types.size(); i++){
			typesArray[i] = types.get(i);
		}
		TriangleIndices mesh = new TriangleIndices(indicesRemap.size(), nextIndex, typesArray);
		VertexBuffer vb = mesh.getVertexBuffer();
		for(int i = 0; i < nextIndex; i++){
			if(positionRemap != null){
				vb.addPosition(positionRemap.get(i*3),
							   positionRemap.get(i*3+1),
							   positionRemap.get(i*3+2));
			}
			if(normalRemap != null){
				vb.addNormal(normalRemap.get(i*3),
							 normalRemap.get(i*3+1),
							 normalRemap.get(i*3+2));
			}
			if(texcoordRemap != null){
				vb.addTexcoord(texcoordRemap.get(i*2),
							   texcoordRemap.get(i*2+1));
			}
			if(colorRemap != null){
				vb.addColor(colorRemap.get(i*4),
							colorRemap.get(i*4+1),
							colorRemap.get(i*4+2),
							colorRemap.get(i*4+4));
			}
		}
		vb.resetBufferPosition();
		
		IndexBuffer ib = mesh.getIndexBuffer();
		for(int i = 0; i < indicesRemap.size(); i++){
			ib.addTriangle(indicesRemap.get(i++),
						   indicesRemap.get(i++),
						   indicesRemap.get(i++));
		}
		ib.resetBufferPosition();
		
		this.mPrimitives.put(name, mesh);
		
	}
	
	private void parseP(Vector<Integer> indices, NameId parentId) throws Exception{
		while(this.moveToChildNode(NameId.p, parentId)){
			String[] values = this.parseStringArray(NameId.p);
			for(int i = 0; i < values.length; i++){
				indices.add(Integer.parseInt(values[i]));
			}
		}
	}
	
	private class Inputs{
		String mPosition;
		int mPositionOffset = -1;
		String mNormal;
		int mNormalOffset = -1;
		String mTexcoord;
		int mTexcoordOffset = -1;
		String mColor;
		int mColorOffset = -1;
	}
	
	private void parseInputs(Inputs inputs, NameId parentId)throws Exception{
		while(this.moveToChildNode(NameId.input, parentId)){
			String offsetString = this.getAttr(NameId.offset);
			int offset = -1;
			if(offsetString != null){
				offset = Integer.parseInt(offsetString);
			}
			switch(this.getAttrId(NameId.semantic)){
			case POSITION:
				inputs.mPosition = this.getRefAttr(NameId.source);
				if(offset != -1){
					inputs.mPositionOffset = offset;
				}
				break;
			case NORMAL:
				inputs.mNormal = this.getRefAttr(NameId.source);
				if(offset != -1){
					inputs.mNormalOffset = offset;
				}
				break;
			case TEXCOORD:
				inputs.mTexcoord = this.getRefAttr(NameId.source);
				if(offset != -1){
					inputs.mTexcoordOffset = offset;
				}
				break;
			case COLOR:
				inputs.mColor = this.getRefAttr(NameId.source);
				if(offset != -1){
					inputs.mColorOffset = offset;
				}
				break;
			case VERTEX:
				if(inputs.mPositionOffset == -1){
					inputs.mPositionOffset = offset;
				}
				if(inputs.mNormalOffset == -1){
					inputs.mNormalOffset = offset;
				}
				if(inputs.mTexcoordOffset == -1){
					inputs.mTexcoordOffset = offset;
				}
				if(inputs.mColorOffset == -1){
					inputs.mColorOffset = offset;
				}
				break;
			}
		}
	}
	
	private void parseLibraryMaterials(){
		
	}
	
	private void parseLibraryEffects(){
		
	}
	
	private void parseLibraryControllers(){
		
	}
/*
	
	private Surface convertMaterial(org.w3c.dom.Node node){
		node = this.getChildNodeByType("bind_material", node);
		node = this.getChildNodeByType("technique_common", node);
		node = this.getChildNodeByType("instance_material", node);
		
		String id = this.getAttribute("target", node);
		
		Surface s = this.surfaces.get(id);
		if(s != null){
			return s;
		}
		
		node = this.getNodeByUrl(id);
		node = this.getChildNodeByType("instance_effect", node);
		String url = this.getAttribute("url", node);
		node = this.getNodeByUrl(url);
		node = this.getChildNodeByType("profile_COMMON", node);
		node = this.getChildNodeByType("technique", node);
		NodeList children = node.getChildNodes();
		
		//get first non text node
		for(int i = 0; i < children.getLength(); i++){
			node = children.item(i);
			if(node.getNodeType() != org.w3c.dom.Node.TEXT_NODE){
				break;
			}
		}
		
		//for now, only diffuse is imported.
		node = this.getChildNodeByType("diffuse", node);
		
		org.w3c.dom.Node texture = this.getChildNodeByType("texture", node);
		if(texture != null){
			url = this.getAttribute("texture", texture);
			texture = this.getNodeByUrl(url);
			
			texture = this.getChildNodeByType("init_from", texture);
			String textureName = texture.getFirstChild().getNodeValue().trim();
			textureName = textureName.replace('\\', '/');
			int index = textureName.lastIndexOf('/');
			if(index != -1){
				textureName = textureName.substring(index+1);
			}
			
			Texture t = new Texture();
			t.setDiffuseTextureName(textureName);
			s = t;
		} else {
			org.w3c.dom.Node color = this.getChildNodeByType("color", node);
			if(color != null){
				float[] values = this.convertToFloatArray(color);
				Color c = new Color();
				c.setColor(values[0], values[1], values[2], values[3]);
				s = c;
			} else {
				s = Surface.getDefaultSurface();
			}
		}
		
		this.surfaces.put(id, s);
		
		return s;
	}
	
	private Primitive convertGeometry(org.w3c.dom.Node node){
		String url = this.getAttribute("url", node);
		if(url != null){
			//check model cache!
			Primitive p = this.primitives.get(url);
			if(p != null){
				return p;
			}
			node = this.getNodeByUrl(url);
			if(node == null || !node.getNodeName().equalsIgnoreCase("geometry")){
				return null;
			}
			node = this.getChildNodeByType("mesh", node);
			if(node != null){
				org.w3c.dom.Node indices = this.getChildNodeByType("polygons", node);
				if(indices != null){
					p = this.convertPolygon(indices);
				} else {
					indices = this.getChildNodeByType("triangles", node);
					if(indices != null){
						p = convertTriangles(indices);
					}
				}
			}
			
			this.primitives.put(url, p);
			
			return p;
		}
		return null;
	}
	
	private Primitive convertTriangles(org.w3c.dom.Node triangles){
		ColladaVb vb = this.convertVertexBuffer(triangles);
		int count = Integer.parseInt(this.getAttribute("count", triangles));
		IndexBuffer ib = new IndexBuffer(count * 3);
		org.w3c.dom.Node p = this.getChildNodeByType("p", triangles);
		int[] values = this.convertToIntArray(p);
		int stride = values.length/count;
		for(int i = 0; i < count; i++){
			ib.addTriangle(vb.registerVertex(values, i*3),
					   	   vb.registerVertex(values, i*3+stride),
					       vb.registerVertex(values, i*3+stride*2));
		}
		ib.resetBufferPosition();
		
		TriangleIndices t = new TriangleIndices(vb.createVb(), ib);
		return t;
	}
	
	private Primitive convertPolygon(org.w3c.dom.Node polygon){
		ColladaVb vb = this.convertVertexBuffer(polygon);
		int count = Integer.parseInt(this.getAttribute("count", polygon));
		IndexBuffer ib = new IndexBuffer(count * 3); //assume triangles!
		NodeList polygons = polygon.getChildNodes();
		for(int i = 0; i < polygons.getLength(); i++){
			org.w3c.dom.Node p = polygons.item(i);
			if(p.getNodeName().equalsIgnoreCase("p")){
				int[] values = this.convertToIntArray(p);
				int stride = values.length/3;
				ib.addTriangle(vb.registerVertex(values, 0),
							   vb.registerVertex(values, stride),
							   vb.registerVertex(values, stride*2));
			}
		}
		ib.resetBufferPosition();
			
		TriangleIndices t = new TriangleIndices(vb.createVb(), ib);
		return t;
	}
	
	
	
	private void convertInput(org.w3c.dom.Node input, ColladaVb vb, int currentOffset){
		String type = input.getNodeName().toLowerCase();
		
		if(!type.equals("input")){
			return;
		}
		
		String semantic = this.getAttribute("semantic", input);
		String offsetStr = this.getAttribute("offset", input);
		int offset;
		
		if(offsetStr == null){
			offset = currentOffset;
		} else {
			offset = Integer.parseInt(this.getAttribute("offset", input));
		}
		
		input = this.getNodeByUrl(this.getAttribute("source", input));
		
		if(semantic.equals("VERTEX")){
			for(int i = 0; i < input.getChildNodes().getLength(); i++){
				this.convertInput(input.getChildNodes().item(i), vb, offset);
			}
		} else if(semantic.equals("POSITION")){
			input = this.getChildNodeByType("float_array", input);
			vb.setPosition(this.convertToFloatArray(input), offset);
		} else if(semantic.equals("NORMAL")){
			input = this.getChildNodeByType("float_array", input);
			vb.setNormal(this.convertToFloatArray(input), offset);
		} else if(semantic.equals("TEXCOORD")){
			input = this.getChildNodeByType("float_array", input);
			vb.setTexcoord(this.convertToFloatArray(input), offset);
		} else if(semantic.equals("COLOR")){
			input = this.getChildNodeByType("float_array", input);
			vb.setColor(this.convertToFloatArray(input), offset);
		}
	}
	
	private ColladaVb convertVertexBuffer(org.w3c.dom.Node source){
		ColladaVb vb = new ColladaVb();
		
		NodeList inputs = source.getChildNodes();
		for(int i = 0; i < inputs.getLength() && vb.count < ColladaVb.MAX; i++){
			org.w3c.dom.Node input = inputs.item(i);
			this.convertInput(input, vb, 0);
		}
		
		vb.generateTypes();
		return vb;
	}
	
	
	private org.w3c.dom.Node getNodeByUrl(String url){
		if(url == null) return null;
		if(url.startsWith("#")){
			url = url.substring(1);
		}
		try{
			XPathExpression expr = this.xpath.compile("//*[@id='"+url+"']");
			return (org.w3c.dom.Node)expr.evaluate(this.document, XPathConstants.NODE);
		} catch(Exception e){
			return null;
		}
	}
	
	private org.w3c.dom.Node getChildNodeByType(String type, org.w3c.dom.Node node){
		for(int i = 0; i < node.getChildNodes().getLength(); i++){
			org.w3c.dom.Node child = node.getChildNodes().item(i);
			if(child.getNodeName().equalsIgnoreCase(type)){
				return child;
			}
		}
		return null;
	}*/
	
	public Node getSceneGraph(){
		return this.mRoot;
	}
	
	public LightGroup getLightGroup(){
		return this.mLights;
	}
	
	public Camera[] getCameras(){
		return this.mCameras;
	}
	
	public Animation getAnimation(){
		return this.mAnimation;
	}
}
