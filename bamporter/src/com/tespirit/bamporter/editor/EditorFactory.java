package com.tespirit.bamporter.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditorFactory {
	
	public static void registerFactory(Factory factory){
		EditorFactory.mEditorMap.put(factory.getDataClass().getName(), factory);
	}
	
	public static Editor createEditor(Object object){
		Factory f = EditorFactory.getClassFactory(object.getClass());
		if(f == null){
			f = EditorFactory.getInterfaceFactory(object.getClass().getInterfaces());
		}
		if(f != null){
			return f.createEditor(object);
		} else {
			return null;
		}
	}
	
	public static List<Factory> getEditorFactoriesOf(Class<?> classy){
		ArrayList<Factory> factories = new ArrayList<Factory>(mEditorMap.values().size());
		for(Factory ef : mEditorMap.values()){
			if(classy.isInstance(ef)){
				factories.add(ef);
			}
		}
		return factories;
	}
	
	private static Factory getClassFactory(Class<?> classy){
		Factory f = EditorFactory.mEditorMap.get(classy.getName());
		if(f == null){
			if(classy.getSuperclass() != null) {
				f = getClassFactory(classy.getSuperclass());
			}
		}
		return f;
	}
	
	private static Factory getInterfaceFactory(Class<?>[] classes){
		for(Class<?> c : classes){
			Factory f = EditorFactory.mEditorMap.get(c.getName());
			if(f != null){
				return f;
			}
		}
		for(Class<?> c : classes){
			Factory f = EditorFactory.getInterfaceFactory(c.getInterfaces());
			if(f != null){
				return f;
			}
			if(c.getSuperclass() != null){
				f = EditorFactory.getInterfaceFactory(c.getSuperclass().getInterfaces());
				if(f != null){
					return f;
				}
			}
		}
		return null;
	}
	
	private static HashMap<String, Factory> mEditorMap = new HashMap<String, Factory>();
}
