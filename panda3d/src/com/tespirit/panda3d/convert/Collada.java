package com.tespirit.panda3d.convert;

import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

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

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


/**
 * This is for loading collada files.
 * Right now there are a few things that the collada file must have:
 * triangulated geometry in the form of a collada polygons node.
 * each vertex maps 1 to 1 to a position,normal,texcoord,color ..... in other
 * words, a triangle uses the same index for all those values (just duplicated).
 * 
 * futute plans will get better support for different input types.
 * @author Todd Espiritu Santo
 *
 */
public class Collada {
	private Node root;
	private LightGroup lights;
	private Camera[] cameras;
	
	private Map<String, Primitive> primitives;
	private Map<String, Surface> surfaces;
	
	private XPath xpath;
	
	private Document document;
	
	public Collada(String fileName)throws Exception{
		this.init(Assets.getManager().openXmlDom(fileName));
	}

	public Collada(Document document) throws Exception{
		this.init(document);
	}
	
	private void init(Document document) throws Exception{
		XPathFactory factory = XPathFactory.newInstance();
	    xpath = factory.newXPath();
		this.document = document;
		this.primitives = new TreeMap<String, Primitive>();
		this.surfaces = new TreeMap<String, Surface>();
		this.root = this.convertSceneNode(document.getElementsByTagName("visual_scene").item(0));		
	}
	
	private String getAttribute(String name, org.w3c.dom.Node node){
		org.w3c.dom.Node attr = node.getAttributes().getNamedItem(name);
		if(attr != null){
			return attr.getNodeValue();
		}
		return null;
	}
	
	private Node convertSceneNode(org.w3c.dom.Node node){
		Matrix3d localTransform = null;
		NodeList children = node.getChildNodes();
		Group g = null;
		Model m = null;
		Node retVal = null;
		String name = getAttribute("id", node);
		for(int i = 0; i < children.getLength(); i++){
			org.w3c.dom.Node child = children.item(i);
			String type = child.getNodeName().toLowerCase();
			if(type.equals("matrix")){
				localTransform = this.convertMatrix(child);
			} else if(type.equals("node")){
				if(g == null){
					g = new Group(name);
					retVal = g;
				}
				g.appendChild(this.convertSceneNode(child));
			} else if(type.equals("instance_geometry")){
				m = new Model(name);
				m.setSurface(this.convertMaterial(child));
				m.setPrimative(this.convertGeometry(child));
			}
		}
		if(m != null && g != null){
			g.appendChild(m);
		} else if(m != null){
			retVal = m;
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
	
	private Matrix3d convertMatrix(org.w3c.dom.Node node){
		Matrix3d m = new Matrix3d();
		float[] vals = this.convertToFloatArray(node);
		for(int row = 0; row < Matrix3d.SIZEROW; row++){
			for(int col = 0; col < Matrix3d.SIZEROW; col++){
				m.setValue(vals[col+Matrix3d.SIZEROW*row], row, col);
			}
		}
		return m;
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
					p = convertPolygon(indices);
				}
			}
			
			this.primitives.put(url, p);
			
			return p;
		}
		return null;
	}
	
	private Primitive convertPolygon(org.w3c.dom.Node polygon){
		ColladaVb vb = this.convertVertexBuffer(polygon);
		if(vb != null){
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
			
			TriangleIndices triangles = new TriangleIndices(vb.createVb(), ib);
			return triangles;
		}
		return null;
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
					vb.addPosition(this.positions2.get(i*3),
								   this.positions2.get(i*3+1), 
								   this.positions2.get(i*3+2));
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
		
		void generateTypes(){
			int i = 0;
			this.types = new int[this.count];
			if(this.positions != null){
				this.positions2 = new Vector<Float>();
				this.types[i] = VertexBuffer.POSITION;
				i++;
			}
			if(this.normals != null){
				this.normals2 = new Vector<Float>();
				this.types[i] = VertexBuffer.NORMAL;
				i++;
			}
			if(this.texcoords != null){
				this.texcoords2 = new Vector<Float>();
				this.types[i] = VertexBuffer.TEXCOORD;
				i++;
			}
			if(this.colors != null){
				this.colors2 = new Vector<Float>();
				this.types[i] = VertexBuffer.COLOR;
			}
		}
	}
	
	private ColladaVb convertVertexBuffer(org.w3c.dom.Node source){
		ColladaVb vb = new ColladaVb();
		
		NodeList inputs = source.getChildNodes();
		for(int i = 0; i < inputs.getLength(); i++){
			org.w3c.dom.Node input = inputs.item(i);
			String type = input.getNodeName().toLowerCase();
			if(type.equals("input")){
				String semantic = this.getAttribute("semantic", input);
				int offset = Integer.parseInt(this.getAttribute("offset", input));
				if(semantic != null){
					org.w3c.dom.Node inputSource = this.getNodeByUrl(this.getAttribute("source", input));
					if(inputSource != null){
						if(semantic.equals("VERTEX")){
							inputSource = this.getNodeByUrl(this.getAttribute("source", this.getChildNodeByType("input", inputSource)));
							inputSource = this.getChildNodeByType("float_array", inputSource);
							vb.positions = this.convertToFloatArray(inputSource);
							vb.positionOffset = offset;
							vb.count++;
						} else if(semantic.equals("NORMAL")){
							inputSource = this.getChildNodeByType("float_array", inputSource);
							vb.normals = this.convertToFloatArray(inputSource);
							vb.normalOffset = offset;
							vb.count++;
						} else if(semantic.equals("TEXCOORD")){
							inputSource = this.getChildNodeByType("float_array", inputSource);
							vb.texcoords = this.convertToFloatArray(inputSource);
							vb.texcoordOffset = offset;
							vb.count++;
						} else if(semantic.equals("COLOR")){
							inputSource = this.getChildNodeByType("float_array", inputSource);
							vb.colors = this.convertToFloatArray(inputSource);;
							vb.colorOffset = offset;
							vb.count++;
						}
					}
				}
			}
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
		return root;
	}
	
	public LightGroup getLightGroup(){
		return lights;
	}
	
	public Camera[] getCameras(){
		return cameras;
	}
}
