package com.melvold.sms.client.activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.melvold.sms.R;
import com.melvold.sms.client.lists.MessageList;

public class ShowMessagesActivity extends ListActivity {

	private static final int CONTEXTMENU_SEND_CHOSEN_NUMBER = 0;
	private static final int CONTEXTMENU_SEND_CHOSEN_USERS = 1;
	private static final int CONTEXTMENU_SEND_CHOSEN_GROUPS = 2;
	private static final int CONTEXTMENU_COPY = 3;
	
	private ArrayList<ArrayList<String>> messages;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerForContextMenu(getListView());
		String type = getIntent().getStringExtra("TYPE");
		
		if(type != null){
			messages = MainMenuActivity.dbi.listCompleteMessagesByType(type);
		}else{
			finish();
		}
		
		if(messages != null && messages.size() > 0){
		
			MessageList ml = new MessageList(messages);
			
			SimpleAdapter sa = new SimpleAdapter(this, ml.getList(), R.layout.listmessages, ml.getFrom(), ml.getTo());
			setListAdapter(sa);
	
			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
	
			lv.setOnItemClickListener(new OnItemClickListener() {
				
				public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
					switch (position) {
						default:
							Intent intent = new Intent();
							intent.putExtra("MESSAGE", messages.get(position).get(0));
							intent.setClass(getApplicationContext(), ShowMessageActivity.class);
							startActivity(intent);
							break;
						}
				}
			});
		}else{
			Toast.makeText(getApplicationContext(), "Det finnes ingen innlagte meldinger i kategorien \"" + type + "\"", Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Valg");
		menu.add(0, CONTEXTMENU_SEND_CHOSEN_NUMBER, 0, "Send til nummer");
		menu.add(0, CONTEXTMENU_SEND_CHOSEN_USERS, 0, "Send til mottakere");
		menu.add(0, CONTEXTMENU_SEND_CHOSEN_GROUPS, 0, "Send til grupper");
		menu.add(0, CONTEXTMENU_COPY, 0, "Kopier melding");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case CONTEXTMENU_SEND_CHOSEN_NUMBER:{
			Intent intent = new Intent();
			intent.putExtra("SENDMESSAGE", messages.get(info.position).get(2));
			intent.setClass(getApplicationContext(), SendNumberActivity.class);
			startActivity(intent);
			return true;
		}
		case CONTEXTMENU_SEND_CHOSEN_USERS:{
			Intent intent = new Intent();
			intent.putExtra("SENDMESSAGE", messages.get(info.position).get(2));
			intent.setClass(getApplicationContext(), CheckMembersActivity.class);
			startActivity(intent);
			return true;
		}
		case CONTEXTMENU_SEND_CHOSEN_GROUPS: {
			Intent intent = new Intent();
			intent.putExtra("SENDMESSAGE", messages.get(info.position).get(2));
			intent.setClass(getApplicationContext(), CheckGroupsActivity.class);
			startActivity(intent);
			return true;
		}
		case CONTEXTMENU_COPY:{
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
			clipboard.setText(messages.get(info.position).get(2));
			return true;
		}
		default:{
			return super.onContextItemSelected(item);
		}
	}
	}
	
	
}
