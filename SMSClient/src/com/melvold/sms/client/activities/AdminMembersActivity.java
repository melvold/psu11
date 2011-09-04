package com.melvold.sms.client.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.TextView;

import com.melvold.sms.R;

public class AdminMembersActivity extends Activity {
	
	private static final int CONTEXTMENU_SEARCH_PERSON = 0;
	private static final int CONTEXTMENU_BROWSE_PERSON = 1;
	
	private Button bNewMember;
	private Button bChangeMember;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminmembers);
		
		bNewMember = (Button)findViewById(R.id.adminmembers_newmember);
		bChangeMember = (Button)findViewById(R.id.adminmembers_editmember);
		
		bNewMember.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), AdminNewMemberActivity.class);
				startActivity(intent);
			}
		});
		
		bChangeMember.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				registerForContextMenu(v);
				v.showContextMenu();
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.admin_menu_back:
	    	setResult(RESULT_OK);
	    	finish();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.layout.admin_menu, menu);
		    return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	ContextMenuInfo menuInfo) {
		if(v instanceof Button){
			menu.setHeaderTitle("Velg medlem");
			menu.add(0, CONTEXTMENU_SEARCH_PERSON, 0, "Søk medlem");
			menu.add(0, CONTEXTMENU_BROWSE_PERSON, 0, "Browse alle medlemmer");
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case CONTEXTMENU_SEARCH_PERSON:{
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SearchMemberActivity.class);
				startActivity(intent);
				return super.onContextItemSelected(item);
			}
			case CONTEXTMENU_BROWSE_PERSON:{
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ShowMembersActivity.class);
				intent.putExtra("ADMIN", true);
				startActivity(intent);
				return super.onContextItemSelected(item);
			}
			default:{
				return super.onContextItemSelected(item);
			}
		}
	}
}
