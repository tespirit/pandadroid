package com.tespirit.bamporter.editor;

import com.tespirit.bamboo.particles.ParticleForce;

public abstract class ParticleForceEditor implements Factory {
	public abstract ParticleForce createForce();
	
	public abstract class Editor extends PropertyTreeNodeEditor{
		protected Editor(Object data, boolean allowChildren) {
			super(data, allowChildren);
		}
		
		protected Editor(Object data){
			super(data);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 51237675564085814L;

		public ParticleForce getForce(){
			return (ParticleForce)this.userObject;
		}
	}
}
