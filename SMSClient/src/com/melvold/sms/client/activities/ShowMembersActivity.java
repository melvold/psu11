package com.melvold.sms.client.activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.melvold.projects.sms.R;
import com.melvold.sms.client.lists.MemberList;

public class ShowMembersActivity extends ListActivity {

	
	private ArrayList<ArrayList<String>> members;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String groupID = getIntent().getStringExtra("GROUP");
		
		if(!groupID.equals(null)){
			members = MainMenuActivity.dbi.listMembersInGroup(groupID);
		}else{
			
		}
		
		MemberList ml = new MemberList(members);
		
		SimpleAdapter sa = new SimpleAdapter(this, ml.getList(), R.layout.listmembers, ml.getFrom(), ml.getTo());
		setListAdapter(sa);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
		int position, long id) {
			switch (position) {
				default:
					Toast.makeText(getApplicationContext(), "Clicked on member " + position, Toast.LENGTH_LONG).show();
					break;
				}
		}
	});
	}
	
}
