package com.melvold.sms.client.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.melvold.sms.R;

public class ExitMessageActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast,
				(ViewGroup) findViewById(R.id.toast));
		ImageView image = (ImageView) layout.findViewById(R.id.image);
		image.setImageResource(getIntent().getIntExtra("IMAGE", 0x7f050007));
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(getIntent().getStringExtra("TEXT"));
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 10, 10);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
        new Thread(new Runnable() {
       
            public void run() {
                try {
                    Thread.sleep(3000);
                }
                catch (Exception e) { }
                setResult(RESULT_OK);
                finish();
            }
        }).start();
	}
}
