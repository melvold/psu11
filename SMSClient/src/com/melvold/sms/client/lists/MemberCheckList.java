package com.melvold.sms.client.lists;

import java.util.ArrayList;
import java.util.List;

public class MemberCheckList{
	
	private List<String> memberlist;
	
	public MemberCheckList(ArrayList<ArrayList<String>> members){
		memberlist = new ArrayList<String>();
		
		for(int i = 0; i <members.size(); i++){
			memberlist.add(members.get(i).get(1) + ", " + members.get(i).get(0) + "\n" + members.get(i).get(2));
		}
		
	}

	public List<String> getList(){
		return this.memberlist;
	}
}