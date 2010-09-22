package com.tespirit.panda3d.convert;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

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
public class Collada {
	private Node root;
	private LightGroup lights;
	private Camera[] cameras;
	private Animation animation;
	
	private Map<String, Primitive> primitives;
	private Map<String, Surface> surfaces;
	private Map<String, Integer> channelOffset;
	
	private ArrayList<Channel> jointChannels;
	
	private XPath xpath;
	
	private Document document;
	
	private float scale;
	
	private boolean normals;
	
	
	/**
	 * use this to disable normals from importing since normals are only
	 * needed if there is lighting.
	 * @param fileName
	 * @param normals
	 * @throws Exception
	 */
	public Collada(String fileName, boolean normals)throws Exception{
		this.normals = normals;
		this.init(Assets.getManager().openXmlDom(fileName));
	}
	
	public Collada(String fileName)throws Exception{
		this.normals = true;
		this.init(Assets.getManager().openXmlDom(fileName));
	}
	
	public Collada(Document document) throws Exception{
		this.normals = true;
		this.init(document);
	}
	
	/**
	 * use this to disable normals from importing since normals are only
	 * needed if there is lighting.
	 * @param document
	 * @param normals
	 * @throws Exception
	 */
	public Collada(Document document, boolean normals) throws Exception{
		this.normals = normals;
		this.init(document);
	}
	
	private void init(Document document) throws Exception{
		XPathFactory factory = XPathFactory.newInstance();
	    this.xpath = factory.newXPath();
		this.document = document;
		this.primitives = new TreeMap<String, Primitive>();
		this.surfaces = new TreeMap<String, Surface>();
		this.channelOffset = new TreeMap<String, Integer>();
		this.jointChannels = new ArrayList<Channel>();
		
		NodeList nodes = document.getElementsByTagName("asset");
		org.w3c.dom.Node node;
		this.scale = 1;
		if(nodes.getLength() > 0){
			node = document.getElementsByTagName("asset").item(0);
			node = this.getChildNodeByType("unit", node);
			String unit = this.getAttribute("meter", node);
			if(unit != null){
				this.scale = Float.parseFloat(unit);
			}
		}
		
		node = document.getElementsByTagName("scene").item(0);
		node = this.getChildNodeByType("instance_visual_scene", node);
		node = this.getNodeByUrl(this.getAttribute("url", node));
		this.root = this.convertSceneNode(node);		
		
		if(this.jointChannels.size() > 0){
			NodeList animations = document.getElementsByTagName("library_animations");
			if(animations != null && animations.getLength() > 0){
				this.animation = this.convertAnimations(animations.item(0));
			}
		}
	}
	
	private Animation convertAnimations(org.w3c.dom.Node animations){
		NodeList children = animations.getChildNodes();
		boolean success = false;
		for(int i = 0; i < children.getLength(); i++){
			org.w3c.dom.Node child = children.item(i);
			if(child.getNodeName().equalsIgnoreCase("animation")){
				if(this.convertChannel(child)){
					success = true;
				}
			}
		}
		
		if(success){
			Animation a = new Animation(this.jointChannels.size());
			for(Channel c : this.jointChannels){
				a.addChannel(c);
			}
			return a;
		}
		
		return null;
	}
	
	private boolean convertChannel(org.w3c.dom.Node animation){
		org.w3c.dom.Node channel = this.getChildNodeByType("animation", animation);
		if(channel != null){
			this.convertChannel(channel);
		}
		channel = this.getChildNodeByType("channel", animation);
		if(channel == null){
			return false;
		}
		String url = this.getAttribute("source", channel);
		String target = this.getAttribute("target", channel);
		
		int offset;
		if(this.channelOffset.containsKey(target)){
			offset = this.channelOffset.get(target);
		} else {
			return false;
		}
		
		float[] time = null;
		float[] values = null;
		
		channel = this.getNodeByUrl(url);
		
		for(int i = 0; i < channel.getChildNodes().getLength(); i++){
			org.w3c.dom.Node input = channel.getChildNodes().item(i);
			if(input.getNodeName().equals("input")){
				String semantic = this.getAttribute("semantic", input);
				if(semantic.equals("INPUT")){
					input = this.getNodeByUrl(this.getAttribute("source", input));
					time = this.convertToFloatArray(this.getChildNodeByType("float_array", input));
				} else if(semantic.equals("OUTPUT")){
					input = this.getNodeByUrl(this.getAttribute("source", input));
					values = this.convertToFloatArray(this.getChildNodeByType("float_array", input));	
				}
			}
		}
		
		int channelCount = values.length / time.length;
		
		for(int i = 0; i < channelCount; i++){
			Channel c = this.jointChannels.get(offset+i);
			for(int j = 0; j < time.length; j++){
				c.addKeyFrame(new Channel.KeyFrame(values[j*channelCount+i], (long)(time[j]*1000)));
			}
		}
		
		return true;
	}
	
