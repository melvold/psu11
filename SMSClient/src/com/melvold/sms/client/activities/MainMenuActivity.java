package com.melvold.sms.client.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.melvold.projects.sms.R;
import com.melvold.sms.client.utils.Macros;
import com.melvold.sms.dbinterface.DBInterface;

public class MainMenuActivity extends Activity {
	
	public static DBInterface dbi;
	public Context con;
	
	private boolean hasConnection;
	private Button bSend;
	private Button bGroups;
	
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
			
		}else if(dbi == null){
			Intent intent = new Intent();
			intent.setClass(MainMenuActivity.this, LoginActivity.class);
			startActivityForResult(intent, Macros.LOGIN);
		}else{
/*			setContentBasedOnLayout();
			
			bSend = (Button)findViewById(R.id.menu_send);
			bGroups = (Button)findViewById(R.id.menu_show_groups);
			bSend.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
			bGroups.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});*/
			finish();
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
					if(dbi == null){
						finish();
					}else{
						setContentBasedOnLayout();
						
						bSend = (Button)findViewById(R.id.menu_send);
						bGroups = (Button)findViewById(R.id.menu_show_groups);
						bSend.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent();
								intent.setClass(MainMenuActivity.this, SendMenuActivity.class);
								startActivity(intent);
							}
						});
						bGroups.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent();
								intent.setClass(MainMenuActivity.this, ShowGroupsActivity.class);
								startActivity(intent);
							}
						});
					}
				}
			}
		}
		if (resultCode == RESULT_CANCELED) {
			finish();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.login_menu_exit:
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
		    inflater.inflate(R.layout.login_menu, menu);
		    return true;
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
	
	@Override
	public void onBackPressed(){
		//dbi.logOut();
	}
}
