package com.tespirit.bamporter.editor;

import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

/**
 * this contains reusable editor panels.
 * @author Todd Espiritu Santo
 *
 */
public class EditorPanels {
	private static JEditorPane mTextInfo;
	
	private static DefaultTreeModel mNavigator;
	
	public static void setNavigator(DefaultTreeModel navigator){
		mNavigator = navigator;
	}
	
	public static void refreshNavigator(MutableTreeNode node){
		mNavigator.reload(node);
	}
	
	public static void refreshNavigator(){
		mNavigator.reload();
	}
	
	public static JEditorPane getTextInfo(){
		if(mTextInfo == null){
			mTextInfo = new JEditorPane();
			mTextInfo.setEditable(false);
		}
		return mTextInfo;
	}
	
	/**
	 * for longs!
	 * @param min
	 * @param max
	 * @return
	 */
	public static JSpinner createLongSpinner(long start, long min, long max, long step){
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(start, min, max, step));
		spinner.setPreferredSize(new Dimension(50, 15));
		JSpinner.NumberEditor numberEditor = new JSpinner.NumberEditor(spinner);
		spinner.setEditor(numberEditor);
		return spinner;
	}
	
	/**
	 * for floats!
	 * @param min
	 * @param max
	 * @param step
	 * @return
	 */
	public static JSpinner createFloatSpinner(float start, float min, float max, float step){
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(start, min, max, step));
		spinner.setPreferredSize(new Dimension(50, 15));
		JSpinner.NumberEditor numberEditor = new JSpinner.NumberEditor(spinner);
		spinner.setEditor(numberEditor);
		return spinner;
	}
	
	public static void alertError(String message){
		JOptionPane.showMessageDialog(null, message, "Uh oh!", JOptionPane.ERROR_MESSAGE);
	}
	
	public static String promptString(String message){
		return JOptionPane.showInputDialog(message);
	}
}
