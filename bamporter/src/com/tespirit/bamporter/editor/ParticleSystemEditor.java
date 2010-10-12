package com.tespirit.bamporter.editor;

import java.awt.Component;

import com.tespirit.bamboo.particles.ConstantForceParticleSystem;
import com.tespirit.bamporter.properties.SimplePanel;

public class ParticleSystemEditor extends TreeNodeEditor{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4329218305403186171L;
	private ConstantForceParticleSystem mParticleSystem;
	
	public ParticleSystemEditor(ConstantForceParticleSystem particleSystem){
		this.mParticleSystem = particleSystem;
	}

	@Override
	protected Component generatePanel() {
		SimplePanel panel = new SimplePanel();
		panel.addProperty("Force", this.mParticleSystem.getForce(),0.5f);
		return panel;
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mParticleSystem);
	}

}
