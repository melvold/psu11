package com.melvold.sms.client.lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.melvold.sms.R;

public class MessageList implements SMSList {
	
	private String[] from;
	private int[] to;
	private List<Map<String, Object>> messagelist;
	
	public MessageList(ArrayList<ArrayList<String>> messages){
		messagelist = new ArrayList<Map<String,Object>>();
		from = new String[] {"NAME", "TEXT", "ICON"};
		to = new int[] { R.id.message_name, R.id.message_text, R.id.message_image};
		
		for(int i = 0; i < messages.size(); i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("NAME", messages.get(i).get(1));
			String tmp = messages.get(i).get(2);
			if(tmp.length() > 70){
				tmp = tmp.substring(0, 70) + "...";
			}
			map.put("TEXT", tmp);
			map.put("ICON", R.drawable.message);
			messagelist.add(map);
		}
	}

	public List<Map<String, Object>> getList(){
		return this.messagelist;
	}
	
	public String[] getFrom() {
		return this.from;
	}

	public int[] getTo() {
		return this.to;
	}
}
