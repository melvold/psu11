package com.melvold.sms.client.activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melvold.sms.R;
import com.melvold.sms.client.lists.MemberList;
import com.melvold.sms.client.utils.ShowAdapter;

public class ShowMembersActivity extends ListActivity {

	
	private ArrayList<ArrayList<String>> members;
	private boolean admin;
	private String group;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listmembers);
		group = getIntent().getStringExtra("GID");
		admin = getIntent().getBooleanExtra("ADMIN", false);
	}
	
	protected void onResume(){
		super.onResume();
		
		if(group != null && !group.equals("")){
			members = MainMenuActivity.dbi.listMembersInGroup(group);
		}else if(admin){
			members = MainMenuActivity.dbi.listAllMembersWithTlf();
		}else{
			finish();
		}
		//System.out.println(members.get(0).get(0) + " " + members.get(0).get(1) + " " + members.get(0).get(2) + " ");
		MemberList ml = new MemberList(members);
		
		ShowAdapter sa = new ShowAdapter(this, R.layout.custom_row, (ArrayList<String>)ml.getList());
		setListAdapter(sa);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		registerForContextMenu(lv);

		lv.setOnItemClickListener(new OnItemClickListener() {
		
		public void onItemClick(AdapterView<?> parent, View view,
		int position, long id) {
			switch (position) {
				default:
					if(admin){
						Intent intent = new Intent();
						intent.putExtra("UID", members.get(position).get(0));
						intent.putExtra("BID", members.get(position).get(1));
						intent.putExtra("NAME", members.get(position).get(2) + " " + members.get(position).get(3));
						intent.putExtra("PHONE", members.get(position).get(4));
						intent.putExtra("PHONETYPE", members.get(position).get(5));
						intent.putExtra("STATUS", members.get(position).get(6));
						intent.setClass(getApplicationContext(), ShowMemberActivity.class);
						startActivity(intent);
					}
					break;
				}
		}
		
	});
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);  
	    menu.setHeaderTitle("Valg");  
	    menu.add(0, v.getId(), 0, "Ring nummer");  
	    menu.add(0, v.getId(), 0, "Kopier nummer");  
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		if (info != null) {
			LinearLayout ll1 = (LinearLayout) info.targetView;
			LinearLayout ll2 = (LinearLayout) ll1.getChildAt(1);
			TextView tv = (TextView) ll2.getChildAt(1);
			if(item.getTitle()=="Ring nummer"){
			  //Toast.makeText(getApplicationContext(), "Ring nummer " + tv.getText().toString(), Toast.LENGTH_LONG).show();
			  try {
			        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv.getText().toString())));
			      } catch (Exception e) {
			        e.printStackTrace();
			      }
			  return true;
			} else if(item.getTitle()=="Kopier nummer"){
			  Toast.makeText(getApplicationContext(), "Kopierte " + tv.getText().toString(), Toast.LENGTH_LONG).show();
			  ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
			  clipboard.setText(tv.getText().toString());
			  return true;
			}
		}
		  return false;
	}
	
}
