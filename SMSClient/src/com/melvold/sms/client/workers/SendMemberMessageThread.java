package com.melvold.sms.client.workers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.Toast;

import com.melvold.sms.client.activities.MainMenuActivity;
import com.melvold.sms.client.utils.CheckAdapter;
import com.melvold.sms.client.utils.Macros;
import com.melvold.sms.client.utils.smsUtils;

public class SendMemberMessageThread extends Thread {
	   
    public final static int DONE = 0;
    public final static int RUNNING = 1;
    
    private Handler mHandler;
    private CheckAdapter ca;
    private Context context;
    private EditText message;
    
    public SendMemberMessageThread(Handler h, Context con, CheckAdapter check, EditText et) {
        mHandler = h;
        ca = check;
        context = con;
        message = et;
    }
 
    @Override
    public void run() {
        int count = 0;
		for(int i = 0; i < ca.getSelectedRows().size(); i++){
			String person = ca.getSelectedRows().get(i);
			String[] tmp = ca.getSelectedRows().get(i).split("\n");
			if(tmp.length!=2 || !smsUtils.isPhoneNumber(tmp[1])){
				System.out.println("+++++++ tmp.length = " + tmp.length);
				System.out.println("+++++++ isphonenumber = " + smsUtils.isPhoneNumber(tmp[1]));
				Toast.makeText(context, "Sendte ikke til:\n" + person + "\nda personen er registrert feil!", Toast.LENGTH_LONG).show();
				Toast.makeText(context, "Kontakt administrator for riktig registrering av personen!", Toast.LENGTH_LONG).show();
			}else{
				MainMenuActivity.dbi.sendMessage(Macros.server_number, smsUtils.makeInternationalNumber(tmp[1], "47"), message.getText().toString());
	            
				Message msg = mHandler.obtainMessage();
	            Bundle b = new Bundle();
	            b.putInt("total", ++count);
	            msg.setData(b);
	            mHandler.sendMessage(msg);
			}
		}
    }
    
}
