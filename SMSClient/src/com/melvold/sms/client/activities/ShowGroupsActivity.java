package com.melvold.sms.client.activities;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.melvold.sms.R;
import com.melvold.sms.client.lists.GroupList;


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
				String group = groups.get(position).get(1);
				Intent intent = new Intent();
				intent.setClass(ShowGroupsActivity.this, ShowMembersActivity.class);
				intent.putExtra("GROUP", group);
				startActivity(intent);
				break;
				}
		}
	});
	}
}
