package com.melvold.sms.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
		Context con = getApplicationContext();
		if(!haveNetworkConnection()){
			//Intent intent = new Intent();
			//intent.putExtra("message", "ERROR");
			//intent.putExtra("image", R.drawable.icon);
			//intent.setClass(con, ToastMessageActivity.class);
			//startActivity(intent);

		}else{

			bCancel = (Button) findViewById(R.id.login_cancel);
			bOk = (Button) findViewById(R.id.login_ok);
			tvBid = (TextView) findViewById(R.id.login_et_bid);
			tvPassword = (TextView) findViewById(R.id.login_et_password);

			bCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});

			bOk.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
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




}
