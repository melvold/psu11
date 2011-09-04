package com.melvold.sms.client.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.melvold.sms.R;

public class SendMenuActivity extends Activity {
	
	private Button bnumber;
	private Button bmembers;
	private Button bgroup;
	private Button bknown;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmain);
		
		bnumber = (Button)findViewById(R.id.send_nomember);
		bmembers = (Button)findViewById(R.id.send_members);
		bgroup = (Button)findViewById(R.id.send_group);
		bknown = (Button)findViewById(R.id.send_known);
		
		bnumber.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SendMenuActivity.this, SendNumberActivity.class);
				startActivity(intent);
			}
		});
		
		bmembers.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SendMenuActivity.this, CheckMembersActivity.class);
				startActivity(intent);
			}
		});
		
		bgroup.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SendMenuActivity.this, CheckGroupsActivity.class);
				startActivity(intent);
			}
		});
		
		bknown.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SendMenuActivity.this, SendListKnownActivity.class);
				startActivity(intent);
			}
		});
	}
	
}