	private String getAttribute(String name, org.w3c.dom.Node node){
		org.w3c.dom.Node attr = node.getAttributes().getNamedItem(name);
		if(attr != null){
			return attr.getNodeValue();
		}
		return null;
	}
	
	private void generateChannels(String name, boolean rotate, boolean x, boolean y, boolean z){
		String prefix;
		String postfix;
		if(rotate){
			name = name +"/Rotate";
			prefix = "";
			postfix = ".Angle";
		} else {
			name = name + "/Translate";
			prefix = ".";
			postfix = "";
		}
		
		if(x){
			this.jointChannels.add(new Channel());
			this.channelOffset.put(name+prefix+"X"+postfix, this.jointChannels.size()-1);
		}
		if(y){
			this.jointChannels.add(new Channel());
			this.channelOffset.put(name+prefix+"Y"+postfix, this.jointChannels.size()-1);
		}
		if(z){
			this.jointChannels.add(new Channel());
			this.channelOffset.put(name+prefix+"Z"+postfix, this.jointChannels.size()-1);
		}
		
		if(x && y && z){
			this.channelOffset.put(name+"XYZ", this.jointChannels.size()-3);
		}
	}
	
	private Joint convertJoint(org.w3c.dom.Node node){
		String name = getAttribute("id", node);
		Joint joint = new JointRotate();
		JointOrient jointO = null;
		this.generateChannels(name, true, true, true, true);
		for(int i = 0; i < node.getChildNodes().getLength(); i++){
			org.w3c.dom.Node child = node.getChildNodes().item(i);
			String type = child.getNodeName();
			
			if(type.equals("matrix")){
				this.pushMatrix(joint.getTransform(), child);
			} else if(type.equals("translate")){ 
				this.pushTranslate(joint.getTransform(), child);
			} else if(type.equals("rotate")){
				type = this.getAttribute("sid", child);
				if(type != null && type.startsWith("jointOrient")){
					if(jointO == null){
						jointO = new JointOrient();
						jointO.getTransform().copy(joint.getTransform());
						joint.getTransform().identity();
					}
					this.pushRotate(jointO.getTransform(), child);
				} else {
					this.pushRotate(joint.getTransform(), child);
				}
			} else if(child.getNodeName().equalsIgnoreCase("node")){
				type = this.getAttribute("type", child);
				if(type.equals("JOINT")){
					joint.appendChild(this.convertJoint(child));
				}
			}
		}
		if(jointO != null){
			if(jointO.getTransform().isIdentity3x3()){
				jointO.getTransform().multiply(joint.getTransform());
				joint.getTransform().copy(jointO.getTransform());
			} else {
				joint = jointO;
			}
		}
		return joint;
	}
	
	private Joint convertSkeleton(org.w3c.dom.Node node){
		//generate the node type!
		String name = getAttribute("id", node);
		this.generateChannels(name, false, true, true, true); //translate
		this.generateChannels(name, true, true, true, true); //rotate
		
		JointTranslate joint = new JointTranslate(name);
		JointRotate subJoint = new JointRotate();
		joint.appendChild(subJoint);
		for(int i = 0; i < node.getChildNodes().getLength(); i++){
			org.w3c.dom.Node child = node.getChildNodes().item(i);
			String type = child.getNodeName();
			
			if(type.equals("matrix")){
				this.pushMatrix(joint.getTransform(), child);
			} else if(type.equals("translate")){ 
				this.pushTranslate(joint.getTransform(), child);
			} else if(type.equals("rotate")){
				type = this.getAttribute("sid", child);
				if(type != null && type.startsWith("jointOrient")){
					this.pushRotate(joint.getTransform(), child);
				} else {
					this.pushRotate(subJoint.getTransform(), child);
				}
			} else if(child.getNodeName().equalsIgnoreCase("node")){
				type = this.getAttribute("type", child);
				if(type.equals("JOINT")){
					subJoint.appendChild(this.convertJoint(child));
				}
			}
		}
		joint.createAllBones(0.5f);
		return joint;
	}
	
