package com.melvold.sms.client.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.melvold.projects.sms.R;

public class SendMenuActivity extends Activity {
	
	private Button bnumber;
	private Button bmembers;
	private Button bgroup;
	private Button bknown;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentBasedOnLayout();
		
		bnumber = (Button)findViewById(R.id.send_nomember);
		bmembers = (Button)findViewById(R.id.send_members);
		bgroup = (Button)findViewById(R.id.send_group);
		bknown = (Button)findViewById(R.id.send_known);
		
		bnumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		bmembers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		bgroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		bknown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	private void setContentBasedOnLayout(){
	    WindowManager winMan = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	    
	    if (winMan != null){
	        int orientation = winMan.getDefaultDisplay().getOrientation();
	        if (orientation == 0){
	            // Portrait
	            setContentView(R.layout.sendmain);
	        }else if (orientation == 1) {
	        	// Landscape
	        	setContentView(R.layout.sendmain);
	        }            
	    }
	}
}
