package com.melvold.sms.lib.communication;

import java.io.BufferedReader;



import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import android.content.Context;
import com.melvold.sms.lib.crypto.CryptoUtils;

import de.rtner.misc.BinTools;

public class Communication {

	private String server;
	private String folder;
	private boolean authenticated;
	private String sessionId;
	private HttpClient client;

	public Communication(Context con, String serv, String dir, String bid, String password){
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
		client = new SecureHttpClient(con, params, "password".toCharArray(), "password".toCharArray());
		sessionId = null;
		server = serv;
		folder = dir;
		
		try {
			if(!this.authenticate(bid, password)) {
				throw new AuthenticationException();
			}else{
				this.authenticated = true;
				System.out.println("AUTHENTICATED");
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}

	private boolean authenticate(String bid, String password){
		long t = System.currentTimeMillis();

		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("bid", bid));
		nvp.add(new BasicNameValuePair("salt", "salt"));
		
		ArrayList<ArrayList<String>> array = get("login.php", nvp);
		byte[] salt = new byte[16];
		if(!array.isEmpty()){
			salt = BinTools.hex2bin(array.get(0).get(0));
			System.out.println(System.currentTimeMillis() - t+": "+"GOT SALT");
		}else{
			return false;
		}
		
		String key = CryptoUtils.generatePBKDF2Key(salt, CryptoUtils.md5(password));
		System.out.println(System.currentTimeMillis() - t+": "+"KEY GENERATED");
		
		nvp.clear();
		nvp.add(new BasicNameValuePair("bid", bid));
		nvp.add(new BasicNameValuePair("password", key));
		
		if(post("login.php", nvp)){
			System.out.println(System.currentTimeMillis() - t+": "+"GOT OK RESULT CODE");
			return true;
		}else{
			System.out.println(System.currentTimeMillis() - t+": "+"GOT BAD RESULT CODE");
			return false;
		}
	}
	
	public ArrayList<ArrayList<String>> get(String script, ArrayList<NameValuePair> nvp){
		URI uri;
		try {
			uri = URIUtils.createURI("https", getServer(), -1, getFolder() + "/" + script, 
			    URLEncodedUtils.format(nvp, "UTF-8"), null);
		} catch (URISyntaxException e) {
			System.out.println("ERROR: Failed to create URI");
			return null;
		}
		HttpGet get = new HttpGet(uri);
		System.out.println("Getting " + get.getURI());
		if(sessionId!=null){
			get.addHeader("Cookie", sessionId);
		}
		try {
			HttpResponse response = client.execute(get);
			InputStream in = response.getEntity().getContent();
			char c;
			String resp = "";
			while((c = (char)in.read()) != -1){
				resp+=c;
			}
			System.out.println("RECEIVED:  " + resp);
			
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				String result = responseToString(is);
				System.out.println(result);
				
				if(sessionId==null){
					this.sessionId = response.getFirstHeader("Set-Cookie").getValue();
				}
				return stringToArray(result);
			}
				
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean post(String script, List<NameValuePair> formparams){
		HttpPost post = new HttpPost("https" + "://" + getServer() + getFolder() + "/" + script);
		System.out.println("POSTING " + post.getRequestLine().getUri());
		
		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			post.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			System.out.println("ERROR: Could not support encoding!");
			return false;
		}
		if(sessionId!=null){
			post.addHeader("Cookie", sessionId);
		}
		try {
			HttpResponse response = client.execute(post);
			System.out.println(responseToString(response.getEntity().getContent()));
			if(response.getStatusLine().getStatusCode() == 200){
				if(sessionId==null){
					this.sessionId = response.getFirstHeader("Set-Cookie").getValue();
				}
				return true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String responseToString(InputStream is) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"ISO8859-1"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		return sb.toString();
	}

	private static ArrayList<ArrayList<String>> stringToArray(String result) {
		ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
		Pattern rowPattern = Pattern.compile("<row>.*?</row>");
		Matcher m1 = rowPattern.matcher(result);
		String row = "";
		String id = "";
		int i = 0;
		while (m1.find()) {
			row = m1.group();
			row = row.substring(5, row.length() - 6);
			Pattern idPattern = Pattern.compile("<id>.*?</id>");
			Matcher m2 = idPattern.matcher(row);

			ArrayList<String> rowArray = new ArrayList<String>();
			int j = 0;
			while (m2.find()) {
				id = m2.group();
				id = id.substring(4, id.length() - 5);
				rowArray.add(id);
				j++;
			}
			array.add(rowArray);
			i++;
		}
		return array;
	}

	public String getServer(){
		return this.server;
	}
	
	public String getFolder(){
		return this.folder;
	}
	
	public boolean isAuthenticated(){
		return this.authenticated;
	}

}
