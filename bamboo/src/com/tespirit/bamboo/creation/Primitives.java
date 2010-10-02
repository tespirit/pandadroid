package com.tespirit.bamboo.creation;

import com.tespirit.bamboo.primitives.VertexBuffer;
import com.tespirit.bamboo.primitives.VertexIndices;
import com.tespirit.bamboo.primitives.VertexList;
import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Vector3d;

public class Primitives {
	
	public static class Plane extends VertexIndices{
		public Plane(){
			super(6, 4, new int[]{VertexBuffer.POSITION, VertexBuffer.NORMAL, VertexBuffer.TEXCOORD});
			this.mVertexBuffer.lock();
			
			this.mVertexBuffer.addPosition(-0.5f,  0.5f, 0.0f);
			this.mVertexBuffer.addPosition(-0.5f, -0.5f, 0.0f);
			this.mVertexBuffer.addPosition(0.5f, -0.5f, 0.0f);
			this.mVertexBuffer.addPosition(0.5f,  0.5f, 0.0f);
			
			this.mVertexBuffer.addNormal(0.0f, 0.0f, 1.0f);
			this.mVertexBuffer.addNormal(0.0f, 0.0f, 1.0f);
			this.mVertexBuffer.addNormal(0.0f, 0.0f, 1.0f);
			this.mVertexBuffer.addNormal(0.0f, 0.0f, 1.0f);
			
			this.mVertexBuffer.addTexcoord(0.0f, 0.0f);
			this.mVertexBuffer.addTexcoord(0.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 0.0f);
			
			this.mVertexBuffer.unlock();
			
			this.mIndexBuffer.lock();
			
			this.mIndexBuffer.addTriangle(0, 1, 2);
			this.mIndexBuffer.addTriangle(0, 2, 3);
			
			this.mIndexBuffer.unlock();
		}
	}
	
	public static class Cube extends VertexIndices {
		public Cube(AxisAlignedBox box){
			this(box.getMin(), box.getMax());
		}
		
		public Cube(Vector3d min, Vector3d max){
			super(12*3, 24, new int[]{VertexBuffer.POSITION, VertexBuffer.NORMAL, VertexBuffer.TEXCOORD});
		
			this.mVertexBuffer.lock();
			
			this.mVertexBuffer.addNormal(0, -1, 0);
			this.mVertexBuffer.addNormal(0, -1, 0);
			this.mVertexBuffer.addNormal(0, -1, 0);
			this.mVertexBuffer.addNormal(0, -1, 0);
			
			this.mVertexBuffer.addTexcoord(0.0f, 0.0f);
			this.mVertexBuffer.addTexcoord(0.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 0.0f);
			
			this.mVertexBuffer.addNormal(0, 1, 0);
			this.mVertexBuffer.addNormal(0, 1, 0);
			this.mVertexBuffer.addNormal(0, 1, 0);
			this.mVertexBuffer.addNormal(0, 1, 0);
			
			this.mVertexBuffer.addTexcoord(0.0f, 0.0f);
			this.mVertexBuffer.addTexcoord(0.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 0.0f);
			
			this.mVertexBuffer.addNormal(0, 0, 1);
			this.mVertexBuffer.addNormal(0, 0, 1);
			this.mVertexBuffer.addNormal(0, 0, 1);
			this.mVertexBuffer.addNormal(0, 0, 1);
			
			this.mVertexBuffer.addTexcoord(0.0f, 0.0f);
			this.mVertexBuffer.addTexcoord(0.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 0.0f);
		
			this.mVertexBuffer.addNormal(0, 0, -1);
			this.mVertexBuffer.addNormal(0, 0, -1);
			this.mVertexBuffer.addNormal(0, 0, -1);
			this.mVertexBuffer.addNormal(0, 0, -1);
			
			this.mVertexBuffer.addTexcoord(0.0f, 0.0f);
			this.mVertexBuffer.addTexcoord(0.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 0.0f);

			this.mVertexBuffer.addNormal(1, 0, 0);
			this.mVertexBuffer.addNormal(1, 0, 0);
			this.mVertexBuffer.addNormal(1, 0, 0);
			this.mVertexBuffer.addNormal(1, 0, 0);
			
			this.mVertexBuffer.addTexcoord(0.0f, 0.0f);
			this.mVertexBuffer.addTexcoord(0.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 0.0f);
			
			this.mVertexBuffer.addNormal(-1, 0, 0);
			this.mVertexBuffer.addNormal(-1, 0, 0);
			this.mVertexBuffer.addNormal(-1, 0, 0);
			this.mVertexBuffer.addNormal(-1, 0, 0);
			
			this.mVertexBuffer.addTexcoord(0.0f, 0.0f);
			this.mVertexBuffer.addTexcoord(0.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 1.0f);
			this.mVertexBuffer.addTexcoord(1.0f, 0.0f);
			
			this.mVertexBuffer.unlock();
			
			this.setBox(min, max);
			
			this.mIndexBuffer.lock();

			this.mIndexBuffer.addTriangle(0, 1, 2);
			this.mIndexBuffer.addTriangle(0, 2, 3);
			
			this.mIndexBuffer.addTriangle(4, 5, 6);
			this.mIndexBuffer.addTriangle(4, 6, 7);
			
			this.mIndexBuffer.addTriangle(8, 9, 10);
			this.mIndexBuffer.addTriangle(8, 10, 11);
			
			this.mIndexBuffer.addTriangle(12, 13, 14);
			this.mIndexBuffer.addTriangle(12, 14, 15);
			
			this.mIndexBuffer.addTriangle(16, 18, 17);
			this.mIndexBuffer.addTriangle(16, 19, 18);
			
			this.mIndexBuffer.addTriangle(20, 22, 21);
			this.mIndexBuffer.addTriangle(20, 23, 22);
			
			this.mIndexBuffer.unlock();
		}
		
