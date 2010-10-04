package com.tespirit.bamboo.render;

public interface Updater {
	public void update();
	//this allows for an updater to have control over dispatching updaters. 
	//whenever it needs to. Updaters that run constantly don't really need
	//an implementation for this, unless they need it to dispatch other updaters.
	public void setUpdateManager(UpdateManager updateManager);
}
