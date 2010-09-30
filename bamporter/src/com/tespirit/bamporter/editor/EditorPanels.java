package com.tespirit.bamporter.editor;

import javax.swing.JEditorPane;

/**
 * this contains reusable editor panels.
 * @author Todd Espiritu Santo
 *
 */
public class EditorPanels {
	private static JEditorPane mTextInfo;
	public static JEditorPane getTextInfo(){
		if(mTextInfo == null){
			mTextInfo = new JEditorPane();
			mTextInfo.setEditable(false);
		}
		return mTextInfo;
	}
}
