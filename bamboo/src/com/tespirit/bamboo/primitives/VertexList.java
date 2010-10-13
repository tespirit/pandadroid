package com.tespirit.bamboo.primitives;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import com.tespirit.bamboo.modifiers.Modifier;
import com.tespirit.bamboo.modifiers.VertexModifier;
import com.tespirit.bamboo.render.ComponentRenderer;
import com.tespirit.bamboo.vectors.AxisAlignedBox;

public class VertexList extends Primitive implements Externalizable{
	protected VertexBuffer mVertexBuffer;
	protected List<Modifier> mModifierStack;
	protected VertexBuffer mTopBuffer;
	
	
	public VertexList(VertexBuffer vertexBuffer){
		this();
		this.mVertexBuffer = vertexBuffer;
		this.mTopBuffer = vertexBuffer;
	}
	
	public VertexList(int vertexCount, int[] vertexTypes){
		this(new VertexBuffer(vertexCount, vertexTypes));
	}
	
	/**
	 * This is just for serialization.
	 */
	public VertexList(){
		this.mModifierStack = new ArrayList<Modifier>();
		this.renderAsTriangleStrip();
	}
	
	public VertexBuffer getVertexBuffer(){
		return this.mTopBuffer;
	}

	@Override
	public void computeBoundingBox(AxisAlignedBox boundingBox) {
		this.mVertexBuffer.computeBoundingBox(boundingBox);
	}
	
	public void addModifier(Modifier vm){
		if(vm instanceof VertexModifier){
			((VertexModifier)vm).setVertexBuffer(this.mTopBuffer);
			this.mTopBuffer = ((VertexModifier)vm).getModifiedBuffer();
		}
		this.mModifierStack.add(vm);
	}

	@Override
	public void update() {
		for(Modifier m : this.mModifierStack){
			m.update();
		}
	}

	@Override
	public void init(){
		for(Modifier m : this.mModifierStack){
			m.init();
		}
	}
	
	@Override
	public void render() {
		this.mVertexBuffer.lock();
		RENDERER.render(this);
		this.mVertexBuffer.unlock();
	}
	
	private static Renderer RENDERER;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			RENDERER = this;
		}
		public abstract void render(VertexList triangles);
	}

	//IO
	private static final long serialVersionUID = 9034648912346256179L;
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		super.read(in);
		this.mVertexBuffer = (VertexBuffer)in.readObject();
		this.mTopBuffer = this.mVertexBuffer;
    	int modifiers = in.readInt();
    	for(int i = 0; i < modifiers; i++){
    		VertexModifier vm = (VertexModifier)in.readObject();
    		this.addModifier(vm);
    	}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.write(out);
		out.writeObject(this.mVertexBuffer);
		out.writeInt(this.mModifierStack.size());
		for(Modifier vm : this.mModifierStack){
			out.writeObject(vm);
		}
	}
}
