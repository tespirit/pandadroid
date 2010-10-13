package com.tespirit.bamporter.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.tespirit.bamboo.particles.ConstantForce;
import com.tespirit.bamboo.particles.ConstantGravityForce;
import com.tespirit.bamboo.particles.GravityForce;
import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamporter.properties.FloatProperty;
import com.tespirit.bamporter.properties.SimplePanel;

public class ParticleForceEditor extends TreeNodeEditor{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6889881888993284106L;
	private ParticleForce mForce;
	
	public ParticleForceEditor(ParticleForce force){
		this.mForce = force;
	}
	
	public ParticleForce getForce(){
		return this.mForce;
	}

	@Override
	protected Component generatePanel() {
		SimplePanel panel = null;
		if(this.mForce instanceof GravityForce){
			panel = generatePanel((GravityForce)this.mForce);
		} else if(this.mForce instanceof ConstantGravityForce){
			panel = generatePanel((ConstantGravityForce)this.mForce);
		} else if(this.mForce instanceof ConstantForce){
			panel = generatePanel((ConstantForce)this.mForce);
		}
		
		panel.createButton("Delete").addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				removeEditorFromParent();
				recycle();
			}
		});
		return panel;
	}
	
	private SimplePanel generatePanel(GravityForce force){
		SimplePanel panel = new SimplePanel();
		panel.addProperty("Position", force.getPosition(), 1f);
		panel.addProperty("Strength", new FloatProperty.Property() {
			@Override
			public void setValue(float value) {
				((GravityForce)mForce).setStrength(value);
			}
			@Override
			public float getValue() {
				return 	((GravityForce)mForce).getStrength();
			}
		}, -Float.MAX_VALUE, Float.MAX_VALUE, 0.1f);
		return panel;
	}
	
	private SimplePanel generatePanel(ConstantGravityForce force){
		SimplePanel panel = new SimplePanel();
		panel.addProperty("Acceleration", force.getAcceleration(), 0.1f);
		return panel;
	}
	
	private SimplePanel generatePanel(ConstantForce force){
		SimplePanel panel = new SimplePanel();
		panel.addProperty("Force", force.getForce(), 0.1f);
		return panel;
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mForce);
	}
}
