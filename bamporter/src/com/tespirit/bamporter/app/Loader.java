package com.tespirit.bamporter.app;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Loader{
	
	private static Loader mLoader;
	
	private static final String SPLASH_IMAGE = "bamporter.png";
	
	public static Loader getInstance(){
		if(mLoader == null){
			mLoader = new Loader();
		}
		return mLoader;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8234262741882676492L;
	private JFrame mFrame;
	private Box mPanel;

	private Loader(){
		this.mFrame = new JFrame();
		this.mFrame.setUndecorated(true);
		
		this.mPanel = Box.createVerticalBox();
		
		ImagePanel imagePanel = Assets.openImagePanel(SPLASH_IMAGE);
		JLabel loading = new JLabel("Loading...");
		loading.setAlignmentX(0.5f);
		this.mPanel.add(imagePanel);
		this.mPanel.add(loading);
		this.mPanel.add(Box.createVerticalStrut(5));
		Preferences.applySimpleBorder(this.mPanel);
		Preferences.unapplySimpleBorder(this.mPanel);//remove this from the updator.
		this.mFrame.getContentPane().add(this.mPanel);
		this.mFrame.pack();
		this.mFrame.setLocationRelativeTo(null);
	}
	
	public void show(){
		this.mFrame.setVisible(true);
	}
	
	public void close(){
		this.mFrame.setVisible(false);
		this.mFrame.dispose();
		mLoader = null;
	}
}
