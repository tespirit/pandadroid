package com.tespirit.bamboo.particles;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.creation.Primitives;
import com.tespirit.bamboo.render.SpriteNode;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.surfaces.Surface;
import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Vector3d;

public class SpriteParticleEmitter extends SpriteNode implements ParticleEmitter, Externalizable{
	private Matrix3d mTransform;
	private Matrix3d mWorldTransform;
	
	private Surface mSurface;
	private ParticleGenerator mGenerator;
	private ParticleSystem mParticles;
	
	protected class SpriteParticle extends StandardParticle{
		private Primitives.Rectangle mSprite;
		private Vector3d mFinalPosition;
		
		protected SpriteParticle(){
			this.mSprite = new Primitives.Rectangle();
			this.mFinalPosition = new Vector3d();
		}

		@Override
		public void update(Vector3d position, float scale){
			getRenderManager().getCamera().getWorldTransform().transform(position, this.mFinalPosition);
			this.mSprite.setCenter(this.mFinalPosition);
			this.mSprite.setSize(scale, scale);
			this.mSprite.update();
		}
		
		@Override
		public void render(){
			this.mSprite.render();
		}
	}
	
	@Override
	public boolean alphaSort() {
		return this.mSurface.hasAlpha();
	}
	
	public Surface getSurface(){
		return this.mSurface;
	}
	
	public void setSurface(Surface surface){
		if(surface != null)
			this.mSurface = surface;
		else
			this.mSurface = Surface.getDefaultSurface();
		this.registerDynamicLoader(this.mSurface);
	}
	
	public SpriteParticleEmitter(ParticleGenerator generator){
		this(generator, new StandardParticleSystem());
	}
	
	public SpriteParticleEmitter(ParticleGenerator generator, ParticleSystem particles){
		this();
		this.mParticles = new StandardParticleSystem();
		this.mGenerator = generator;
		this.mSurface = Surface.getDefaultSurface();
	}
	
	public SpriteParticleEmitter(){
		super(null);

		float[] m = Matrix3d.createBuffer(2);
		this.mTransform = new Matrix3d(m);
		this.mWorldTransform = new Matrix3d(m, Matrix3d.SIZE);
	}
	
	@Override
	public void update(Matrix3d transform){
		super.update(transform);
		float deltaTime = this.getRenderManager().getClock().getDeltaTime()/1000f;
		this.mGenerator.update(this, deltaTime);
		this.mParticles.update(deltaTime);
	}

	@Override
	public void render() {
		this.mSurface.renderStart();
		this.mParticles.render();
		this.mSurface.renderEnd();
	}

	@Override
	public Node getChild(int i) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public Matrix3d getTransform() {
		return this.mTransform;
	}

	@Override
	public Matrix3d getWorldTransform() {
		return this.mWorldTransform;
	}

	@Override
	public AxisAlignedBox getBoundingBox() {
		return null;
	}

	@Override
	protected void recycleInternal() {
		this.mParticles.recycle();
		this.mParticles = null;
		this.mGenerator = null;
		this.mSurface = null;
		this.mTransform = null;
		this.mWorldTransform = null;
	}

	@Override
	public ParticleSystem getParticleSysetm() {
		return this.mParticles;
	}
	
	@Override
	public ParticleGenerator getParticleGenerator(){
		return this.mGenerator;
	}

	@Override
	public Particle createParticle() {
		SpriteParticle p = new SpriteParticle();
		this.mParticles.add(p);
		return p;
	}

	//IO
	private static final long serialVersionUID = -8460916180368530654L;
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.write(out);
		for(int i = 0; i < Matrix3d.SIZE; i++){
			out.writeFloat(this.mTransform.getValue(i));
		}
		out.writeObject(this.mSurface);
		out.writeObject(this.mGenerator);
		out.writeObject(this.mParticles);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		super.read(in);
		for(int i = 0; i < Matrix3d.SIZE; i++){
    		this.mTransform.setValue(in.readFloat(), i);
    	}
		this.mSurface = (Surface)in.readObject();
		this.mGenerator = (ParticleGenerator)in.readObject();
		this.mParticles = (ParticleSystem)in.readObject();
	}
}
