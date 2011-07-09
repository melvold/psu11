package com.melvold.sms.client;

import android.app.Activity;
import android.os.Bundle;

import com.melvold.projects.sms.R;

public class ToastMessageActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		/*		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast,
				(ViewGroup) findViewById(R.id.toast));

		ImageView image = (ImageView) layout.findViewById(R.id.image);
		image.setImageResource(getIntent().getExtras().getInt("image"));
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(getIntent().getExtras().getString("message"));

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();*/

	}

}