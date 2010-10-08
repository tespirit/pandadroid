package com.tespirit.bamporter.app;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class AboutDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5983220572539995513L;

	private Box mPanel;
	public AboutDialog(JFrame parent) {
		super(parent, "About", true);
		if (parent != null) {
			Dimension parentSize = parent.getSize(); 
			Point p = parent.getLocation(); 
			setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		}
		this.mPanel = Box.createVerticalBox();
		ImagePanel logo = Assets.openImagePanel("bamporter.png");
		this.mPanel.add(logo);
		JScrollPane scrolly = new JScrollPane();
		JTextArea text = Assets.openTextPanel("about.txt");
		text.setMinimumSize(new Dimension(logo.getWidth(), 100));
		text.setMaximumSize(new Dimension(logo.getWidth()+100, Integer.MAX_VALUE));
		scrolly.setMinimumSize(new Dimension(logo.getWidth(), 100));
		scrolly.setPreferredSize(new Dimension(logo.getWidth()+100, 150));
		scrolly.setMaximumSize(new Dimension(logo.getWidth()+100, 200));
		scrolly.setViewportView(text);
		this.mPanel.add(scrolly);
		Preferences.applySimpleBorder(this.mPanel);
		
		JButton button = new JButton("Close");
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Preferences.unapplySimpleBorder(mPanel);
				mPanel = null;
				setVisible(false);
				dispose();
			}
		});
		
		button.setAlignmentX(0.5f);
		
		this.mPanel.add(Box.createVerticalStrut(5));
		this.mPanel.add(button);
		this.mPanel.add(Box.createVerticalStrut(5));

		this.setUndecorated(true);
		this.getContentPane().add(this.mPanel);
		this.pack();
		
		this.setVisible(true);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
