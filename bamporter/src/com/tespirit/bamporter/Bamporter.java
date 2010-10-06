package com.tespirit.bamporter;

import javax.swing.SwingUtilities;

import com.tespirit.bamporter.app.Assets;
import com.tespirit.bamporter.app.BamporterFrame;
import com.tespirit.bamporter.opengl.Renderer;

public class Bamporter {

	/**
	 * Main entry of the class.
	 */
	public static void main(String[] args) {
		Renderer.initGl();
		Assets.init();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BamporterFrame.getInstance().setVisible(true);
			}
		});
	}
}
