package com.tespirit.bamboo.vectors;

public class Plane {
	float d;
	Vector3d normal;
	
	/**
	 * Defaults to creating a xy plane at the origin.
	 */
	public Plane(){
		this.d = 0;
		this.normal = new Vector3d();
		this.normal.setZ(-1.0f);
		this.normal.makeDirectional();
	}
	
	public Plane(Vector3d normal){
		this.d = 0;
		this.normal = normal.clone();
		this.normal.normalize();
		this.normal.makeDirectional();
	}
	
	public Plane(Vector3d normal, Vector3d point){
		this(normal);
		this.d = -point.dot(normal);
	}
	
	public void setNormal(Vector3d normal){
		this.normal.copy(normal);
		this.normal.normalize().makeDirectional();
	}
	
	public void setPoint(Vector3d point){
		this.d = -point.dot(this.normal);
	}
	
	public void setDistance(float d){
		this.d = d;
	}
	
	/**
	 * Gets the point of intersection
	 * @param ray
	 * @return the point or null if the ray does not intersect.
	 */
	public Vector3d rayIntersectsAt(Ray ray){
		Vector3d position = ray.getPosition();
		Vector3d direction = ray.getDirection();
		
		float dirDot = direction.dot(normal);
		if(Util.floatEquals(dirDot, 0)){
			return null; //does not intersect.
		}
		
		float rayScale = (-d - position.dot(this.normal))/dirDot;
		
		Vector3d retVal = direction.clone();
		retVal.makePositional();
		retVal.scale(rayScale).add(position);
		
		return retVal;
	}
}
