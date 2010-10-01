package com.tespirit.bamboo.modifiers;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.primitives.VertexBuffer;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Vector3d;


/**
 * This is a simple skinning class. It can support up to 256 joints and 256 weights (though i recomment 2-4 weights).
 * @author Todd Espiritu Santo
 *
 */
public class SkinModifier extends VertexModifier implements Externalizable{
	private String[] mSkeleton;
	private Matrix3d[] mBindMatricesInv;
	private float[] mWeights;
	private byte[] mSkeletonMap;
	private byte[] mWeightStrides;
	
	private Vector3d mPosition;
	private Vector3d mNormal;
	private Vector3d mWeight;
	private Vector3d mPositionOriginal;
	private Vector3d mNormalOriginal;
	private Matrix3d[] mTransformMatrices;
	
	
	public SkinModifier(){
	}
	
	protected void init(){
		//create buffers!
		this.mTransformMatrices = new Matrix3d[this.mBindMatricesInv.length];
		float[] buffer = Matrix3d.createBuffer(this.mBindMatricesInv.length);
		for(int i = 0; i < this.mTransformMatrices.length; i++){
			this.mTransformMatrices[i] = new Matrix3d(buffer, Matrix3d.SIZE*i);
		}
		
		buffer = Vector3d.createBuffer(5);
		this.mPosition = new Vector3d(buffer);
		this.mPositionOriginal = new Vector3d(buffer, Vector3d.SIZE);
		this.mNormal = new Vector3d(buffer, Vector3d.SIZE*2);
		this.mNormalOriginal = new Vector3d(buffer, Vector3d.SIZE*3);
		this.mWeight = new Vector3d(buffer, Vector3d.SIZE*4);
	}

	@Override
	public void update() {
		//compute transform matrices
		for(int i = 0; i < this.mSkeleton.length; i++){
			Joint joint = (Joint)Node.getNode(this.mSkeleton[i]);
			this.mTransformMatrices[i].multiply(joint.getWorldTransform(),this.mBindMatricesInv[i]);
		}
		
		int weightIndex = 0;
		for(int i = 0; i < this.mWeightStrides.length; i++){
			float stride = this.mWeightStrides[i];
			this.mOriginalBuffer.nextVector3d(this.mPositionOriginal, VertexBuffer.POSITION);
			this.mOriginalBuffer.nextVector3d(this.mNormalOriginal, VertexBuffer.NORMAL);
			this.mNormalOriginal.makeDirectional();
			this.mPosition.set(0, 0, 0);
			this.mNormal.set(0, 0, 0);
			for(int j = 0; j < stride; j++){
				Matrix3d transform = this.mTransformMatrices[this.mSkeletonMap[weightIndex+j]];
				//compute position
				this.mWeight.copy(mPositionOriginal);
				this.mWeight.scale(this.mWeights[weightIndex+j]);
				this.mPosition.add(transform.transform(this.mWeight));
				
				//compute normal
				this.mWeight.copy(mNormalOriginal);
				this.mWeight.scale(this.mWeights[weightIndex+j]);
				this.mNormal.add(transform.transform(this.mWeight));
			}
			this.mNormal.normalize();
			this.mModifiedBuffer.addPosition(this.mPosition);
			this.mModifiedBuffer.addNormal(this.mNormal);
			weightIndex += stride;
		}
		this.mOriginalBuffer.resetBufferPosition();
		this.mModifiedBuffer.resetBufferPosition();
	}
	
	public void attachRig(String[] skeleton, Matrix3d[] bindMatricesInv){
		this.mSkeleton = skeleton;
		this.mBindMatricesInv = bindMatricesInv;
		this.init();
	}
	
	public void setWeights(float[] weights){
		this.mWeights = weights;
	}
	
	public void setSkeletonMap(byte[] skeletonMap){
		this.mSkeletonMap = skeletonMap;
	}
	
	public void setWeightStrides(byte[] weightStrides){
		this.mWeightStrides = weightStrides;
	}
	
	//IO
	private static final long serialVersionUID = -7926200357366584475L;

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.mSkeleton = (String[])in.readObject();
    	float[] buffer = (float[])in.readObject();
    	this.mBindMatricesInv = new Matrix3d[buffer.length/Matrix3d.SIZE];
    	for(int i = 0; i < this.mBindMatricesInv.length; i++){
    		this.mBindMatricesInv[i] = new Matrix3d(buffer, Matrix3d.SIZE*i);
    	}
    	this.mWeights = (float[])in.readObject();
    	this.mSkeletonMap = (byte[])in.readObject();
    	this.mWeightStrides = (byte[])in.readObject();
    	this.init();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.mSkeleton);
		out.writeObject(this.mBindMatricesInv[0].getBuffer());
		out.writeObject(this.mWeights);
		out.writeObject(this.mSkeletonMap);
		out.writeObject(this.mWeightStrides);
	}
}
