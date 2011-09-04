package com.melvold.sms.client.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.melvold.sms.R;

public class AdminMenuActivity extends Activity {

	private Button bMembers;
	private Button bGroups;
	private Button bListAdmins;
	private Button bListMessages;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminmain);
		
		bMembers = (Button)findViewById(R.id.adminmenu_members);
		bGroups = (Button)findViewById(R.id.adminmenu_groups);
		bListAdmins = (Button)findViewById(R.id.adminmenu_admins);
		bListMessages = (Button)findViewById(R.id.adminmenu_messages);
		
		bMembers.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), AdminMembersActivity.class);
				startActivity(intent);
			}
		});
		
		bGroups.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			}
		});
		
		bListAdmins.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			}
		});
		
		bListMessages.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
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
	
}
