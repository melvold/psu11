package com.melvold.sms.client.lists;

import java.util.ArrayList;
import java.util.List;

public class GroupCheckList {
	private List<String> groupList;
	private List<String> groupIdList;
	
	public GroupCheckList(ArrayList<ArrayList<String>> groups){
		groupList = new ArrayList<String>();
		groupIdList = new ArrayList<String>();
		for(int i = 0; i < groups.size(); i++){
			groupIdList.add(groups.get(i).get(0));
			groupList.add(groups.get(i).get(1));
		}
		
	}

	public List<String> getList(){
		return this.groupList;
	}
	
	public String getGroupId(int index){
		return this.groupIdList.get(index);
	}
}
