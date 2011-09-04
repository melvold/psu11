package com.melvold.sms.client.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.melvold.sms.R;

public class ShowAdapter extends ArrayAdapter<String> implements SectionIndexer{

	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	private ArrayList<String> items;
	private Context con;
	
	public ShowAdapter(Context context, int textViewResourceId,
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
        
		items = objects;
		con = context;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.custom_row, parent, false);
		}

		
		String text = items.get(position);
		TextView tvRow = (TextView) v.findViewById(R.id.tv_showmember);
		tvRow.setText(text);
		ImageView ivRow = (ImageView) v.findViewById(R.id.iv_showmember);
		ivRow.setBackgroundResource(R.drawable.member);
		return v;
		
	}
	
/*	public void onClick(View v) {
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
	}*/
	
    public int getPositionForSection(int section) {
        return alphaIndexer.get(sections[section]);
    }

    public int getSectionForPosition(int position) {
        return 1;
    }

    public Object[] getSections() {
         return sections;
    }
	
}
