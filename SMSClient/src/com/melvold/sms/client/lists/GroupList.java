package com.melvold.sms.client.lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.melvold.sms.R;

public class GroupList implements SMSList{
	
	private String[] from;
	private int[] to;
	private List<Map<String, Object>> grouplist;

	public GroupList(ArrayList<ArrayList<String>> groups){
		grouplist = new ArrayList<Map<String,Object>>();
		from = new String[] {"TEXT", "ICON"};
		to = new int[] { R.id.group_name, R.id.group_image};
		
		for(int i = 0; i < groups.size(); i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("TEXT", groups.get(i).get(1));
			map.put("ICON", R.drawable.group);
			grouplist.add(map);
		}
	}
	
	public String[] getFrom(){
		return this.from;
	}
	
	public int[] getTo(){
		return this.to;
	}
	
	public List<Map<String, Object>> getList(){
		return this.grouplist;
	}
	
}
