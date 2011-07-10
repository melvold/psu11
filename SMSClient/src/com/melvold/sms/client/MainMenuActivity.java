package com.melvold.sms.client;

import com.melvold.projects.sms.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenuActivity extends Activity {
	
	public boolean loggedIn;
	public Context con;
	
	private boolean hasConnection;
	private Button bMenu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		con = getApplicationContext();
		hasConnection = haveNetworkConnection();
		if(!hasConnection){
			Intent intent = new Intent();
			intent.setClass(MainMenuActivity.this, ExitMessageActivity.class);
			intent.putExtra("TEXT", "FEIL: Enheten er ikke tilkoblet internett!");
			intent.putExtra("IMAGE", R.drawable.nonetwork);
			startActivityForResult(intent, Macros.EXITM);
			
		}else if(!loggedIn){
			Intent intent = new Intent();
			intent.setClass(MainMenuActivity.this, LoginActivity.class);
			startActivityForResult(intent, Macros.LOGIN);
		}else{
			setContentBasedOnLayout();
			
			bMenu = (Button)findViewById(R.id.menu_b);
			
			bMenu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case Macros.EXITM: {
					finish();
				}
				case Macros.LOGIN:{
					if(!loggedIn){
						finish();
					}
				}
			}
		}
		if (resultCode == RESULT_CANCELED) {
			finish();
		}
	}
	
	private boolean haveNetworkConnection(){

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo)
		{
			if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
				if (ni.isConnected()) {
					return true;
				}
			}
			if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
				if (ni.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void setContentBasedOnLayout(){
	    WindowManager winMan = (WindowManager) con.getSystemService(Context.WINDOW_SERVICE);
	    
	    if (winMan != null){
	        int orientation = winMan.getDefaultDisplay().getOrientation();
	        if (orientation == 0){
	            // Portrait
	            setContentView(R.layout.main);
	        }else if (orientation == 1) {
	        	// Landscape
	        	setContentView(R.layout.main);
	        }            
	    }
	}
}
