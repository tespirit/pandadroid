package com.tespirit.bamporter.editor;

public class Util {
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
