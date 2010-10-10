package com.tespirit.bamporter.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GLProfile;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.tespirit.bamboo.io.Bamboo;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamporter.io.BambooHandler;
import com.tespirit.bamporter.io.ColladaHandler;
import com.tespirit.bamporter.io.FileHandler;
import com.tespirit.bamporter.editor.Util;

public class Assets {
	
	private static Map<String, File> mAbsolutePaths = new HashMap<String, File>();
	private static List<File> mPaths = new ArrayList<File>();
	private static String mLocalPath;
	private static GLProfile mGlProfile;
	
	private static Map<String, FileHandler> mFileHandlers = new HashMap<String, FileHandler>();
	private static List<FileFilter> mFilters = new ArrayList<FileFilter>();
	
	private static String ASSET_DIR;
	private static String HOME_DIR;
	private static String APP_DIR;
	private static String USER_DATA_DIR;
	
	public static void init(){
		BambooHandler.init();
		ColladaHandler.init();
		try{
			Assets.APP_DIR = new File(".").getCanonicalPath();
		} catch (Exception e){
			e.printStackTrace();
			Assets.APP_DIR = new File(".").getAbsolutePath();
		}
		Assets.ASSET_DIR = new File(Assets.APP_DIR,"assets").getPath();
		Assets.HOME_DIR = System.getProperty("user.home");
		Assets.USER_DATA_DIR = new File(Assets.HOME_DIR, "bamporter").getPath();
		if(new File(Assets.USER_DATA_DIR).isFile()){
			Util.alertError("Preferences cannot be saved because a file is named the same as the preference folder: " + Assets.USER_DATA_DIR);
		}
	}
	
	/**
	 * returns null if the file could not be opened.
	 * @param fileName
	 * @return
	 */
	public static Object openUserObject(String id) {
		Object object = null;
		File file = new File(Assets.USER_DATA_DIR, id);
		if(file.isFile()){
			FileInputStream stream;
			try{
				stream = new FileInputStream(file);
				object = new ObjectInputStream(stream).readObject();
			} catch(Exception e){
				return null;
			}
			try{
			} catch(Exception e){
				//VOID
			}
			if(stream != null){
				try{
					stream.close();
				} catch(Exception ex){
					//VOID
				}
				stream = null;
			}
		}
		return object;
	}
	
	public static void deleteUserObject(String id){
		File file = new File(id);
		if(file.isFile()){
			try{
				file.delete();
			} catch(Exception e){
				//VOID
			}
		}
	}
	
	public static void saveUserObject(Object data, String id){
		File userData = new File(Assets.USER_DATA_DIR);
		if(!userData.exists()){
			userData.mkdir();
		} else if(userData.isFile()){
			return;
		}
		File file = new File(Assets.USER_DATA_DIR, id);
		FileOutputStream stream = null;
		try{
			stream = new FileOutputStream(file);
			ObjectOutputStream ostream = new ObjectOutputStream(stream);
			ostream.writeObject(data);
		} catch(Exception e){
			//VOID
		} 
		
		if(stream != null){
			try{
				stream.close();
			} catch (Exception e){
				//VOID
			}
		}
	}
	
	public static String getHomeDir(){
		return Assets.HOME_DIR;
	}
	
	public static String getAppDir(){
		return Assets.APP_DIR;
	}
	
	public static ImagePanel openImagePanel(String name){
		return new ImagePanel(Assets.openImageIcon(name));
	}
	
	public static ImageIcon openImageIcon(String name){
		return new ImageIcon(new File(ASSET_DIR, name).getPath());
	}
	
	public static JTextArea openTextPanel(String name){
		JTextArea text = new JTextArea();
		text.setEditable(false);
		text.setFocusable(false);
		text.setWrapStyleWord(true);
		text.setLineWrap(true);
		String value = Assets.openText(name);
		if(value != null){
			text.setText(value);
		}
		return text;
	}
	
	private static File getFile(String name){
		return new File(ASSET_DIR, name);
	}
	
	public static String openText(String name){
		BufferedReader reader = null;
		StringBuffer contents = new StringBuffer();
		try{
			reader = new BufferedReader(new FileReader(Assets.getFile(name)));
			String line;
			while((line=reader.readLine()) != null){
				contents.append(line).append('\n');
			}
			reader.close();
		} catch(Exception e){
			return null;
		} finally {
			try{
				if(reader != null){
					reader.close();
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return contents.toString();
	}
	
	public static FileInputStream open(String name){
		try{
			return new FileInputStream(Assets.getFile(name));
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static void registerFileHandler(FileHandler fh){
		mFileHandlers.put(fh.getExtension(), fh);
		mFilters.add(fh.getFilter());
	}
	
	public static List<FileFilter> getFilters(){
		return mFilters;
	}
	
	public static BambooAsset open(File file) throws Exception{
		String ext = file.getName();
		int dotIndex = ext.lastIndexOf('.');
		if(dotIndex != -1){
			ext = ext.substring(dotIndex);
		}
		if(mFileHandlers.containsKey(ext)){
			if(Assets.mLocalPath != null){
				Assets.mPaths.remove(Assets.mLocalPath);
			}
			BambooAsset asset = mFileHandlers.get(ext).open(file);
			Assets.mLocalPath = file.getParent();
			Assets.addTexturePath(file.getParent());
			return asset;
		} else {
			throw new Exception("The ext '"+ext+"' is not supported.");
		}
	}
	
	public enum SaveTypes{
		scene,
		animation,
		camera,
		player,
		all
	}
	
	public static void saveBamboo(BambooAsset asset, File file, SaveTypes type) throws Exception{
		FileOutputStream stream = new FileOutputStream(file);
		try{
			switch(type){
			case scene:
				Bamboo.saveNodes(asset.getScenes(), stream);
				break;
			case animation:
				Bamboo.saveAnimations(asset.getAnimations(), stream);
				break;
			case all:
				Bamboo.saveBamboo(asset, stream);
			}
			stream.close();
		} catch(Exception e){
			stream.close();
			throw e;
		}
	}
	
	public static void setGlProfile(GLProfile glProfile){
		Assets.mGlProfile = glProfile;
	}
	
	//use this to add a path, either absolute name for a texture or a directory.
	//absolute paths will be checked first, then directories.
	public static void addTexturePath(String path){
		File file = new File(path);
		if(file.isDirectory()){
			Assets.mPaths.add(file);
		} else if(file.isFile()){
			Assets.mAbsolutePaths.put(file.getName(), file);
		}
	}
	
	public static void exportTextures(String fileName, String outPath){
		
	}
	
	public static TextureData openTexture(String fileName) throws Exception{
		File file = getTexturePath(fileName);
		InputStream stream = new FileInputStream(file);
		return TextureIO.newTextureData(Assets.mGlProfile, stream, false, Assets.getExtension(file));
	}
	
	public static String getExtension(File file){
		int index = file.getName().lastIndexOf('.');
		if(index != -1 && index < file.getName().length()){
			return file.getName().substring(index+1);
		} else {
			return "";
		}
	}
	
	private static File getTexturePath(String textureName) throws Exception{
		File file = new File(textureName);
		if(file.isFile()){
			return file;
		}
		if(Assets.mAbsolutePaths.containsKey(file.getName())){
			return Assets.mAbsolutePaths.get(file.getName());
		} else { //search file paths!
			for(File path : Assets.mPaths){
				File fullPath = new File(path.getPath(),file.getName());
				if(fullPath.exists()){
					return fullPath;
				}
			}
		}
		throw new IOException("The file " + textureName + " could not be located.");
	}
}
