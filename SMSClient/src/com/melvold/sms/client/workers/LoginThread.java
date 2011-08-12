package com.melvold.sms.client.workers;

import android.content.Context;

import com.melvold.sms.client.activities.MainMenuActivity;
import com.melvold.sms.client.utils.Macros;
import com.melvold.sms.lib.dbinterface.DBInterface;

public class LoginThread extends Thread {

	private String userId;
	private String password;
	private Context context;
	
	public LoginThread(Context con, String user, String pwd){
		userId = user;
		password = pwd;
		context = con;
	}
	
	@Override
	public void run(){
		MainMenuActivity.dbi = new DBInterface(context, Macros.SERVER, Macros.FOLDER, userId, password);
	}
}
