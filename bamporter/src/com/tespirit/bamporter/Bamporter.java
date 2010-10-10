package com.tespirit.bamporter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.tespirit.bamporter.app.Assets;
import com.tespirit.bamporter.app.BamporterFrame;
import com.tespirit.bamporter.app.Loader;
import com.tespirit.bamporter.app.Preferences;
import com.tespirit.bamporter.opengl.Renderer;

public class Bamporter {
	


	/**
	 * Main entry of the class.
	 */
	public static void main(String[] args) {
		Renderer.initGl();
		Assets.init();
		Preferences.init();

		Loader.getInstance().show();
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Preferences.load();
				BamporterFrame.getInstance().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				BamporterFrame.getInstance().setLocationRelativeTo(null);
				BamporterFrame.getInstance().setVisible(true);
				Loader.getInstance().close();
			}
		});
	}
}