	private Node convertSceneNode(org.w3c.dom.Node node){
		Matrix3d localTransform = new Matrix3d();
		NodeList children = node.getChildNodes();
		Group g = null;
		Model m = null;
		Joint j = null;
		Node retVal = null;
		String name = getAttribute("id", node);
		for(int i = 0; i < children.getLength(); i++){
			org.w3c.dom.Node child = children.item(i);
			String type = child.getNodeName().toLowerCase();
			if(type.equals("matrix")){
				this.pushMatrix(localTransform, child);
			} else if(type.equals("translate")){ 
				this.pushTranslate(localTransform, child);
			} else if(type.equals("rotate")){
				this.pushRotate(localTransform, child);
			} else if(type.equals("scale")){
				this.pushScale(localTransform, child);
			} else if(type.equals("node")){
				type = this.getAttribute("type", child);
				if(type != null && type.equals("JOINT")){
					j = this.convertSkeleton(child);
				} else {
					if(g == null){
						g = new Group(name);
						retVal = g;
					}
					g.appendChild(this.convertSceneNode(child));
				}
			} else if(type.equals("instance_geometry")){
				m = new Model(name);
				m.setSurface(this.convertMaterial(child));
				m.setPrimative(this.convertGeometry(child));
			}
		}
		if((m != null || j != null) && g != null){
			g.appendChild(j);
			g.appendChild(m);
		} else if(m != null){
			retVal = m;
		} else if(j != null){
			retVal = j;
		}
		
		if(localTransform != null && retVal != null){
			retVal.getTransform().copy(localTransform);
		}
		
		if(node.getNodeName().equalsIgnoreCase("visual_scene") && g != null){
			if(g.getChildCount() == 1){
				retVal = g.getChild(0);
			}
		}
		return retVal;
	}

	Matrix3d temp = new Matrix3d();
	Vector3d tempVector = new Vector3d();
	
	private void pushMatrix(Matrix3d current, org.w3c.dom.Node node){
		float[] vals = this.convertToFloatArray(node);
		for(int row = 0; row < Matrix3d.SIZEROW; row++){
			for(int col = 0; col < Matrix3d.SIZEROW; col++){
				temp.setValue(vals[col+Matrix3d.SIZEROW*row], row, col);
			}
		}
		temp.getTranslation().scale(this.scale);
		current.multiply(temp);
	}
	
	private void pushTranslate(Matrix3d current, org.w3c.dom.Node node){
		float[] vals = this.convertToFloatArray(node);
		temp.copy(current);
		current.identity().translate(vals[0], vals[1], vals[2]);
		current.getTranslation().scale(this.scale);
		current.multiply(temp);
	}
	
	private void pushRotate(Matrix3d current, org.w3c.dom.Node node){
		float[] vals = this.convertToFloatArray(node);
		temp.copy(current);
		tempVector.set(vals[0], vals[1], vals[2]);
		current.identity().rotateAxis(vals[3], tempVector);
		current.multiply(temp);
	}
	
	private void pushScale(Matrix3d current, org.w3c.dom.Node node){
		float[] vals = this.convertToFloatArray(node);
		temp.copy(current);
		current.identity().scale(vals[0], vals[1], vals[2]);
		current.multiply(temp);
	}
	
	private float[] convertToFloatArray(org.w3c.dom.Node node){
		String[] values = node.getFirstChild().getNodeValue().trim().split("[ \t\n\r]+");
		float[] retVals = new float[values.length];
		for(int i = 0; i < values.length; i++){
			retVals[i] = Float.parseFloat(values[i]);
		}
		return retVals;
	}
	
	private int[] convertToIntArray(org.w3c.dom.Node node){
		String[] values = node.getFirstChild().getNodeValue().trim().split("[ \t\n\r]+");
		int[] retVals = new int[values.length];
		for(int i = 0; i < values.length; i++){
			retVals[i] = Integer.parseInt(values[i]);
		}
		return retVals;
	}
	
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
	
	class ColladaVb{
		float[] positions;
		float[] normals;
		float[] texcoords;
		float[] colors;
		
		Vector<Float> positions2;
		Vector<Float> normals2;
		Vector<Float> texcoords2;
		Vector<Float> colors2;
		
		float[][] buffer;
		
