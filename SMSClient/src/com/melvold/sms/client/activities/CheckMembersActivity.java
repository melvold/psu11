
package com.melvold.sms.client.activities;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.melvold.projects.sms.R;
import com.melvold.sms.client.lists.MemberCheckList;
import com.melvold.sms.client.utils.CheckAdapter;
import com.melvold.sms.client.workers.SendMemberMessageThread;

public class CheckMembersActivity extends ListActivity {

	
	private EditText et_message;
	private Button b_send;
	private ListView lv;
	private MemberCheckList ml;
	private CheckAdapter ca;
	private int numberToSend;
	
    private SendMemberMessageThread progThread;
    private ProgressDialog progDialog;
    
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int total = msg.getData().getInt("total");
            progDialog.setProgress(total);
            if (total >= numberToSend){
                dismissDialog(1);
            }
        }
    };
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendlistlayout);
		numberToSend = 0;
		
		ml = new MemberCheckList(MainMenuActivity.dbi.listAllMembersWithTlf());
		
		lv = getListView();//Calling getView on ListAdapter
		lv.setTextFilterEnabled(true);
		lv.setCacheColorHint(0);
		
		ca = new CheckAdapter(CheckMembersActivity.this, R.layout.cb_custom_row, (ArrayList<String>) ml.getList());
		lv.setAdapter(ca);
		lv.setItemsCanFocus(false);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		et_message = (EditText)findViewById(R.id.et_send_members);
		b_send = (Button)findViewById(R.id.b_send_members);
		
		b_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(et_message.getText().toString()!=null && !et_message.getText().toString().equals("")){
					if(ca.getSelectedRows().size() < 1){
						Toast.makeText(getApplicationContext(), "Ingen personer er valgt!", Toast.LENGTH_LONG).show();
					}else{
						numberToSend = ca.getSelectedRows().size();
						showDialog(1);
					}
				}else{
					Toast.makeText(getApplicationContext(), "Vennligst skriv en melding!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.check_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menu_back:
	        finish();
	        return true;
	    case R.id.menu_check_all:
	        ca.selectAllRows();
	        return true;
	    case R.id.menu_clear_check_all:
	        ca.deselectAllRows();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
        default:
            progDialog = new ProgressDialog(this);
            progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progDialog.setMax(numberToSend);
            progDialog.setMessage("Sender melding til valgte mottakere:");
            progThread = new SendMemberMessageThread(handler, getApplicationContext(), ca, et_message);
            progThread.start();
            return progDialog;
        }
    }
}
