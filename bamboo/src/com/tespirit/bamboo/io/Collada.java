package com.tespirit.bamboo.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Channel;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.animation.JointOrient;
import com.tespirit.bamboo.animation.JointRotate;
import com.tespirit.bamboo.animation.JointTranslate;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.modifiers.SkinModifier;
import com.tespirit.bamboo.primitives.IndexBuffer;
import com.tespirit.bamboo.primitives.Primitive;
import com.tespirit.bamboo.primitives.VertexIndices;
import com.tespirit.bamboo.primitives.VertexBuffer;
import com.tespirit.bamboo.scenegraph.Camera;
import com.tespirit.bamboo.scenegraph.Group;
import com.tespirit.bamboo.scenegraph.Model;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamboo.surfaces.Surface;
import com.tespirit.bamboo.surfaces.Texture;
import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Util;
import com.tespirit.bamboo.vectors.Vector3d;

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
public class Collada implements BambooAsset{
	private List<Node> mSceneRoots;
	private List<Animation> mAnimations;
	private List<Camera> mCameras;
	private List<Player> mPlayers;
	
	protected ArrayList<String> mTexturePaths;
	
	private float mScale;
	
	protected boolean mImportNormals;
	protected boolean mFlipYTexCoord;
	protected String mName;
	
	private enum NameId{
		asset,
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
		library_images,
		
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
		Name_array,
		
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
		
		instance_controller,
		controller,
		skin,
		vertex_weights,
		joints,
		vcount,
		v,
		WEIGHT,
		INV_BIND_MATRIX,
		
		bind_material,
		technique_common,
		instance_material,
		material,
		instance_effect,
		effect,
		profile_COMMON,
		technique,
		diffuse,
		transparent,
		texture,
		color,	
		image,
		init_from,
		phong,
		blinn,
		lambert,
		
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
	private Hashtable<String, String> mTextureNames;
	private Hashtable<String, String> mEffectsLink;
	private Hashtable<String, Effect> mEffects;
	private Hashtable<String, Vector<Integer>> mSkinRemaps;
	private Hashtable<String, SkinData> mSkinDatas;
	
	private class ModelLink{
		Model mModel;
		String mSurfaceId;
		String mPrimitiveId;
	}
	
	private class Source{
		private String mId;
		private float[] mFloatSource;
		private String[] mNameSource;
		
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
	
	private class EffectParam{
		Color4 color;
		String textureId;
	}
	
	private class Effect {
		EffectParam diffuse;
		EffectParam transparent;
		
	}
	
	private class InvalidRefException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5048613965778781497L;
		public InvalidRefException(NameId tag, String id, String target){
			super("<"+tag.toString() + " id='" + id + "'/>: ref '" + target + "' not found.");
		}
		public InvalidRefException(NameId tag, String target){
			super("<"+tag.toString() + "'/>: ref '" + target + "' not found.");
		}
	}
	
	/**
	 * use this to disable normals from importing since normals are only
	 * needed if there is lighting.
	 * @param fileName
	 * @param normals
	 * @throws Exception
	 */
	public Collada(XmlPullParser input, String name, boolean flipYTexcoord, boolean normals)throws Exception{
		this.mAnimations = new ArrayList<Animation>();
		this.mSceneRoots = new ArrayList<Node>();
		this.mCameras = new ArrayList<Camera>();
		this.mPlayers = new ArrayList<Player>();
		this.mImportNormals = normals;
		this.mFlipYTexCoord = flipYTexcoord;
		this.init(input, name);
	}
	
	public Collada(XmlPullParser input, String name, boolean flipYTexcoord)throws Exception{
		this(input, name, flipYTexcoord, true);
	}
	
	public Collada(XmlPullParser input, String name)throws Exception{
		this(input, name, true, true);
	}
	
	private NameId getNameId(String name){
		try{
			return NameId.valueOf(name);
		} catch(Exception e){
			return NameId.error;
		}
	}
	
	private NameId getTagId(){
		String name = this.mParser.getName();
		System.out.println("<" + name + ">");
		return this.getNameId(name);
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
		int eventType = this.mParser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
			switch(eventType){
			case XmlPullParser.START_TAG:
				if(this.getTagId() == id){
					return true;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == parentId){
					return false;
				}
			}
		}
		return false;
	}
	
