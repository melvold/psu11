package com.melvold.sms.client.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.melvold.sms.R;

public class ShowMemberActivity extends Activity {

	private static final int ALERT_DELETE = 0;
	
	private String uid;
	private String bid;
	private String phone;
	private String phonetype;
	private String status;
	private String name;
	
	private AlertDialog.Builder builder;
	
	private TextView tvUid;
	private TextView tvBid;
	private TextView tvName;
	private TextView tvStatus;
	private TextView tvPhone;
	private EditText etPhone;
	private Spinner spStatus;
	private Spinner spPhoneType;
	private Button bClose;
	private Button bDelete;
	private Button bEdit;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uid = getIntent().getStringExtra("UID");
		bid = getIntent().getStringExtra("BID");
		phone = getIntent().getStringExtra("PHONE");
		phone = phone.trim();
		if(!phone.substring(0, 3).equals("+47") && !phone.substring(0, 4).equals("0047")){
			phone = "+47" + phone;
		}
		phonetype = getIntent().getStringExtra("PHONETYPE");
		phonetype = phonetype.substring(0,1).toUpperCase() + phonetype.substring(1);
		status = getIntent().getStringExtra("STATUS");
		status = status.substring(0,1).toUpperCase() + status.substring(1);
		name = getIntent().getStringExtra("NAME");
		
		if(uid != null && !uid.equals("")){
			setContentView(R.layout.adminmember);
			builder = new AlertDialog.Builder(this);
			ArrayAdapter <CharSequence> aaStatus = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
			aaStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			aaStatus.add("Bruker");
			aaStatus.add("Admin");
			spStatus = (Spinner) findViewById(R.id.adminmember_spstatus);
			spStatus.setAdapter(aaStatus);
			if(status.equalsIgnoreCase("bruker")){
				spStatus.setSelection(0);
			}else{
				spStatus.setSelection(1);
			}
			
			ArrayAdapter <CharSequence> aaPhoneType = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
			aaPhoneType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			aaPhoneType.add("Tjeneste");
			aaPhoneType.add("Privat");
			spPhoneType = (Spinner) findViewById(R.id.adminmember_spphone);
			spPhoneType.setAdapter(aaPhoneType);
			if(phonetype.equalsIgnoreCase("Tjeneste")){
				spPhoneType.setSelection(0);
			}else{
				spPhoneType.setSelection(1);
			}
				tvUid = (TextView)findViewById(R.id.adminmember_uid);
				tvUid.setText(uid);
				tvBid = (TextView)findViewById(R.id.adminmember_bid);
				tvBid.setText(bid);
				tvName = (TextView)findViewById(R.id.adminmember_name);
				tvName.setText(name);
				tvStatus = (TextView)findViewById(R.id.adminmember_show_status);
				tvStatus.setText(status);
				tvPhone = (TextView)findViewById(R.id.adminmember_show_phone);
				tvPhone.setText(phonetype + ": " + phone);
				etPhone = (EditText)findViewById(R.id.adminmember_phone);
				etPhone.setText(phone);
				spStatus = (Spinner)findViewById(R.id.adminmember_spstatus);
				
				builder.setMessage("Er du sikker på at du vil slette " + name + "?")
			       .setCancelable(false)
			       .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   MainMenuActivity.dbi.deleteUser(uid);
			               finish();			        	 
			               Toast.makeText(getApplicationContext(), name + " er nå slettet fra systemet!", Toast.LENGTH_LONG).show();
			           }
			       })
			       .setNegativeButton("Nei", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
				
				bClose = (Button)findViewById(R.id.adminmember_close);
				bClose.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						finish();
					}
				});
				bEdit = (Button)findViewById(R.id.adminmember_edit);
				bEdit.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						MainMenuActivity.dbi.updateUser(uid, null, null, bid, null, null, spStatus.getSelectedItem().toString().toLowerCase(), null);
						MainMenuActivity.dbi.updatePhone(uid, phone, spPhoneType.getSelectedItem().toString().toLowerCase());
						finish();
						Toast.makeText(getApplicationContext(), name + " er nå oppdatert!", Toast.LENGTH_LONG).show();
					}
				});
				bDelete = (Button)findViewById(R.id.adminmember_delete);
				bDelete.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						builder.create().show();
					}
				});
				

		}
	}

}
