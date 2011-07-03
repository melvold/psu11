package com.melvold.sms.dbinterface;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.melvold.sms.communication.Communication;

public class DBInterface {

	public Byte salt;
	public Communication communication;

	public DBInterface(String host, String bid, String password){
		this.salt = null;
		this.communication = null;
		this.communication = new Communication(host, bid, password, 443, "https");
	}

	public void testConnection(){
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("bid", "lam002"));
		nvp.add(new BasicNameValuePair("bid2", "iam001"));
		JSONArray ans = this.communication.post("test.php", nvp);
		try {
			System.out.println("JSON object is: " + ans.getJSONObject(1).getInt("u_id"));
			System.out.println("SUCCESS: Session is preserved!");
		} catch (JSONException e) {
			System.out.println("\nJSONEXCEPTION!");
			System.out.println("ERROR: The utilized session does not exist! ");
		} catch (NullPointerException e) {
			System.out.println("\nNULLPOINTEREXCEPTION!");
			System.out.println("ERROR: The utilized session does not exist! ");
		}
	}

	public static void main(String[] args){
		DBInterface dbi = new DBInterface("localhost", "lam002", "mikkel96");
		dbi.testConnection();
	}

}
