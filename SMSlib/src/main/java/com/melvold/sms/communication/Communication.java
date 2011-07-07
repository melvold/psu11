package com.melvold.sms.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.AuthenticationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;

import com.melvold.sms.crypto.CryptoUtils;
import com.melvold.sms.macros.Macros;


public class Communication {

	private String host;
	private int port;
	private HttpHost httpHost;
	private boolean authenticated;
	private String sessionId;

	public Communication(String host, String bid, String password, int port, String scheme){
		this.host = host;
		this.port = port;
		this.authenticated = false;
		this.httpHost = new HttpHost(this.host, this.port, scheme);
		this.sessionId = null;

		try {
			if(!this.authenticate(bid, password)) {
				throw new AuthenticationException();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean authenticate(String bid, String password) throws ClientProtocolException, IOException, NoSuchAlgorithmException {
		HttpClient client = getNewSecureAuthHttpClient();
		HttpPost post = new HttpPost("/" + Macros.FOLDER + "/login.php");

		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("bid", bid));
		nvp.add(new BasicNameValuePair("password", CryptoUtils.md5(password)));
		post.setEntity(new UrlEncodedFormEntity(nvp));

		HttpResponse response = null;
		try {
			response = client.execute(this.getHttpHost(), post, getAuthCacheContext());
			//			System.out.println("\nAuthentication SERVER:");
			//			for(Header h : response.getAllHeaders()) {
			//				System.out.println(h.getName() + " " + h.getValue());
			//			}

			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();

			String result = responseToString(is);
			System.out.println(result);
			//JSONArray jArray = new JSONArray(result);

			this.sessionId = response.getFirstHeader("Set-Cookie").getValue();
			this.authenticated = true;
			return response.getStatusLine().getStatusCode() == 200;

		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	public ArrayList<ArrayList<String>> post(String script, ArrayList<NameValuePair> nvp){
		if(!this.authenticated) {
			return null;
		}
		HttpClient client = getNewSecureAuthHttpClient();
		HttpPost post = new HttpPost("/" + Macros.FOLDER + "/" + script);

		try {
			post.setEntity(new UrlEncodedFormEntity(nvp));
			post.addHeader("Cookie", getSessionId());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			HttpResponse response = client.execute(this.getHttpHost(), post, getAuthCacheContext());

			//			System.out.println("\nPOST SERVER:");
			//			for(Header h : response.getAllHeaders()) {
			//				System.out.println(h.getName() + " " + h.getValue());
			//			}
			//			System.out.println(response.getStatusLine().getStatusCode());

			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();

			String result = responseToString(is);
			System.out.println(result);
			ArrayList<ArrayList<String>> array = stringToArray(result);
			String test = "";
			for(int i = 0; i < array.size(); i++){
				for(int j = 0; j < array.get(i).size(); j++){
					test+= array.get(i).get(j) + ", ";
				}
				test+="\n-------------------------\n";
			}
			System.out.println(test);

			return array;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}


	private static String responseToString(InputStream is) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"ISO8859-1"));//TODO: UTF8?
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		return sb.toString();
	}

	private ArrayList<ArrayList<String>> stringToArray(String result) {
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

	//Creating new Https client
	private SecureHttpClient getNewSecureAuthHttpClient() {
		SecureHttpClient client = new SecureHttpClient();
		addBasicAuth(client);

		HttpRequestInterceptor preemptiveAuth = new HttpRequestProcesser();
		client.addRequestInterceptor(preemptiveAuth, 0);

		return client;
	}

	//Adding basic authentication credentials
	private void addBasicAuth(DefaultHttpClient client) {
		client.getCredentialsProvider().setCredentials(new AuthScope(this.getHost(), this.getPort()),
				new UsernamePasswordCredentials("username", null));
		return;
	}

	//Setting authentication cache context
	private BasicHttpContext getAuthCacheContext() {
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(this.getHttpHost(), basicAuth);

		BasicHttpContext localcontext = new BasicHttpContext();
		localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
		return localcontext;
	}

	public int getPort(){
		return this.port;
	}

	public String getHost(){
		return this.host;
	}

	public HttpHost getHttpHost(){
		return this.httpHost;
	}

	public String getSessionId(){
		return this.sessionId;
	}

}
