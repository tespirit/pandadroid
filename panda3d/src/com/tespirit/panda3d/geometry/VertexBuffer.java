package com.tespirit.panda3d.geometry;

import com.tespirit.panda3d.vectors.*;

public class VertexBuffer {
	private float[] positions;
	private float[] normals;
	private float[] texcoords;
	
	public VertexBuffer(int size){
		this.positions = new float[size * Vector3d.SIZE];
		this.normals = new float[size * Vector3d.SIZE];
		this.texcoords = new float[size];
	}
}
