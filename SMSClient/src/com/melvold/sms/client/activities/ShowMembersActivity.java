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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.melvold.sms.R;
import com.melvold.sms.client.lists.MemberList;

public class ShowMembersActivity extends ListActivity {

	
	private ArrayList<ArrayList<String>> members;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String group = getIntent().getStringExtra("GROUP");
		
		if(!group.equals(null)){
			members = MainMenuActivity.dbi.listMembersInGroup(group);
		}else{
			finish();
		}
		
		MemberList ml = new MemberList(members);
		
		SimpleAdapter sa = new SimpleAdapter(this, ml.getList(), R.layout.listmembers, ml.getFrom(), ml.getTo());
		setListAdapter(sa);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		registerForContextMenu(lv);

		lv.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
		int position, long id) {
			switch (position) {
				default:
					//Toast.makeText(getApplicationContext(), "Clicked on member " + position, Toast.LENGTH_LONG).show();
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
