package com.tespirit.bamboo.io;

import java.util.List;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.render.Camera;
import com.tespirit.bamboo.scenegraph.Node;

public interface BambooAsset {

	public List<Camera> getRootCameras();
	public List<Node> getRootSceneNodes();
	public List<Animation> getAnimations();
}