		int nextIndex;
		
		Hashtable<String, Integer> remap;
		
		ColladaVb(){
			this.remap = new Hashtable<String, Integer>();
			this.nextIndex = 0;
		}
		
		int registerVertex(int[] buffer, int offset){
			//generate id
			String id = "";
			for(int i = 0; i < this.count; i++){
				id += buffer[i+offset]+",";
			}
			if(this.remap.containsKey(id)){
				return this.remap.get(id);
			}
			
			int retVal = this.nextIndex;
			this.remap.put(id, retVal);
			
			int index;
			if(this.positions != null){
				index = buffer[offset+this.positionOffset]*3;
				this.positions2.add(this.positions[index]);
				this.positions2.add(this.positions[index+1]);
				this.positions2.add(this.positions[index+2]);
			}
			if(this.normals != null){
				index = buffer[offset+this.normalOffset]*3;
				this.normals2.add(this.normals[index]);
				this.normals2.add(this.normals[index+1]);
				this.normals2.add(this.normals[index+2]);
			}
			if(this.texcoords != null){
				index = buffer[offset+this.texcoordOffset]*2;
				this.texcoords2.add(this.texcoords[index]);
				this.texcoords2.add(this.texcoords[index+1]);
			}
			if(this.colors != null){
				index = buffer[offset+this.colorOffset]*4;
				this.colors2.add(this.colors[index]);
				this.colors2.add(this.colors[index+1]);
				this.colors2.add(this.colors[index+2]);
				this.colors2.add(this.colors[index+4]);
			}
			
			this.nextIndex++;
			return retVal;
		}
		
		VertexBuffer createVb(){
			VertexBuffer vb = new VertexBuffer(this.nextIndex, this.types);
			
			for(int i = 0; i < this.nextIndex; i++){
				if(this.positions2 != null){
					vb.addPosition(scale*this.positions2.get(i*3),
								   scale*this.positions2.get(i*3+1), 
								   scale*this.positions2.get(i*3+2));
				}
				if(this.normals2 != null){
					vb.addNormal(this.normals2.get(i*3),
								 this.normals2.get(i*3+1), 
								 this.normals2.get(i*3+2));
				}
				if(this.texcoords2 != null){
					vb.addTexcoord(this.texcoords2.get(i*2),
								   1-this.texcoords2.get(i*2+1));//flip y
				}
				if(this.colors2 != null){
					vb.addColor(this.colors2.get(i*4),
								this.colors2.get(i*4+1), 
								this.colors2.get(i*4+2),
								this.colors2.get(i*4+3));
				}
			}
			vb.resetBufferPosition();
			return vb;
		}
		
		int positionOffset;
		int normalOffset;
		int texcoordOffset;
		int colorOffset;
		
		int count;
		
		int[] types;
		
		final static int MAX = 4;
		
		void generateTypes(){
			int i = 0;
			this.types = new int[this.count];
			if(this.positions != null){
				this.types[i] = VertexBuffer.POSITION;
				i++;
			}
			if(this.normals != null){
				this.types[i] = VertexBuffer.NORMAL;
				i++;
			}
			if(this.texcoords != null){
				this.types[i] = VertexBuffer.TEXCOORD;
				i++;
			}
			if(this.colors != null){
				this.colors2 = new Vector<Float>();
				this.types[i] = VertexBuffer.COLOR;
			}
		}
		
		void setPosition(float[] values, int offset){
			this.positions2 = new Vector<Float>();
			this.positions = values;
			this.positionOffset = offset;
			this.count++;
		}
		
		void setNormal(float[] values, int offset){
			if(Collada.this.normals){
				this.normals2 = new Vector<Float>();
				this.normals = values;
				this.normalOffset = offset;
				this.count++;
			}
		}
		
		void setTexcoord(float[] values, int offset){
			this.texcoords2 = new Vector<Float>();
			this.texcoords = values;
			this.texcoordOffset = offset;
			this.count++;
		}
		
		void setColor(float[] values, int offset){
			this.colors2 = new Vector<Float>();
			this.colors = values;
			this.colorOffset = offset;
			this.count++;
		}
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
	}
	
	public Node getSceneGraph(){
		return this.root;
	}
	
	public LightGroup getLightGroup(){
		return this.lights;
	}
	
	public Camera[] getCameras(){
		return this.cameras;
	}
	
	public Animation getAnimation(){
		return this.animation;
	}
}
