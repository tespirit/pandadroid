package com.tespirit.bamporter.app;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 480615870929979652L;
	
	private ImageIcon mImage;
	
	public ImagePanel(ImageIcon image){
		this.mImage = image;
		
		Dimension size = new Dimension(this.mImage.getIconWidth(), this.mImage.getIconHeight());
		
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.setSize(size);
		this.setLayout(null);
	}
	
	@Override
	public int getWidth(){
		return this.mImage.getIconWidth();
	}
	
	@Override
	public int getHeight(){
		return this.mImage.getIconHeight();
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    g.drawImage(mImage.getImage(), 0, 0, null);
	}

}