	private boolean moveToFirstChild() throws Exception{
		int eventType = this.mParser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
			switch(eventType){
			case XmlPullParser.START_TAG:
				return true;
			case XmlPullParser.END_TAG:
				return false;
			}
		}
		return false;
	}
	
	protected void init(XmlPullParser input, String name) throws Exception{
		
		this.mSources = new Hashtable<String, Source>();
		this.mSamplers = new Hashtable<String, Sampler>();
		this.mChannels = new Hashtable<String, Channel>();
		this.mModels = new ArrayList<ModelLink>();
		this.mChannelOrder = new ArrayList<String>();
		this.mPrimitives = new Hashtable<String, Primitive>();
		this.mTextureNames = new Hashtable<String, String>();
		this.mEffectsLink = new Hashtable<String, String>();
		this.mEffects = new Hashtable<String, Effect>();
		this.mSkinRemaps = new Hashtable<String, Vector<Integer>>();
		this.mSkinDatas = new Hashtable<String, SkinData>();
		this.mTexturePaths = new ArrayList<String>();
		
		this.mParser = input;

		this.mName = new File(name).getName();
		int ext = this.mName.lastIndexOf('.');
		if(ext != -1){
			this.mName = this.mName.substring(0, ext);
		}
		
		int eventType = this.mParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT){
        	switch(eventType){
        	case XmlPullParser.START_TAG:
        		NameId tagId = this.getTagId();
        		switch(tagId){
        		case asset:
        			this.parseAssets();
        			break;
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
        		case library_images:
        			this.parseLibraryImages();
        			break;
        		}
        		break;
        	}
        	eventType = this.mParser.next();
        }
        
        //assemble linked data!
        for(int i = 0; i < this.mModels.size(); i++){
        	ModelLink ml = this.mModels.get(i);
        	this.createSkin(ml.mPrimitiveId);
        	Primitive p = this.mPrimitives.get(ml.mPrimitiveId);
        	if(p == null){
        		throw new Exception("Model primitive data not found: "+ml.mPrimitiveId);
        	}
        	ml.mModel.setPrimitive(p);
        	ml.mModel.setSurface(this.createSurface(ml.mSurfaceId));
        }
        
        if(this.mChannelOrder.size() > 0 && this.mMaxAnimationTime >= this.mMinAnimationTime){
        	Animation animation = new Animation(this.mName, this.mChannelOrder.size());
        	for(int i = 0; i < this.mChannelOrder.size(); i++){
        		Channel c = this.mChannels.get(this.mChannelOrder.get(i));
        		if(c == null){
        			c = new Channel();
        		}
        		animation.addChannel(c);
        	}
        	animation.addClip(new Clip("default", this.mMinAnimationTime, this.mMaxAnimationTime));
        	this.mAnimations.add(animation);
        	Player player = new Player();
			player.setAnimation(animation);
			this.mPlayers.add(player);
        }
        this.mParser = null;
	}
	
	private void createSkin(String skinId){
		if(!this.mSkinDatas.containsKey(skinId)){
			return;
		}
		SkinData skinData = this.mSkinDatas.get(skinId);
		VertexIndices source = (VertexIndices)this.mPrimitives.get(skinData.mSource);
		
		SkinModifier skin = new SkinModifier();
		skin.attachRig(skinData.mJoints, skinData.mBindMatricesInv);
		
		//create weights and all!
		Vector<Integer> skinRemap = this.mSkinRemaps.get(skinData.mSource);
		
		int[] totalOffset = new int[skinData.mStrideMap.length];
		int offset = 0;
		for(int i = 0; i < totalOffset.length; i++){
			totalOffset[i] = offset;
			offset += skinData.mStrideMap[i];
		}
		
		byte[] weightStrides = new byte[source.getVertexBuffer().getCount()];
		Vector<Vector<Byte>> skeletonMapTemp = new Vector<Vector<Byte>>();
		Vector<Vector<Float>> weightsTemp = new Vector<Vector<Float>>(); 
		
		int mapCount = 0;
		
		for(int i = 0; i < skinRemap.size(); i++){
			int index = skinRemap.get(i);
			int mapOffset = totalOffset[index]*skinData.mStride;
			int stride = skinData.mStrideMap[index];
			weightStrides[i] = (byte)stride;
			Vector<Byte> skeletonVertex = new Vector<Byte>();
			Vector<Float> weightVertex = new Vector<Float>();
			skeletonMapTemp.add(skeletonVertex);
			weightsTemp.add(weightVertex);
			for(int j = 0; j < stride; j++){
				int baseOffset = mapOffset+j*skinData.mStride;
				skeletonVertex.add((byte)skinData.mSkeletonMap[baseOffset+skinData.mSkeletonOffset]);
				int weightIndex = skinData.mSkeletonMap[baseOffset+skinData.mWeightOffset];
				weightVertex.add(skinData.mWeights[weightIndex]);
				mapCount++;
			}
		}
		
		byte[] skeletonMap = new byte[mapCount];
		float[] weights = new float[mapCount];
		int current = 0;
		for(int i = 0; i < skeletonMapTemp.size(); i++){
			for(int j = 0; j < skeletonMapTemp.get(i).size(); j++){
				skeletonMap[current] = skeletonMapTemp.get(i).get(j);
				weights[current] = weightsTemp.get(i).get(j);
				current++;
			}
		}
		
		skin.setSkeletonMap(skeletonMap);
		skin.setWeights(weights);
		skin.setWeightStrides(weightStrides);
		
		source.addModifier(skin);
		
		this.mPrimitives.put(skinId, source);
	}
	
	private Surface createSurface(String materialId){
		Surface surface = Surface.getDefaultSurface();
		if(materialId == null){
			return surface;
		}
		String effectId = this.mEffectsLink.get(materialId);
		Effect effect = this.mEffects.get(effectId);
		if(effect != null){
			if(effect.diffuse != null){
				if(effect.diffuse.textureId != null){
					Texture texture = new Texture();
					texture.setDiffuseTextureName(this.mTextureNames.get(effect.diffuse.textureId));
					surface = texture;
					//set blending
					if(effect.transparent != null && effect.diffuse.textureId.equals(effect.transparent.textureId)){
						texture.setBlending(Surface.BLEND_ALPHA);
					}
				} else if(effect.diffuse.color != null){
					Color color = new Color();
					color.getColor().copy( effect.diffuse.color);
					surface = color;
				}
			}
		}
		return surface;
	}
	
	private void parseAssets() throws Exception{
		if(this.moveToChildNode(NameId.unit, NameId.asset)){
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
		
		int eventType = this.mParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
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
            				sampler.mInputSource = this.getRefAttr(NameId.source);
            				break;
            			case OUTPUT:
            				sampler.mOutputSource = this.getRefAttr(NameId.source);
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
		}
	}
	
	private void createChannels(String samplerId, String targetId) throws Exception{
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
		
		if(!this.mSamplers.containsKey(samplerId)){
			throw new InvalidRefException(NameId.channel, samplerId);
		}
		Sampler sampler = this.mSamplers.get(samplerId);
		
		if(!this.mSources.containsKey(sampler.mInputSource)){
			throw new InvalidRefException(NameId.sampler, sampler.mId, sampler.mInputSource);
		}
		if(!this.mSources.containsKey(sampler.mOutputSource)){
			throw new InvalidRefException(NameId.sampler, sampler.mId, sampler.mOutputSource);
		}
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
			float previousValue = Float.POSITIVE_INFINITY;
			for(int j  = 0; j < input.length; j++){
				float value = output[j*targetIds.length+i];
				if(!Util.floatEquals(value, previousValue)){
					channel.addKeyFrame(new Channel.KeyFrame(output[j*targetIds.length+i], (long)(input[j]*1000)));
					previousValue = value;
				}
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
		String id = this.getAttr(NameId.id);
		
		String[] nameArray = null;
		float[] floatArray = null;
		
		int eventType = this.mParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
			switch(eventType){
			case XmlPullParser.START_TAG:
				switch(this.getTagId()){
				case float_array:
					floatArray = this.parseFloatArray(NameId.float_array);
					break;
				case Name_array:
					nameArray = this.parseStringArray(NameId.Name_array);
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == NameId.source){
					if(floatArray != null || nameArray != null){
						Source source = new Source(id);
						source.mFloatSource = floatArray;
						source.mNameSource = nameArray;
						this.mSources.put(source.mId, source);
					}
					return;
				}
				break;
			}
		}
	}
	
	private String[] parseStringArray(NameId id) throws Exception{
		String value = this.parseString(id);
		if(value != null){
			return value.split("[ \t\n\r]+");
		} else {
			return null;
		}
	}
	
	private String parseString(NameId parentId) throws Exception{
		String value = null;
		
		int eventType = this.mParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
			switch(eventType){
			case XmlPullParser.TEXT:
				value = this.mParser.getText().trim();
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == parentId){
					return value;
				}
				break;
			}
		}
		return null;
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
	
	private int[] parseIntArray(NameId id) throws Exception{
		String[] strings = this.parseStringArray(id);
		if(strings != null){
			int[] values = new int[strings.length];
			for(int i = 0; i<strings.length; i++){
				values[i] = Integer.parseInt(strings[i]);
			}
			return values;
		}
		return null;
	}
		
	private void parseLibraryVisualScenes() throws Exception{
		
		if(this.moveToChildNode(NameId.visual_scene, NameId.library_visual_scenes)){
			while(this.moveToChildNode(NameId.node, NameId.visual_scene)){
				Node node;
				if(this.getAttrId(NameId.type) == NameId.JOINT){
					node = this.parseSkeleton();
				} else {
					node = this.parseNode();
				}
				if(node != null){
					this.mSceneRoots.add(node);
				}
			}
		}
	}
	
	private Node parseNode() throws Exception{
		String name = this.getAttr(NameId.id);
		Matrix3d transform = new Matrix3d();
		ArrayList<Node> children = new ArrayList<Node>();
		boolean hasChildNodes = false;
		
		int eventType = this.mParser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
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
					children.add(parseModel(name));
					name = name+"/group"; //if this is a group, it will get this name.
					break;
				case instance_controller:
					children.add(parseModel(name));
					name = name+"/group"; //if this is a group, it will get this name.
					break;
				case node:
					Node child;
					if(this.getAttrId(NameId.type) == NameId.JOINT){
						child  = this.parseSkeleton();
					} else {
						child = parseNode();
					}
					if(child != null){
						hasChildNodes = true;
						children.add(child);
					}
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				switch(this.getTagId()){
				case node:
					Node retVal = null;
					if(hasChildNodes || children.size() > 1){
						Group group = new Group(name, children);
						retVal = group;
					} else if(children.size() == 1){
						retVal = children.get(0);
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
	
	private Model parseModel(String name) throws Exception{
		Model model = new Model(name);
		ModelLink modelLink = new ModelLink();
		modelLink.mModel = model;
		modelLink.mPrimitiveId = this.getRefAttr(NameId.url);
		if(this.moveToChildNode(NameId.instance_material, NameId.instance_geometry)){
			modelLink.mSurfaceId = this.getRefAttr(NameId.target);
		}
		this.mModels.add(modelLink);
		return model;
	}
	
	Matrix3d temp = new Matrix3d();
	Vector3d tempVector = new Vector3d();
	
	private void parseMatrix(Matrix3d current) throws Exception{
		float[] vals = this.parseFloatArray(NameId.matrix);
		this.flipMatrix(current, vals, 0);
		current.getTranslation().scale(this.mScale);
	}
	
	private void flipMatrix(Matrix3d current, float[] source, int offset){
		for(int row = 0; row < Matrix3d.SIZEROW; row++){
			for(int col = 0; col < Matrix3d.SIZEROW; col++){
				current.setValue(source[offset+row+Matrix3d.SIZEROW*col], row, col);
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
		JointRotate subJoint = new JointRotate(name+"/rotate");
		JointNode subJointNode = new JointNode(subJoint);
		
		int eventType = this.mParser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
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
						JointNode child = this.parseJoint();
						if(child != null){
							subJointNode.mChildren.add(child);
						}
					}
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == NameId.node){
					joint.appendChild(subJointNode.createSkeleton(false));
					joint.createAllBones(0.05f);
					return joint;
				}
				break;
			}
		}
		return null;
	}
	
	/**
	 * This is to account for rearranging sibling joints which can occur either by user mistake or just the fact that
	 * one collada exporter orders siblings in a different way (this was an issue i ran into a while back). This class
	 * is a temporary skeleton that then sorts the child nodes alphabetically before generating the channel information
	 * so that as long as the skeleton joint names are the same for all nodes above the root, the animations should 
	 * import correctly. namespaces on joints will also work, as long as it sorts the same.
	 * @author Todd Espiritu Santo
	 *
	 */
	private class JointNode implements Comparator<JointNode>{
		Joint mJoint;
		Joint mJointOrient;
		List<JointNode> mChildren;
		
		JointNode(Joint joint){
			this.mJoint = joint;
			mChildren = new ArrayList<JointNode>();
		}
		
		void sortChildren(){
			Collections.sort(mChildren, this);
		}
		
		Joint createSkeleton(boolean genChannels){
			if(genChannels){
				generateChannels(this.mJoint.getName(), true, true, true, true); //rotate
			}
			this.sortChildren();
			for(JointNode child : this.mChildren){	
				this.mJoint.appendChild(child.createSkeleton(true));
			}
			if(mJointOrient != null){
				mJointOrient.appendChild(mJoint);
				return mJointOrient;
			} else {
				return this.mJoint;
			}
		}

		@Override
		public int compare(JointNode o1, JointNode o2) {
			return o1.mJoint.getName().compareTo(o2.mJoint.getName());
		}
		
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
	
	private JointNode parseJoint() throws Exception{
		String name = this.getAttr(NameId.id);
		
		Matrix3d orientMatrix = new Matrix3d();
		
		Joint joint = new JointRotate(name);
		JointNode jointNode = new JointNode(joint);
		
		int eventType = this.mParser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
			switch(eventType){
			case XmlPullParser.START_TAG:
				switch(this.getTagId()){
				case matrix:
					this.parseMatrix(orientMatrix);
					break;
				case translate:
					this.parseTranslate(orientMatrix);
					break;
				case rotate:
					String sid = this.getAttr(NameId.sid);
					if(sid != null && sid.startsWith("jointOrient")){
						this.parseRotate(orientMatrix);
					} else {
						this.parseRotate(joint.getTransform());
					}
					break;
				case node:
					if(this.getAttrId(NameId.type) == NameId.JOINT){
						JointNode child = this.parseJoint();
						if(child != null){
							jointNode.mChildren.add(child);
						}
					}
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == NameId.node){
					if(orientMatrix.isIdentity3x3()){
						joint.getTransform().multiply(orientMatrix);
					} else {
						JointOrient jointO = new JointOrient(name+"/orient");
						jointO.getTransform().copy(orientMatrix);
						jointNode.mJointOrient = jointO;
					}
					return jointNode;
				}
				break;
			}
		}
		return null;
	}
	
	private void parseLibraryGeometries() throws Exception{
		while(this.moveToChildNode(NameId.geometry, NameId.library_geometries)){
			String name = this.getAttr(NameId.id);
			if(this.moveToChildNode(NameId.mesh, NameId.geometry)){
				this.parseMesh(name);
			}
		}
	}
	
	private void parseMesh(String name) throws Exception{
		Inputs inputs = new Inputs();
		Vector<Integer> indices = new Vector<Integer>();
		
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
					while(this.moveToChildNode(NameId.input, NameId.vertices)){
						this.parseInput(inputs);
					}
					break;
				case polygons:
					indices = parseIndices(inputs, NameId.polygons);
					break;
				case triangles:
					indices = parseIndices(inputs, NameId.triangles);
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == NameId.geometry){
					this.createPrimitive(name, inputs, indices);
					return;
				}
				break;
			}
		}
	}
	
	private Vector<Integer> parseIndices(Inputs inputs, NameId parentId) throws Exception{
		Vector<Integer> indices = new Vector<Integer>();
		int eventType = this.mParser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
			switch(eventType){
			case XmlPullParser.START_TAG:
				switch(this.getTagId()){
				case input:
					this.parseInput(inputs);
					break;
				case p:
					String[] values = this.parseStringArray(NameId.p);
					for(int i = 0; i < values.length; i++){
						indices.add(Integer.parseInt(values[i]));
					}
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == parentId){
					return indices;
				}
				break;
			}
		}
		return indices;
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
		
		//for skinning data later on!
		Vector<Integer> skinRemap = new Vector<Integer>();
		this.mSkinRemaps.put(name, skinRemap);
		
		int nextIndex = 0;
		//expand vertices!
		for(int i = 0; i < indices.size()/count; i++){
			String id = "";
			int positionIndex = 0;
			for(int j = 0; j < count; j++){
				int index = indices.get(i*count+j);
				//for skinning!
				if(j == inputs.mPositionOffset){
					positionIndex = index;
				}
				id += index+",";
			}
			if(remap.containsKey(id)){
				indicesRemap.add(remap.get(id));
				continue;
			}

			remap.put(id, nextIndex);
			indicesRemap.add(nextIndex);
			skinRemap.add(positionIndex);
			nextIndex++;
			
			int index;
			if(position != null){
				index = indices.get(i*count+inputs.mPositionOffset)*3;
				positionRemap.add(position[index]*this.mScale);
				positionRemap.add(position[index+1]*this.mScale);
				positionRemap.add(position[index+2]*this.mScale);
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
				if(this.mFlipYTexCoord){
					texcoordRemap.add(1-texcoord[index+1]);
				} else {
					texcoordRemap.add(texcoord[index+1]);
				}
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
		VertexIndices mesh = new VertexIndices(indicesRemap.size(), nextIndex, typesArray);
		VertexBuffer vb = mesh.getVertexBuffer();
		vb.lock();
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
		vb.unlock();
		
		IndexBuffer ib = mesh.getIndexBuffer();
		ib.lock();
		for(int i = 0; i < indicesRemap.size(); i+=3){
			ib.addTriangle(indicesRemap.get(i),
						   indicesRemap.get(i+1),
						   indicesRemap.get(i+2));
		}
		ib.unlock();
		
		this.mPrimitives.put(name, mesh);
		
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
	
	private void parseInput(Inputs inputs){
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
	
	private void parseLibraryMaterials() throws Exception{
		while(this.moveToChildNode(NameId.material, NameId.library_materials)){
			String id = this.getAttr(NameId.id);
			if(this.moveToChildNode(NameId.instance_effect, NameId.material)){
				String effectsTarget = this.getRefAttr(NameId.url);
				if(effectsTarget != null){
					this.mEffectsLink.put(id, effectsTarget);
				}
			}
		}
	}
	
	private boolean isEffect(NameId id){
		return id == NameId.phong || id == NameId.blinn || id == NameId.lambert;
	}
	
	private void parseLibraryEffects() throws Exception{
		while(this.moveToChildNode(NameId.effect, NameId.library_effects)){
			String id = this.getAttr(NameId.id);
			if(this.moveToChildNode(NameId.technique, NameId.effect) && this.moveToFirstChild()){
				if(this.isEffect(this.getTagId())){
					Effect effect = this.parseEffect();
					if(effect != null){
						this.mEffects.put(id, effect);
					}
				}
			}
		}
	}
	
	private Effect parseEffect() throws Exception{
		Effect effect = null;
		int eventType = this.mParser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
			switch(eventType){
			case XmlPullParser.START_TAG:
				switch(this.getTagId()){
				case diffuse:
					if(effect == null){
						effect = new Effect();
					}
					effect.diffuse = parseEffectParam(NameId.diffuse); 
					break;
				case transparent:
					if(effect == null){
						effect = new Effect();
					}
					effect.transparent = parseEffectParam(NameId.transparent);
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.isEffect(this.getTagId())){
					return effect;
				}
				break;
			}
		}
		return null;
	}
	
	private EffectParam parseEffectParam(NameId paramId) throws Exception{
		EffectParam effect = new EffectParam();

		int eventType = this.mParser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
			switch(eventType){
			case XmlPullParser.START_TAG:
				switch(this.getTagId()){
				case texture:
					effect.textureId = this.getAttr(NameId.texture);
					break;
				case color:
					float[] colorBuffer = this.parseFloatArray(NameId.color);
					if(colorBuffer != null){
						effect.color = new Color4(colorBuffer);
					}
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == paramId)
					return effect;
				break;
			}
		}
		return null;
	}
	
	private void parseLibraryImages() throws Exception{
		while(this.moveToChildNode(NameId.image, NameId.library_images)){
			String id = this.getAttr(NameId.id);
			if(this.moveToChildNode(NameId.init_from, NameId.image)){
				String texture = this.parseString(NameId.init_from);
				if(texture != null){
					this.mTexturePaths.add(texture);
					int lastIndex = texture.lastIndexOf('/');
					if(lastIndex != -1){
						texture = texture.substring(lastIndex+1);
					}
					this.mTextureNames.put(id, texture);
				}
			}
		}
	}
	
	private void parseLibraryControllers() throws Exception{
		while(this.moveToChildNode(NameId.controller, NameId.library_controllers)){
			String name = this.getAttr(NameId.id);
			if(this.moveToChildNode(NameId.skin, NameId.controller)){
				this.parseSkin(name);
			}
		}
	}
	
	private void parseSkin(String name) throws Exception{
		String source = this.getRefAttr(NameId.source);
		SkinInputs inputs = new SkinInputs();
		SkinData skinData = null;
		int eventType = this.mParser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
			switch(eventType){
			case XmlPullParser.START_TAG:
				switch(this.getTagId()){
				case source:
					this.parseSource();
					break;
				case joints:
					while(this.moveToChildNode(NameId.input, NameId.joints)){
						this.parseSkinInput(inputs);
					}
					break;
				case vertex_weights:
					skinData = this.parseVertexWeights(name, inputs);
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == NameId.skin){
					this.convertSkinData(skinData, source, inputs);
					return;
				}
				break;
			}
		}
	}
	
	private class SkinData{
		String mId;
		String mSource;
		int[] mStrideMap;
		int[] mSkeletonMap;
		String[] mJoints;
		Matrix3d[] mBindMatricesInv;
		float[] mWeights;
		int mSkeletonOffset;
		int mWeightOffset;
		int mStride;
		
		SkinData(String id){
			this.mId = id;
		}
	}
	
	private void convertSkinData(SkinData skinData, String source, SkinInputs input) throws Exception{
		//convert the bind matrix!
		float[] matrixSource = this.mSources.get(input.mBindMatrix).mFloatSource;
		float[] matrixBuffer = new float[matrixSource.length];
		Matrix3d[] bindMatrixInv = new Matrix3d[matrixBuffer.length/Matrix3d.SIZE];
		for(int i = 0; i < bindMatrixInv.length; i++){
			int offset = Matrix3d.SIZE*i;
			Matrix3d current = new Matrix3d(matrixBuffer, offset);
			bindMatrixInv[i] = current;
			this.flipMatrix(current, matrixSource, offset);
			//adjust for the scale
			//TODO: compute this in a better way that doesn't require inverting the data
			bindMatrixInv[i].invert().getTranslation().scale(this.mScale);
			bindMatrixInv[i].invert();
		}
		skinData.mBindMatricesInv = bindMatrixInv;
		skinData.mSource = source;
		skinData.mWeights = this.mSources.get(input.mWeights).mFloatSource;
		skinData.mJoints = this.mSources.get(input.mJoint).mNameSource;
		skinData.mSkeletonOffset = input.mJointOffset;
		skinData.mWeightOffset = input.mWeightsOffset;
		
		//store this data so that it can be bound!
		this.mSkinDatas.put(skinData.mId, skinData);
	}
	
	private SkinData parseVertexWeights(String name, SkinInputs inputs) throws Exception{
		SkinData skinData = new SkinData(name);
		int eventType = this.mParser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			eventType = this.mParser.next();
			switch(eventType){
			case XmlPullParser.START_TAG:
				switch(this.getTagId()){
				case input:
					this.parseSkinInput(inputs);
					skinData.mStride++;
					break;
				case vcount:
					skinData.mStrideMap = this.parseIntArray(NameId.vcount);
					break;
				case v:
					skinData.mSkeletonMap = this.parseIntArray(NameId.v);
					break;
				}
				break;
			case XmlPullParser.END_TAG:
				if(this.getTagId() == NameId.vertex_weights){
					return skinData;
				}
				break;
			}
		}
		return null;
	}
	
	private class SkinInputs{
		String mJoint;
		int mJointOffset = -1;
		String mBindMatrix;
		String mWeights;
		int mWeightsOffset = -1;
		
	}
	
	private void parseSkinInput(SkinInputs inputs) throws Exception{
		String offsetString = this.getAttr(NameId.offset);
		int offset = -1;
		if(offsetString != null){
			offset = Integer.parseInt(offsetString);
		}
		switch(this.getAttrId(NameId.semantic)){
		case JOINT:
			//COLLADA uses the same identifier for two different cases......
			if(offset == -1){
				inputs.mJoint = this.getRefAttr(NameId.source);
			} else {
				inputs.mJointOffset = offset;
			}
			break;
		case WEIGHT:
			inputs.mWeights = this.getRefAttr(NameId.source);
			if(offset != -1){
				inputs.mWeightsOffset = offset;
			}
			break;
		case INV_BIND_MATRIX:
			inputs.mBindMatrix = this.getRefAttr(NameId.source);
			break;
		}
	}

	@Override
	public List<Animation> getAnimations() {
		return this.mAnimations;
	}

	@Override
	public List<Camera> getCameras() {
		return this.mCameras;
	}

	@Override
	public List<Node> getScenes() {
		return this.mSceneRoots;
	}

	@Override
	public List<Player> getPlayers() {
		return this.mPlayers;
	}
}
