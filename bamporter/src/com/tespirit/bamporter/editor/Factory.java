package com.tespirit.bamporter.editor;

public interface Factory{
	Editor createEditor(Object object);
	Class<?> getDataClass();
}