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
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.melvold.sms.R;
import com.melvold.sms.client.utils.Macros;
import com.melvold.sms.lib.dbinterface.DBInterface;

public class MainMenuActivity extends Activity {
	
	public static DBInterface dbi;
	public Context con;
	
	private boolean hasConnection;
	private Button bSend;
	private Button bGroups;
	private Button bAdmin;
	private Button bnumber;
	private Button bmembers;
	private Button bgroup;
	private Button bknown;
	private ViewFlipper vf;
	
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
						setContentView(R.layout.main);
						
						bSend = (Button)findViewById(R.id.menu_send);
						bGroups = (Button)findViewById(R.id.menu_show_groups);
						bAdmin = (Button)findViewById(R.id.menu_admin);
						bnumber = (Button)findViewById(R.id.send_nomember);
						bmembers = (Button)findViewById(R.id.send_members);
						bgroup = (Button)findViewById(R.id.send_group);
						bknown = (Button)findViewById(R.id.send_known);
						
						vf = (ViewFlipper)findViewById(R.id.vfmain);
						

						
						if(dbi.getCommunication().isAdministrator()){
							bAdmin.setVisibility(View.INVISIBLE);
/*							bAdmin.setOnClickListener(new OnClickListener() {
								
								public void onClick(View v) {
									Intent intent = new Intent();
									intent.setClass(getApplicationContext(), AdminMenuActivity.class);
									startActivity(intent);
								}
							});*/
						}else{
							bAdmin.setVisibility(View.INVISIBLE);
						}
						
						bSend.setOnClickListener(new View.OnClickListener() {
							
							public void onClick(View v) {
								setVFAnimation("forward");
								vf.showNext();
							}
						});
						bGroups.setOnClickListener(new View.OnClickListener() {
							
							public void onClick(View v) {
								Intent intent = new Intent();
								intent.setClass(MainMenuActivity.this, ShowGroupsActivity.class);
								startActivity(intent);
							}
						});
						
						bnumber.setOnClickListener(new OnClickListener() {
							
							
							public void onClick(View v) {
								Intent intent = new Intent();
								intent.setClass(MainMenuActivity.this, SendNumberActivity.class);
								startActivity(intent);
							}
						});
						
						bmembers.setOnClickListener(new OnClickListener() {
							
							
							public void onClick(View v) {
								Intent intent = new Intent();
								intent.setClass(MainMenuActivity.this, CheckMembersActivity.class);
								startActivity(intent);
							}
						});
						
						bgroup.setOnClickListener(new OnClickListener() {
							
							
							public void onClick(View v) {
								Intent intent = new Intent();
								intent.setClass(MainMenuActivity.this, CheckGroupsActivity.class);
								startActivity(intent);
							}
						});
						
						bknown.setOnClickListener(new OnClickListener() {
							
							
							public void onClick(View v) {
								Intent intent = new Intent();
								intent.setClass(MainMenuActivity.this, SendListKnownActivity.class);
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
		if(vf.getCurrentView().getId() == R.id.vfmain_second){
			setVFAnimation("backward");
			vf.showPrevious();
		}
	}
	
	public void setVFAnimation(String string) {
		if(string.equals("forward")){
			vf.setInAnimation(this, R.anim.slideinf);
			vf.setOutAnimation(this, R.anim.slideoutf);
		}else if(string.equals("backward")){
			vf.setInAnimation(this, R.anim.slideinb);
			vf.setOutAnimation(this, R.anim.slideoutb);
		}
		
	}

}
