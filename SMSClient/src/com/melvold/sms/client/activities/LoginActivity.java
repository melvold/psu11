package com.melvold.sms.client.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.melvold.sms.R;
import com.melvold.sms.client.workers.LoginThread;

public class LoginActivity extends Activity{
	
	private TextView tvBid;
	private TextView tvPassword;
	private Button bCancel;
	private Button bOk;
	
	private ProgressDialog progDialog;
	private LoginThread progThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		MainMenuActivity.dbi = null;
			
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
				showDialog(0);
				System.out.println("+++++PROGDIALOG STARTED!");
	            progThread = new LoginThread(getApplicationContext(), tvBid.getText().toString(), tvPassword.getText().toString());
	            progThread.start();
				try {
					System.out.println("+++++++WAITING FOR THREAD TO FINISH WORK");
					progThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(MainMenuActivity.dbi.isConnected()){
					setResult(RESULT_OK);
					finish();
				}else{
					MainMenuActivity.dbi = null;
					Toast.makeText(getApplicationContext(), "Feil BID eller passord!", Toast.LENGTH_LONG).show();
				}
				
			}
		});

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
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
        default:
            progDialog = new ProgressDialog(this);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setMessage("Authentiserer bruker...");
            progDialog.show();
            return progDialog;
        }
    }

}
