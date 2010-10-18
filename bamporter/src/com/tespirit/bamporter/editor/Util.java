package com.tespirit.bamporter.editor;

import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;

/**
 * this contains reusable editor panels.
 * @author Todd Espiritu Santo
 *
 */
public class Util {
	private static JEditorPane mTextInfo;
	
	public static JEditorPane getTextInfo(){
		if(mTextInfo == null){
			mTextInfo = new JEditorPane();
			mTextInfo.setEditable(false);
		}
		return mTextInfo;
	}
	
	
	public static void alertError(String message){
		JOptionPane.showMessageDialog(null, message, "Uh oh!", JOptionPane.ERROR_MESSAGE);
	}
	
	public static String promptString(String message){
		return JOptionPane.showInputDialog(message);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T promptChoice(String message, List<T> items, T defaultItem){
		Object[] objects = items.toArray();
		int index = JOptionPane.showOptionDialog(null, message, "Hmm...", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, objects, defaultItem);
		if(index == JOptionPane.CLOSED_OPTION){
			return null;
		}
		return (T)objects[index];
	}
	
	public static String getClassName(Object o){
		String className = o.toString();
		int i = className.lastIndexOf('.');
		if(i != -1){
			className = className.substring(i+1);
		}
		i = className.indexOf('@');
		if(i != -1){
			className = className.substring(0, i);
		}
		return "<"+className+">";
	}
}
