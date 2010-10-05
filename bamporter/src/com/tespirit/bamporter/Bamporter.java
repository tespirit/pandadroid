package com.tespirit.bamporter;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.tespirit.bamporter.app.BamporterEditor;
import com.tespirit.bamporter.app.BamporterFrame;
import com.tespirit.bamporter.opengl.Renderer;

public class Bamporter {

	private static final String PREFERRED_LOOK_AND_FEEL = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
	
	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	/**
	 * Main entry of the class.
	 */
	public static void main(String[] args) {
		Renderer.initGl();
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BamporterEditor.getInstance().show();
			}
		});
	}
}
