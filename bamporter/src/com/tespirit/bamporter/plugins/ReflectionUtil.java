package com.tespirit.bamporter.plugins;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtil{
	
	private static final String CLASS_EXT = ".class";
	
	@SuppressWarnings("unchecked")
	public static <T> Set<Class<? extends T>> getSubClasses(String packageName, Class<T> baseClass){
		HashSet<Class<? extends T>> result = new HashSet<Class<? extends T>>();
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        
        URL resource = classLoader.getResource(path);
        File packageDir = new File(resource.getFile());
        File[] files = packageDir.listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(CLASS_EXT);
			}
        });
        
        for(File file : files){
        	String className = file.getName();
        	className = className.substring(0, className.length() - CLASS_EXT.length());
        	Class<?> classy;
			try {
				classy = Class.forName(packageName+"."+className);
	        	if(baseClass.isAssignableFrom(classy)){
	        		result.add((Class<? extends T>)classy);
	        	}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        }
		return result;
	}
}
