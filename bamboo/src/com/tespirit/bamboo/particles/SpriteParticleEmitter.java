package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.creation.Primitives;
import com.tespirit.bamboo.render.SpriteNode;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.surfaces.Surface;
import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Vector3d;

public class SpriteParticleEmitter extends SpriteNode implements ParticleEmitter{
	private Matrix3d mTransform;
	private Matrix3d mWorldTransform;
	
	private Surface mSurface;
	private ParticleGenerator mGenerator;
	private ParticleSystem mParticles;
	
	protected class SpriteParticle extends StandardParticle{
		private Primitives.Plane mSprite;
		
		protected SpriteParticle(){
			this.mSprite = new Primitives.Plane();
		}

		@Override
		public void update(Vector3d position, float scale){
			this.mSprite.setCenter(position);
			this.mSprite.setSize(scale, scale);
			this.mSprite.update();
		}
		
		@Override
		public void render(){
			this.mSprite.render();
		}
	}
	
	public SpriteParticleEmitter(ParticleGenerator generator){
		this(generator, new ParticleSystemList());
	}
	
	public SpriteParticleEmitter(ParticleGenerator generator, ParticleSystem particles){
		this.mParticles = new ParticleSystemList();
		this.mGenerator = generator;
		this.mSurface = Surface.getDefaultSurface();
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
		this.mSurface.render();
		this.mParticles.render();
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
	public Particle createParticle() {
		SpriteParticle p = new SpriteParticle();
		this.mParticles.add(p);
		return p;
	}
}
