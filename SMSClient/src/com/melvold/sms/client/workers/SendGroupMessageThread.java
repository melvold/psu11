package com.melvold.sms.client.workers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.Toast;

import com.melvold.sms.client.activities.MainMenuActivity;
import com.melvold.sms.client.lists.MemberCheckList;
import com.melvold.sms.client.utils.CheckAdapter;
import com.melvold.sms.client.utils.Macros;
import com.melvold.sms.client.utils.smsUtils;

public class SendGroupMessageThread extends Thread {	
    
    public final static int DONE = 0;
    public final static int RUNNING = 1;
    
    private Handler mHandler;
    private CheckAdapter ca;
    private Context context;
    private EditText message;
    
    public SendGroupMessageThread(Handler h, Context con, CheckAdapter check, EditText et) {
        mHandler = h;
        ca = check;
        context = con;
        message = et;
    }
 
    @Override
    public void run() {
        int count = 0;
        for(int i = 0; i < ca.getSelectedRows().size(); i++){
			String group = ca.getSelectedRows().get(i);
			MemberCheckList groupmembers = new MemberCheckList(MainMenuActivity.dbi.listMembersInGroup(group));
			for(int j = 0; j < groupmembers.getList().size(); j++){
				String person = groupmembers.getList().get(i);
				String[] person_values = groupmembers.getList().get(j).split("\n");
				if(person_values.length!=2 || !smsUtils.isPhoneNumber(person_values[1])){
					System.out.println("+++++++ tmp.length = " + person_values.length);
					System.out.println("+++++++ isphonenumber = " + smsUtils.isPhoneNumber(person_values[1]));
					Toast.makeText(context, "Sendte ikke til:\n" + person + "\nda personen er registrert feil!", Toast.LENGTH_LONG).show();
					Toast.makeText(context, "Kontakt administrator for riktig registrering av personen!", Toast.LENGTH_LONG).show();
				}else{
					MainMenuActivity.dbi.sendMessage(Macros.server_number, smsUtils.makeInternationalNumber(person_values[1], "47"), message.getText().toString());
		            // Send message (with current value of  total as data) to Handler on UI thread
		            // so that it can update the progress bar.
		            Message msg = mHandler.obtainMessage();
		            Bundle b = new Bundle();
		            b.putInt("total", ++count);
		            msg.setData(b);
		            mHandler.sendMessage(msg);
				}
			}
		}
    }
    
}
