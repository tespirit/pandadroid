package com.tespirit.bamboo.io;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.render.Camera;
import com.tespirit.bamboo.render.LightGroup;
import com.tespirit.bamboo.scenegraph.Node;

public interface BambooAsset {
	public Node getSceneGraph();
	public LightGroup getLightGroup();
	public Camera[] getCameras();
	public Animation getAnimation();
}