		public Cube(){
			this(new AxisAlignedBox(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f));
		}
		
		public void setBox(Vector3d min, Vector3d max){
			this.mVertexBuffer.lock();
			
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
			
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
			
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
			
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
			
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
			
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
			
			this.mVertexBuffer.unlock();
		}
		
		public void setBox(AxisAlignedBox box){
			this.setBox(box.getMin(), box.getMax());
		}
	}
	
	public static class WireCube extends VertexList{

		public WireCube(){
			this(new Vector3d(-0.5f, -0.5f, -0.5f), new Vector3d(0.5f, 0.5f, 0.5f));
		}
		
		public WireCube(AxisAlignedBox box){
			this(box.getMin(), box.getMax());
		}
		
		public WireCube(Vector3d min, Vector3d max) {
			super(24, new int[]{VertexBuffer.POSITION});
			this.setBox(min, max);
			this.renderAsLines();
		}
		
		public void setBox(AxisAlignedBox box){
			this.setBox(box.getMin(), box.getMax());
		}
		
		public void setBox(Vector3d min, Vector3d max){
			this.mVertexBuffer.lock();
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
			
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
			
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
			
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
			
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
			
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
			
			
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
			
			this.mVertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
			
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
			
			this.mVertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
			
			
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
			this.mVertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
			
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
			this.mVertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
			
			this.mVertexBuffer.unlock();
		}
	}
	
	public static class Axis extends VertexList{
		public Axis() {
			super(6, new int[]{VertexBuffer.POSITION, VertexBuffer.COLOR});
			
			this.mVertexBuffer.lock();
			
			this.mVertexBuffer.addPosition(0, 0, 0);
			this.mVertexBuffer.addPosition(1, 0, 0);
			
			this.mVertexBuffer.addColor(0.5f, 0.5f, 0.5f);
			this.mVertexBuffer.addColor(1, 0, 0);
			
			this.mVertexBuffer.addPosition(0, 0, 0);
			this.mVertexBuffer.addPosition(0, 1, 0);
			
			this.mVertexBuffer.addColor(0.5f, 0.5f, 0.5f);
			this.mVertexBuffer.addColor(0, 1, 0);
			
			this.mVertexBuffer.addPosition(0, 0, 0);
			this.mVertexBuffer.addPosition(0, 0, 1);
			
			this.mVertexBuffer.addColor(0.5f, 0.5f, 0.5f);
			this.mVertexBuffer.addColor(0, 0, 1);
			
			this.mVertexBuffer.unlock();
			
			this.renderAsLineStrip();
		}
	}
}
