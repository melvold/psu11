package com.melvold.sms.client.activities;
import java.io.File;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.melvold.projects.sms.R;
import com.melvold.sms.client.lists.GroupList;
import com.melvold.sms.client.utils.smsUtils;


public class ShowGroupsActivity extends ListActivity {

	private TextView tv_title;
	private ArrayList<ArrayList<String>> groups;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listlayout);
		
		//LayoutInflater inflater = getLayoutInflater();
		//ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listheader, lv, false);
		//lv.addHeaderView(header, null, false);

		groups= MainMenuActivity.dbi.listAllGroups();
		GroupList gl = new GroupList(groups);
		SimpleAdapter sa = new SimpleAdapter(this, gl.getList(), R.layout.listgroups, gl.getFrom(), gl.getTo());
		setListAdapter(sa);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		tv_title = (TextView)findViewById(R.id.listheader_text);
		tv_title.setText("Grupper");
		
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
		int position, long id) {
			switch (position) {
				default:
				String groupID = groups.get(position).get(0);
				Intent intent = new Intent();
				intent.setClass(ShowGroupsActivity.this, ShowMembersActivity.class);
				intent.putExtra("GROUP", groupID);
				startActivity(intent);
				break;
				}
		}
	});
	}
}
