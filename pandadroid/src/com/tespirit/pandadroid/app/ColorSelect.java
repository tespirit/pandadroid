package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.scenegraph.Model;
import com.tespirit.bamboo.surfaces.Surface;

/**
 * A simple 2d controller changes the material of a selected object into 
 * another material until it is deselected.
 * @author Todd Espiritu Santo
 *
 */
public class ColorSelect extends SelectController2d{
	Model current;
	Surface currentSurface;
	Surface pickSurface;

	public ColorSelect(PandadroidView view, Surface pickSurface) {
		super(view);
		this.pickSurface = pickSurface;
	}
	
	public void setPickSurface(Surface pickSurface){
		this.pickSurface = pickSurface;
		if(this.current != null){
			this.current.setSurface(this.pickSurface);
		}
	}

	@Override
	public void select(Model model) {
		if(this.current != null){
			this.current.setSurface(this.currentSurface);
		}
		if(model != null){
			this.currentSurface = model.getSurface();
			model.setSurface(this.pickSurface);
		} 

		this.current = model;
	}

	@Override
	public void deselect() {
		if(this.current != null){
			this.current.setSurface(this.currentSurface);
		}
		this.current = null;
		this.currentSurface = null;
	}
}
