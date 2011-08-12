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

import com.melvold.sms.R;
import com.melvold.sms.client.lists.MessageList;

public class ShowMessagesActivity extends ListActivity {

	private ArrayList<ArrayList<String>> messages;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String type = getIntent().getStringExtra("TYPE");
		
		if(!type.equals(null)){
			messages = MainMenuActivity.dbi.listCompleteMessagesByType(type);
		}else{
			finish();
		}
		
		if(messages.size() > 0){
		
			MessageList ml = new MessageList(messages);
			
			SimpleAdapter sa = new SimpleAdapter(this, ml.getList(), R.layout.listmessages, ml.getFrom(), ml.getTo());
			setListAdapter(sa);
	
			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
	
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
					switch (position) {
						default:
							Toast.makeText(getApplicationContext(), "Clicked on message " + position, Toast.LENGTH_LONG).show();
							break;
						}
				}
			});
		}else{
			Toast.makeText(getApplicationContext(), "Det finnes ingen innlagte meldinger i kategorien \"" + type + "\"", Toast.LENGTH_LONG).show();
			finish();
		}
	}
}
