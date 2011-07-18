package com.melvold.sms.client.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class smsUtils {

	public static boolean isPhoneNumber(String number){
		if(number != null){
			String tmp = number;
			if(number.charAt(0) == '+'){
				tmp = tmp.substring(3);
			}else if(number.charAt(0) == '0' && number.charAt(1) == '0'){
				tmp = tmp.substring(4);
			}
			
		    Pattern pattern = Pattern.compile("\\d{8}");
		    Matcher matcher = pattern.matcher(tmp);
		 
		    if (matcher.matches()) {
		    	  return true;
		    }
		}
		return false;
	}
}
