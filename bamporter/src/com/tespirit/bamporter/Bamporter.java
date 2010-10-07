package com.tespirit.bamporter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.tespirit.bamporter.app.Assets;
import com.tespirit.bamporter.app.BamporterFrame;
import com.tespirit.bamporter.app.Loader;
import com.tespirit.bamporter.opengl.Renderer;

public class Bamporter {
	
	private static final String NIMBUS = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
	
	public static void setDefaultTheme(){
		try{
			UIManager.setLookAndFeel(NIMBUS);
		} catch (Exception e){
			
		}
	}

	/**
	 * Main entry of the class.
	 */
	public static void main(String[] args) {
		Renderer.initGl();
		Assets.init();
		setDefaultTheme();

		Loader.getInstance().show();
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BamporterFrame.getInstance().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				BamporterFrame.getInstance().setLocationRelativeTo(null);
				BamporterFrame.getInstance().setVisible(true);
				Loader.getInstance().close();
			}
		});
	}
}
