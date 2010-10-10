package com.tespirit.bamporter.app;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class Preferences implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3846220277697146862L;
	transient private static Preferences mPrefs;
	transient private static final String ID = "preferences";
	transient private static Set<JComponent> mSimpleBorderComponents = new HashSet<JComponent>();
	transient private static HashMap<String, Theme> mLookAndFeels = new HashMap<String, Theme>();
	transient private static final String DEFAULT_THEME = "Pretty";
	
	private Color mRenderBGColor;
	private String mOpenDirectory;
	private String mSaveDirectory;
	private String mThemeName;
	transient private Theme mTheme; 
	
	public static void init(){
		Border simpleBorder = BorderFactory.createLineBorder(new Color(0xff888888), 1);
		addTheme(new Theme("Pretty", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel", simpleBorder));
		addTheme(new Theme("Ugly", "com.sun.java.swing.plaf.motif.MotifLookAndFeel", simpleBorder));
		addTheme(new Theme("Flakey OS", UIManager.getSystemLookAndFeelClassName(), simpleBorder));
		addTheme(new Theme("Java-y", "javax.swing.plaf.metal.MetalLookAndFeel", simpleBorder));
		addTheme(new Theme("Linux-y", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel", simpleBorder));
		Preferences.mPrefs = new Preferences();
		Preferences.applyTheme();
		Preferences.applyLookAndFeel();
	}
	
	public static void addTheme(Theme theme){
		mLookAndFeels.put(theme.mName, theme);
	}
	
	private static class Theme implements Serializable{
		private static final long serialVersionUID = -4901932410829285428L;
		private String mName;
		private String mLookAndFeelId;
		private Border mSimpleBorder;
		
		private Theme(String name, String lookAndFeelId, Border simpleBorder){
			this.mName = name;
			this.mLookAndFeelId = lookAndFeelId;
			this.mSimpleBorder = simpleBorder;
		}
	}
	
	public static String[] getLookAndFeels(){
		String[] laf = new String[Preferences.mLookAndFeels.size()];
		return Preferences.mLookAndFeels.keySet().toArray(laf);
	}
	
	public static void refreshComponent(Component component){
		SwingUtilities.updateComponentTreeUI(component);
	}
	
	private static boolean applyLookAndFeel(){
		try{
			UIManager.setLookAndFeel(Preferences.mPrefs.mTheme.mLookAndFeelId);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	private static void save(){
		Assets.saveUserObject(Preferences.mPrefs, Preferences.ID);
	}
	
	public static void load(){
		Object obj = Assets.openUserObject(Preferences.ID);
		if(obj instanceof Preferences){
			Preferences.mPrefs = (Preferences)obj;
		} else {
			Preferences.mPrefs = new Preferences();
		}
		Preferences.applyTheme();
		Preferences.applyLookAndFeel();
	}
	
	public Preferences() {
		this.mThemeName = Preferences.DEFAULT_THEME;
		this.mRenderBGColor = new Color(0xffffffff);
	}
	
	public static void applySimpleBorder(JComponent component){
		component.setBorder(Preferences.mPrefs.mTheme.mSimpleBorder);
		Preferences.mSimpleBorderComponents.add(component);
	}
	
	public static void unapplySimpleBorder(JComponent component){
		Preferences.mSimpleBorderComponents.remove(component);
	}
	
	public static Color getRenderBGColor(){
		return Preferences.mPrefs.mRenderBGColor;
	}
	
	public static void setRenderBGColor(Color rbgc){
		if(!Preferences.mPrefs.mRenderBGColor.equals(rbgc)){
			Preferences.mPrefs.mRenderBGColor = rbgc;
			Preferences.save();
		}
	}
	
	public static String getTheme(){
		return Preferences.mPrefs.mTheme.mName;
	}
	
	private static void applyTheme(){
		if(Preferences.mLookAndFeels.containsKey(Preferences.mPrefs.mThemeName)){
			Preferences.mPrefs.mTheme = Preferences.mLookAndFeels.get(Preferences.mPrefs.mThemeName);
		} else {
			Preferences.mPrefs.mTheme = Preferences.mLookAndFeels.get(Preferences.DEFAULT_THEME);
		}
	}
	
	public static void setTheme(String theme){
		if(Preferences.mPrefs.mThemeName != theme){
			Preferences.mPrefs.mThemeName = theme;
			Preferences.applyTheme();
			Preferences.applyLookAndFeel();
			Preferences.save();
		}
	}
	
	public static File getOpenDirectory(){
		if(Preferences.mPrefs.mOpenDirectory == null){
			return new File(System.getProperty("user.home"));
		}
		File file = new File(Preferences.mPrefs.mOpenDirectory);
		if(!file.isDirectory()){
			return new File(System.getProperty("user.home"));
		}
		return file;
	}
	
	/**
	 * 
	 * @param dir Can be a file path or directory. if it is a file, then
	 * the parent directory is used.
	 */
	public static void setOpenDirectory(File dir){
		try	{
			if(dir.isFile()){
				dir = new File(dir.getParent());
			} 
			String path = dir.getCanonicalPath();
			if(Preferences.mPrefs.mOpenDirectory != path){
				Preferences.mPrefs.mOpenDirectory = path;
				Preferences.save();
			}
		} catch(Exception e){
			
		}
	}
	
	public static File getSaveDirectory(){
		if(Preferences.mPrefs.mSaveDirectory == null){
			
			return new File(System.getProperty("user.home"));
		}
		File file = new File(Preferences.mPrefs.mSaveDirectory);
		if(!file.isDirectory()){
			return new File(System.getProperty("user.home"));
		}
		return file;
	}
	
	/**
	 * 
	 * @param dir Can be a file path or directory. if it is a file, then
	 * the parent directory is used.
	 */
	public static void setSaveDirectory(File dir){
		try	{
			if(dir.isFile()){
				dir = new File(dir.getParent());
			} 
			String path = dir.getCanonicalPath();
			if(Preferences.mPrefs.mSaveDirectory != path){
				Preferences.mPrefs.mSaveDirectory = path;
				Preferences.save();
			}
		} catch(Exception e){
			
		}
	}
}
