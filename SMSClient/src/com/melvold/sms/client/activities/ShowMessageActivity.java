package com.melvold.sms.client.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.melvold.sms.R;

public class ShowMessageActivity extends Activity {

	private static final int CONTEXTMENU_SEND_CHOSEN_NUMBER = 0;
	private static final int CONTEXTMENU_SEND_CHOSEN_USERS = 1;
	private static final int CONTEXTMENU_SEND_CHOSEN_GROUPS = 2;
	private static final int CONTEXTMENU_COPY = 3;

	
	private ArrayList<ArrayList<String>> message;
	private TextView tv_header;
	private TextView tv_body;
	private Button b_choose;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showmessage);
		String m_id = getIntent().getStringExtra("MESSAGE");
		if(m_id != null){
			message = MainMenuActivity.dbi.listCompleteMessageByID(m_id);
		}else{
			Toast.makeText(getApplicationContext(), "Feil! meldingen ble ikke funnet", Toast.LENGTH_LONG).show();
			finish();
		}
		if(message != null && message.size() > 0){
			tv_header = (TextView)findViewById(R.id.showmessage_header);
			tv_header.setText(message.get(0).get(0));
	
			tv_body = (TextView)findViewById(R.id.showmessage_body);
			tv_body.setText(message.get(0).get(1));
			registerForContextMenu(tv_body);
			
			b_choose = (Button)findViewById(R.id.showmessage_send);
			b_choose.setLongClickable(false);
			b_choose.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					
					registerForContextMenu(b_choose);
					b_choose.showContextMenu();
				}
			});
		}else{
			Toast.makeText(getApplicationContext(), "Feil! meldingen ble ikke funnet", Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	ContextMenuInfo menuInfo) {
		if(v instanceof Button){
			menu.setHeaderTitle("Send til...");
			menu.add(0, CONTEXTMENU_SEND_CHOSEN_NUMBER, 0, "nummer");
			menu.add(0, CONTEXTMENU_SEND_CHOSEN_USERS, 0, "valgte mottakere");
			menu.add(0, CONTEXTMENU_SEND_CHOSEN_GROUPS, 0, "valgte grupper");
		}else if(v instanceof TextView){
			menu.setHeaderTitle("Valg");
			menu.add(0, CONTEXTMENU_COPY, 0, "kopier melding");
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case CONTEXTMENU_SEND_CHOSEN_NUMBER:{
				Intent intent = new Intent();
				intent.putExtra("SENDMESSAGE", tv_body.getText());
				intent.setClass(getApplicationContext(), SendNumberActivity.class);
				startActivity(intent);
				return true;
			}
			case CONTEXTMENU_SEND_CHOSEN_USERS:{
				Intent intent = new Intent();
				intent.putExtra("SENDMESSAGE", tv_body.getText());
				intent.setClass(getApplicationContext(), CheckMembersActivity.class);
				startActivity(intent);
				return true;
			}
			case CONTEXTMENU_SEND_CHOSEN_GROUPS: {
				Intent intent = new Intent();
				intent.putExtra("SENDMESSAGE", tv_body.getText());
				intent.setClass(getApplicationContext(), CheckGroupsActivity.class);
				startActivity(intent);
				return true;
			}
			case CONTEXTMENU_COPY:{
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
				clipboard.setText(tv_body.getText());
				return true;
			}
			default:{
				return super.onContextItemSelected(item);
			}
		}
	}
}
