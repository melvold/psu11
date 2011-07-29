package com.melvold.sms.client.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.melvold.projects.sms.R;

public class CheckAdapter extends ArrayAdapter<String> implements OnClickListener, SectionIndexer{

	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	private ArrayList<String> selectedRows;
	private ArrayList<String> check_items;
	private Context con;
	
	public CheckAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		
		alphaIndexer = new HashMap<String, Integer>();
		for(int i = objects.size() - 1; i >= 0; i--){
			String s = objects.get(i).trim();
			s = s.substring(0, 1);
			s = s.toUpperCase();
			alphaIndexer.put(s, i);
		}
		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters); 
        Collections.sort(sectionList);
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
        
		selectedRows = new ArrayList<String>();
		check_items = objects;
		con = context;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.cb_custom_row, parent, false);
		}

		
		String text = check_items.get(position);
		TextView tvRow = (TextView) v.findViewById(R.id.tv_checkmember);
		CheckBox cbRow = (CheckBox) v.findViewById(R.id.cb_custom_row2);
		
		cbRow.setTag(text);
		if(selectedRows.contains(text)){
			cbRow.setChecked(true);
		}else{
			cbRow.setChecked(false);
		}
		
		tvRow.setText(text);
		cbRow.setOnClickListener(this);
		return v;
		
	}
	
	@Override
	public void onClick(View v) {
		CheckBox cb = (CheckBox)v;
		String text = (String)cb.getTag();
		
		if(!selectedRows.contains(text)){
			//System.out.println("+++++ ADDING " + text + " AND SETTING CB CHECKED");
			selectedRows.add(text);
			cb.setChecked(true);
		}else{
			//System.out.println("+++++ REMOVING " + text + " AND SETTING CB NOT CHECKED");
			selectedRows.remove(text);
			cb.setChecked(false);
		}
		//System.out.println("+++++++ NUMBER SELECTED = " + selectedRows.size());
	}
	
	public ArrayList<String> getSelectedRows(){
		return selectedRows;
	}
	
	public void selectAllRows(){
		selectedRows.clear();
		for(int i = 0; i < check_items.size(); i++){
			selectedRows.add(check_items.get(i));
		}
		this.notifyDataSetChanged();
	}
	
	public void deselectAllRows(){
		selectedRows.clear();
		this.notifyDataSetChanged();
	}
	
	@Override
    public int getPositionForSection(int section) {
        return alphaIndexer.get(sections[section]);
    }

	@Override
    public int getSectionForPosition(int position) {
        return 1;
    }

	@Override
    public Object[] getSections() {
         return sections;
    }
	
}
