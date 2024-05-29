package com.woodnsoft.airmajor_bs_android.DataListView.Dialog_item_spec;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class Dialog_item_spec_ListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Dialog_item_spec_Item> mItems = new ArrayList<Dialog_item_spec_Item>();
    public Dialog_item_spec_ListAdapter(Context context) {
        mContext = context;
    }
 
	public void clear() {
        mItems.clear();
        
    }
     
    public void addItem(Dialog_item_spec_Item it) {
        mItems.add(it);
    }
 
    public void setListItems(List<Dialog_item_spec_Item> lit) {
        mItems = lit;
    }
    
    public int getCount() {
        return mItems.size();
    }
 
    public Object getItem(int position) {
        return mItems.get(position);
    }
 
    public boolean areAllItemsSelectable() {
        return false;
    }
    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Dialog_item_spec_View itemView;
        if (convertView == null) {
           itemView = new Dialog_item_spec_View(mContext, mItems.get(position));
        } else {
            itemView = (Dialog_item_spec_View) convertView;
             
            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(1, mItems.get(position).getData(1));
        }
        return itemView;
    }	
	
 	public void remove(int i) {
		// TODO Auto-generated method stub
		if (mItems.size() - 1 >= i)	mItems.remove(i);
	}	
	
	

	
}