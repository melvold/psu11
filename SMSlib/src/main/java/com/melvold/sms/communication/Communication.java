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

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;

import com.melvold.sms.crypto.CryptoUtils;
import com.melvold.sms.macros.Macros;

import de.rtner.misc.BinTools;




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
			}else{
				this.authenticated = true;
				System.out.println("++++++++++AUTHENTICATED");
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
		long t = System.currentTimeMillis();
		System.out.println("0 :++++++++++STARTING AUTHENTICATE");
		
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpClient client = getNewSecureAuthHttpClient(params);
		
		HttpPost post = new HttpPost("/" + Macros.FOLDER + "/login.php");
		
		HttpResponse response = null;

		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();

		nvp.add(new BasicNameValuePair("bid", bid));
		nvp.add(new BasicNameValuePair("salt", "salt"));
		post.setEntity(new UrlEncodedFormEntity(nvp));
		System.out.println(System.currentTimeMillis() - t+": "+"++++++++++GETTING SALT");
		response = client.execute(this.getHttpHost(), post, getAuthCacheContext());
		System.out.println("+++++++++EXECUTE DONE");
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
	
		String result = responseToString(is);
		System.out.println("++++++++++"+result);
		ArrayList<ArrayList<String>> array = stringToArray(result);
		byte[] salt = new byte[16];
		if(!array.isEmpty()){
			salt = BinTools.hex2bin(array.get(0).get(0));
			System.out.println(System.currentTimeMillis() - t+": "+"++++++++++GOT SALT");
			this.sessionId = response.getFirstHeader("Set-Cookie").getValue();
		}else{
			return false;
		}
		nvp.clear();
		System.out.println(System.currentTimeMillis() - t+": "+"++++++++++GENERATING KEY");
		String key = CryptoUtils.generatePBKDF2Key(salt, CryptoUtils.md5(password));
		System.out.println(System.currentTimeMillis() - t+": "+"++++++++++KEY GENERATED");
		nvp.add(new BasicNameValuePair("bid", bid));
		nvp.add(new BasicNameValuePair("password", key));
		post.setEntity(new UrlEncodedFormEntity(nvp));
		post.addHeader("Cookie", getSessionId());

		response = null;
		try {
			System.out.println(System.currentTimeMillis() - t+": "+"++++++++++SENDING KEY");
			response = client.execute(this.getHttpHost(), post, getAuthCacheContext());
			//			System.out.println("\nAuthentication SERVER:");
			//			for(Header h : response.getAllHeaders()) {
			//				System.out.println(h.getName() + " " + h.getValue());
			//			}

			entity = response.getEntity();
			is = entity.getContent();

			result = responseToString(is);
			if(stringToArray(result).size() > 0){
				System.out.println(System.currentTimeMillis() - t+": "+"++++++++++GOT OK RESULT CODE");
				return true;
			}else{
				System.out.println(System.currentTimeMillis() - t+": "+"++++++++++GOT BAD RESULT CODE");
				return false;
			}
				
			//System.out.println(result);
			
			//return response.getStatusLine().getStatusCode() == 200;

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
		
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpClient client = getNewSecureAuthHttpClient(params);
		
		HttpPost post = new HttpPost("/" + Macros.FOLDER + "/" + script);

		try {
			post.setEntity(new UrlEncodedFormEntity(nvp));
			post.addHeader("Cookie", getSessionId());

			HttpResponse response = client.execute(this.getHttpHost(), post, getAuthCacheContext());

			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();

			String result = responseToString(is);
			//System.out.println(result);
			ArrayList<ArrayList<String>> array = stringToArray(result);
			String test = "";
			for(int i = 0; i < array.size(); i++){
				for(int j = 0; j < array.get(i).size(); j++){
					test+= array.get(i).get(j) + ", ";
				}
				test+="\n-------------------------\n";
			}
			if(test!=""){
				System.out.println(test);
			}

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
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"ISO8859-1"));
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
	private SecureHttpClient getNewSecureAuthHttpClient(HttpParams params) {
		SecureHttpClient client = new SecureHttpClient(params);
		addBasicAuth(client);

		HttpRequestInterceptor preemptiveAuth = new HttpRequestProcesser();
		client.addRequestInterceptor(preemptiveAuth, 0);

		return client;
	}

	//Adding basic authentication credentials
	private void addBasicAuth(DefaultHttpClient client) {
		client.getCredentialsProvider().setCredentials(new AuthScope(this.getHost(), this.getPort()),
				new UsernamePasswordCredentials("Eivind", ""));
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
	
	public boolean isAuthenticated(){
		return this.authenticated;
	}

}
