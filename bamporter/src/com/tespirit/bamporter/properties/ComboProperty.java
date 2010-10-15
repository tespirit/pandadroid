package com.tespirit.bamporter.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

public abstract class ComboProperty<T> extends Property<T>{
	private List<T> mItems;
	private int mSelected;
	private JComboBox mCombo;

	public ComboProperty(String name) {
		super(name);
		this.mItems = new ArrayList<T>();
	}

	@Override
	public JComponent getEditor() {
		this.mCombo = new JComboBox();
		for(T item : this.mItems){
			this.mCombo.addItem(item);
		}
		this.mItems.clear();
		this.mItems = null;
		
		T currentValue = this.getValue();
		if(currentValue != null){
			this.mCombo.setSelectedItem(this.getValue());
		} 
		
		this.mCombo.addActionListener(new ActionListener(){
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				setValue((T)mCombo.getSelectedItem());
			}
		});
		
		if (currentValue == null && this.mSelected < this.mCombo.getItemCount()){
			this.mCombo.setSelectedIndex(this.mSelected);
		}
		
		return this.mCombo;
	}
	
	/**
	 * Default get value is null. override this to make sure that an item other than the first item
	 * is selected upon initialization.
	 */
	@Override
	public T getValue() {
		return null;
	}
	
	public void addItem(T item){
		if(this.mCombo == null){
			this.mItems.add(item);
		} else {
			this.mCombo.addItem(item);
		}
	}
	
	public void insertItem(T item, int index){
		if(this.mCombo == null){
			this.mItems.add(index, item);
		} else {
			if(index > this.mCombo.getItemCount()){
				this.mCombo.addItem(item);
			} else {
				this.mCombo.insertItemAt(item, index);
			}
		}
	}
	
	public void removeItem(T item){
		if(this.mCombo == null){
			this.mItems.remove(item);
		} else {
			this.mCombo.removeItem(item);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T getSelectedItem(){
		if(this.mCombo != null){
			return (T) this.mCombo.getSelectedItem();
		} else if(this.mItems.size() > 0){
			return this.mItems.get(0);
		} else {
			return null;
		}
	}
	
	public int getItemCount(){
		return this.mCombo.getItemCount();
	}
	
	public void removeAllItems(){
		if(this.mCombo != null){
			this.mCombo.removeAllItems();
		} else {
			this.mItems.clear();
		}
	}
	
	public void selectIndex(int index){
		if(this.mCombo != null){
			if(index < this.mCombo.getItemCount()){
				this.mCombo.setSelectedIndex(index);
			}
		} else {
			this.mSelected = index;
		}
	}

}
