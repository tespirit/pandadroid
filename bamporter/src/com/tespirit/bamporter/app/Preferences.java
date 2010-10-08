package com.tespirit.bamporter.app;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class Preferences implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3846220277697146862L;
	private static Preferences mPrefs;
	private static final String FILE = "preferences";
	private static Set<JComponent> mSimpleBorderComponents = new HashSet<JComponent>();
	private static HashMap<String, String> mLookAndFeels = new HashMap<String, String>();
	private static final String DEFAULT_LAF = "Nimbus";
	
	
	private String mLookAndFeel;
	private Border mSimpleBorder;
	private Color mRenderBGColor;
	private String mOpenDirectory;
	private String mSaveDirectory;
	
	public static void init(){
		mLookAndFeels.put("Nimbus", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		mLookAndFeels.put("Metal", "javax.swing.plaf.metal.MetalLookAndFeel");
		mLookAndFeels.put("System", "system");
		mLookAndFeels.put("Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		mLookAndFeels.put("GTK", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		
		Preferences.openTheme();
		Preferences.setLookAndFeel();
	}
	
	public static String[] getLookAndFeels(){
		String[] laf = new String[Preferences.mLookAndFeels.size()];
		return Preferences.mLookAndFeels.keySet().toArray(laf);
	}
	
	private static void refreshTheme(JFrame frame){
		Preferences.setLookAndFeel();
		SwingUtilities.updateComponentTreeUI(frame);
	}
	
	private static void setLookAndFeel(){
		String val = Preferences.mLookAndFeels.get(Preferences.mPrefs.mLookAndFeel);
		if(val == null){
			val = Preferences.mLookAndFeels.get(DEFAULT_LAF);
		}
		if(val == "system"){
			val = UIManager.getSystemLookAndFeelClassName();
		}
		try{
		UIManager.setLookAndFeel(val);
		} catch (Exception e){
			//VOID;
		}
	}
	
	private static void save(){
		FileOutputStream stream = null;
		try{
			stream = new FileOutputStream(new File(FILE));
			ObjectOutputStream ostream = new ObjectOutputStream(stream);
			ostream.writeObject(Preferences.mPrefs);
		} catch(Exception e){
			//VOID
		} finally {
			if(stream != null){
				try{
					stream.close();
				} catch (Exception e){
					//VOID
				}
			}
		}
	}
	
	private static void openTheme(){
		File file = new File(FILE);
		if(file.exists()){
			FileInputStream stream = null;
			try{
				stream = new FileInputStream(file);
				ObjectInputStream ostream = new ObjectInputStream(stream);
				Preferences.mPrefs = (Preferences)ostream.readObject();
			} catch (Exception e) {
				//VOID
			} finally {
				if(stream != null){
					try{
						stream.close();
					} catch (Exception e){
						//VOID
					}
					if(Preferences.mPrefs == null){
						file.delete();
					}
				}	
			}
		}
		if(Preferences.mPrefs == null){
			Preferences.mPrefs = new Preferences();
		}
	}
	
	public Preferences() {
		this.mLookAndFeel = DEFAULT_LAF;
		this.mRenderBGColor = new Color(0xffffffff);
		this.mSimpleBorder = BorderFactory.createLineBorder(new Color(0xff888888), 1);
	}
	
	public static void applySimpleBorder(JComponent component){
		component.setBorder(Preferences.mPrefs.mSimpleBorder);
		Preferences.mSimpleBorderComponents.add(component);
	}
	
	public static void unapplySimpleBorder(JComponent component){
		Preferences.mSimpleBorderComponents.remove(component);
	}
	/*
	private static void setSimpleBorder(Border simpleBorder){
		Preferences.mPrefs.mSimpleBorder = simpleBorder;
		for(JComponent component : Preferences.mSimpleBorderComponents){
			component.setBorder(simpleBorder);
		}
	}
	*/
	public static Color getRenderBGColor(){
		return Preferences.mPrefs.mRenderBGColor;
	}
	
	public static void setRenderBGColor(Color rbgc){
		if(!Preferences.mPrefs.mRenderBGColor.equals(rbgc)){
			Preferences.mPrefs.mRenderBGColor = rbgc;
			Preferences.save();
		}
	}
	
	public static String getLookAndFeel(){
		return Preferences.mPrefs.mLookAndFeel;
	}
	
	public static void setLookAndFeel(String lookAndFeel, JFrame frame){
		if(Preferences.mPrefs.mLookAndFeel != lookAndFeel){
			Preferences.mPrefs.mLookAndFeel = lookAndFeel;
			Preferences.refreshTheme(frame);
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
