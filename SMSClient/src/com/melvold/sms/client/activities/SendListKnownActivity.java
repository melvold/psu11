package com.melvold.sms.client.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.melvold.projects.sms.R;

public class SendListKnownActivity extends ListActivity {

	private TextView tv_header;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listheader, lv, false);
		lv.addHeaderView(header, null, false);
		
		String[] from = {"ICON", "NAME"};
		int[] to = {R.id.group_image, R.id.group_name};
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ICON", R.drawable.archive);
		map1.put("NAME", "Forvaltning");
		list.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ICON", R.drawable.archive);
		map2.put("NAME", "Etterforskning");
		list.add(map2);

		SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.listgroups, from, to);
		setListAdapter(sa);

		tv_header = (TextView)findViewById(R.id.listheader_text);
		tv_header.setText("Velg kategori");
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
		int position, long id) {
			Intent intent = new Intent();
			switch (position) {
			case 1:
				System.out.println("++++POSITION " + position);
				intent.putExtra("TYPE", "Forvaltning");
				intent.setClass(SendListKnownActivity.this, ShowMessagesActivity.class);
				startActivity(intent);
				break;
			case 2:
				System.out.println("++++POSITION " + position);
				intent.putExtra("TYPE", "Etterforskning");
				intent.setClass(SendListKnownActivity.this, ShowMessagesActivity.class);
				startActivity(intent);
				break;
			default:
				System.out.println("++++POSITION " + position);
				break;
			}
		}
	});
	}
}
