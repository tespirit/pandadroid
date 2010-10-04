package com.tespirit.bamboo.io;

import java.util.List;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.scenegraph.Camera;
import com.tespirit.bamboo.scenegraph.Node;

public interface BambooAsset {

	public List<Camera> getCameras();
	public List<Node> getScenes();
	public List<Animation> getAnimations();
}
