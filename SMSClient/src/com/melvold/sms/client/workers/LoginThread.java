package com.melvold.sms.client.workers;

import com.melvold.sms.client.activities.MainMenuActivity;
import com.melvold.sms.client.utils.Macros;
import com.melvold.sms.dbinterface.DBInterface;

public class LoginThread extends Thread {

	private String userId;
	private String password;
	
	public LoginThread(String user, String pwd){
		userId = user;
		password = pwd;
	}
	
	@Override
	public void run(){
		MainMenuActivity.dbi = new DBInterface(Macros.SERVER, Macros.FOLDER, userId, password);
	}
}
