package com.tespirit.bamboo.vectors;

public class Color4 {
	private float[] c;
	private int offset;
		
	public final static int SIZE = 4; //generic size info, incase this changes for some reason
	public final static int MAX_INT = 256;
	
	public static float[] createBuffer(int colorCount){
		float[] buffer = new float[Color4.SIZE*colorCount];
		for(int i = 0; i < colorCount; i++){
			buffer[3+Color4.SIZE*i] = 1.0f;
		}
		return buffer;
	}
	
	public Color4(){
		this.c = new float[Vector3d.SIZE];
		this.c[3] = 1;
		this.offset = 0;
	}
	
	public Color4(float[] values){
		this.c = values;
		this.offset = 0;
	}
	
	public Color4(float[] values, int offset){
		this.c = values;
		this.offset = offset;
	}
	
	public Color4(float r, float g, float b){
		this.c = new float[Vector3d.SIZE];
		this.c[0] = r;
		this.c[1] = g;
		this.c[2] = b;
		this.c[3] = 1.0f;
		this.offset = 0;
	}
	
	public Color4(float r, float g, float b, float a){
		this.c = new float[Vector3d.SIZE];
		this.c[0] = r;
		this.c[1] = g;
		this.c[2] = b;
		this.c[3] = a;
		this.offset = 0;
	}
	
	public Color4(int r, int g, int b, int a){
		this.c = new float[Vector3d.SIZE];
		this.c[0] = (float)r/(float)Color4.MAX_INT;
		this.c[1] = (float)g/(float)Color4.MAX_INT;
		this.c[2] = (float)b/(float)Color4.MAX_INT;
		this.c[3] = (float)a/(float)Color4.MAX_INT;
		this.offset = 0;
	}
	
	public float[] getBuffer(){
		return this.c;
	}
	
	public int getBufferOffset(){
		return this.offset;
	}
	
	public void copy(Color4 c){
		this.c[this.offset] = c.c[c.offset];
		this.c[this.offset+1] = c.c[c.offset+1];
		this.c[this.offset+2] = c.c[c.offset+2];
		this.c[this.offset+3] = c.c[c.offset+3];
	}
	
	public Color4 clone(){
		Color4 c = new Color4(this.c[this.offset], 
								this.c[this.offset+1], 
								this.c[this.offset+2],
								this.c[this.offset+3]);
		return c;
	}
	
	public void set(float r, float g, float b){
		this.c[this.offset] = r;
		this.c[this.offset+1] = g;
		this.c[this.offset+2] = b;
	}
	
	public void set(float r, float g, float b, float a){
		this.c[this.offset] = r;
		this.c[this.offset+1] = g;
		this.c[this.offset+2] = b;
		this.c[this.offset+3] = a;
	}
	
	public void set(int r, int g, int b){
		this.c[this.offset] = (float)r/(float)Color4.MAX_INT;
		this.c[this.offset+1] = (float)g/(float)Color4.MAX_INT;
		this.c[this.offset+2] = (float)b/(float)Color4.MAX_INT;
	}
	
	public void set(int r, int g, int b, int a){
		this.c[this.offset] = (float)r/(float)Color4.MAX_INT;
		this.c[this.offset+1] = (float)g/(float)Color4.MAX_INT;
		this.c[this.offset+2] = (float)b/(float)Color4.MAX_INT;
		this.c[this.offset+3] = (float)a/(float)Color4.MAX_INT;
	}
	
	public void setAt(int i, float value){
		this.c[this.offset+i] = value;
	}
	
	public void setAt(int i, int value){
		this.c[this.offset+i] = (float)value/(float)Color4.MAX_INT;
	}
	
	public void setRed(float r){
		this.c[this.offset] = r;
	}
	
	public void setGreen(float g){
		this.c[this.offset] = g;
	}
	
	public void setBlue(float b){
		this.c[this.offset+2] = b;
	}
	
	public void setAlpha(float a){
		this.c[this.offset+3] = a;
	}
	
	public float getRed(){
		return this.c[this.offset];
	}
	
	public float getGreen(){
		return this.c[this.offset+1];
	}
	
	public float getBlue(){
		return this.c[this.offset+2];
	}
	
	public float getAlpha(){
		return this.c[this.offset+3];
	}
}
