package com.melvold.sms.client.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.melvold.projects.sms.R;
import com.melvold.sms.client.utils.Macros;
import com.melvold.sms.client.utils.smsUtils;

public class SendNumberActivity extends Activity {
	
	private Button bBack;
	private Button bSend;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentBasedOnLayout();
		
		bBack = (Button)findViewById(R.id.sendnumber_back);
		bSend = (Button)findViewById(R.id.sendnumber_send);
		
		bBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		bSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText etTo = (EditText) findViewById(R.id.et_tlf_number);
				EditText etMessage = (EditText)findViewById(R.id.et_tlf_message);
				String to = etTo.getText().toString();
				String message = etMessage.getText().toString();
				if(to !=  null && message!= null && !to.equals("") && !message.equals("")){
					if(!smsUtils.isPhoneNumber(to)){
						Toast.makeText(getApplicationContext(), "Ugyldig telefonnummer!", Toast.LENGTH_LONG).show();
					}else{
						MainMenuActivity.dbi.sendMessage(Macros.server_number, to, message);
						Toast.makeText(getApplicationContext(), "Melding sendt!", Toast.LENGTH_LONG).show();
						finish();
					}
				}else{
					if(to == null || to.equals("")){
						Toast.makeText(getApplicationContext(), "Telefonnummer?", Toast.LENGTH_LONG).show();
					}else if(message == null || message.equals("")){
						Toast.makeText(getApplicationContext(), "Vennligst skriv en melding!", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
	}
	
	private void setContentBasedOnLayout(){
	    WindowManager winMan = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	    
	    if (winMan != null){
	        int orientation = winMan.getDefaultDisplay().getOrientation();
	        if (orientation == 0){
	            // Portrait
	            setContentView(R.layout.sendnumber);
	        }else if (orientation == 1) {
	        	// Landscape
	        	setContentView(R.layout.sendnumber);
	        }            
	    }
	}
}
