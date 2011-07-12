package com.melvold.sms.client.lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.melvold.projects.sms.R;

public class MemberList implements SMSList {
	
	private String[] from;
	private int[] to;
	private List<Map<String, Object>> memberlist;
	
	public MemberList(ArrayList<ArrayList<String>> members){
		memberlist = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> tmp = new ArrayList<Map<String,Object>>();
		from = new String[] {"NAME", "TLF", "ICON"};
		to = new int[] { R.id.member_name, R.id.member_tlf, R.id.member_image};
		
		for(int i = 0; i < members.size(); i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("NAME", members.get(i).get(1) + ", " + members.get(i).get(0));
			map.put("TLF", "+47" + members.get(i).get(2));
			map.put("ICON", R.drawable.member);
			memberlist.add(map);
		}
	}

	public List<Map<String, Object>> getList(){
		return this.memberlist;
	}
	@Override
	public String[] getFrom() {
		return this.from;
	}

	@Override
	public int[] getTo() {
		return this.to;
	}

}
