package com.melvold.sms.client;

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
import android.widget.TextView;
import android.widget.Toast;

import com.melvold.projects.sms.R;

public class LoginActivity extends Activity {
	
	private Button bCancel;
	private Button bOk;
	private TextView tvBid;
	private TextView tvPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
			
			bCancel = (Button) findViewById(R.id.login_cancel);
			bOk = (Button) findViewById(R.id.login_ok);
			tvBid = (TextView) findViewById(R.id.login_et_bid);
			tvPassword = (TextView) findViewById(R.id.login_et_password);

			bCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setResult(RESULT_OK);
					finish();
				}
			});

			bOk.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			}
		}
		if (resultCode == RESULT_CANCELED) {
			
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

}
