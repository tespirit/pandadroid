package com.tespirit.bamporter.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.tespirit.bamboo.particles.ConstantForce;
import com.tespirit.bamboo.particles.ConstantGravityForce;
import com.tespirit.bamboo.particles.GravityForce;
import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamboo.particles.StandardParticleSystem;
import com.tespirit.bamporter.properties.SimplePanel;

public class ParticleSystemEditor extends TreeNodeEditor{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4329218305403186171L;
	private StandardParticleSystem mParticleSystem;
	
	public ParticleSystemEditor(StandardParticleSystem particleSystem){
		this.mParticleSystem = particleSystem;
	}

	@Override
	protected Component generatePanel() {
		SimplePanel panel = new SimplePanel();
		panel.createButton("New Constant Gravity").addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				addForce(new ConstantGravityForce());
			}
		});
		panel.createButton("New Gravity").addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				addForce(new GravityForce());
			}
		});
		panel.createButton("New Constant Force").addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				addForce(new ConstantForce());
			}
		});
		return panel;
	}
	
	public void addForce(ParticleForce force){
		this.mParticleSystem.addForce(force);
		this.addEditor(new ParticleForceEditor(force));
	}
	
	@Override
	public void removeEditor(TreeNodeEditor node){
		super.removeEditor(node);
		if(node instanceof ParticleForceEditor){
			this.mParticleSystem.removeForce(((ParticleForceEditor)node).getForce());
		}
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mParticleSystem);
	}

}
