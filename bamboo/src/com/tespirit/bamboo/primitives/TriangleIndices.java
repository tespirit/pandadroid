package com.tespirit.bamboo.primitives;

import java.util.Stack;

import com.tespirit.bamboo.render.ComponentRenderer;
import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.modifiers.VertexModifier;

/**
 * This is a standard mesh class that has a vertex buffer and an index buffer.
 * @author Todd Espiritu Santo
 *
 */
public class TriangleIndices extends Primitive{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1986380555935180298L;
	protected VertexBuffer vertexBuffer;
	protected IndexBuffer indexBuffer;
	protected Stack<VertexModifier> modifierStack;
	
	public TriangleIndices(VertexBuffer vb, IndexBuffer ib){
		this.vertexBuffer = vb;
		this.indexBuffer = ib;
		this.modifierStack = new Stack<VertexModifier>();
		this.renderAsTriangles();
	}
	
	public TriangleIndices(int indexCount, int vertexCount, int[] vertexTypes){
		this.vertexBuffer = new VertexBuffer(vertexCount, vertexTypes);
		this.renderAsTriangles(); //default setting.
		this.indexBuffer = new IndexBuffer(indexCount, vertexCount);
	}
	
	public VertexBuffer getVertexBuffer(){
		return this.vertexBuffer;
	}
	
	public IndexBuffer getIndexBuffer(){
		return this.indexBuffer;
	}
	
	public void addModifier(VertexModifier vm){
		if(this.modifierStack.isEmpty()){
			vm.setVertexBuffer(this.vertexBuffer);
		} else {
			vm.setVertexBuffer(this.modifierStack.peek().getModifiedBuffer());
		}
		this.vertexBuffer = vm.getModifiedBuffer();
		this.modifierStack.push(vm);
	}

	@Override
	public void computeBoundingBox(AxisAlignedBox boundingBox) {
		this.vertexBuffer.computeBoundingBox(boundingBox);
	}
	
	public void renderWireFrame(){
		this.renderAsLineStrip();
	}
	
	public void renderSolid(){
		this.renderAsTriangleStrip();
	}
	
	@Override
	public void render() {
		TriangleIndices.renderer.render(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			TriangleIndices.renderer = this;
		}
		
		public abstract void render(TriangleIndices triangles);
	}
}
