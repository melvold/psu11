package com.melvold.sms.lib.communication;
import java.io.IOException;



import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;

import android.content.Context;

import com.melvold.sms.lib.R;


public class SecureHttpClient extends DefaultHttpClient {

	private char[] tsPassword;
	private char[] ksPassword;
	private Context context;
	
	public SecureHttpClient(Context con, HttpParams params, char[] tspwd, char[] kspwd){
		super(params);
		tsPassword = tspwd;
		ksPassword = kspwd;
		context = con;
	}
	
	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		return new SingleClientConnManager(getParams(), getRegistry());
	}
	
	private SchemeRegistry getRegistry(){
		Scheme https = new Scheme("https", getSocketFactory(), 443);
		SchemeRegistry sr = new SchemeRegistry();
		sr.register(https);
		return sr;
	}
	
	private SSLSocketFactory getSocketFactory(){
		//Set up secure socket
		//SSLContext sslcon;
		try {
			//sslcon = SSLContext.getInstance("TLSv1");
	        
	        //KeyManager[] 		arg1 selects the authentication credentials that will be sent to the remote server
	        //TrustManager[] 	arg2 defines which remote certificates should be trusted during authorization
	        //SecureRandom		arg3 defines a random number generator, which is default if null is used
			KeyStore ks = getKeyStore();
			KeyStore ts = getTrustStore();
			//sslcon.init(getKeyManagers(ks), getTrustManagers(ts), null);
			
			//Create socket factory for creating desirable sockets
			SSLSocketFactory sf = new SSLSocketFactory(SSLSocketFactory.TLS, ks, ksPassword.toString(), ts, null, null); //new SSLSocketFactory(sslcon, SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
			sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
			
			return sf;
	
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
/*	private TrustManager[] getTrustManagers(KeyStore ks){
		//TMF queries KeyStore ks to find which remote certificates should be trusted during authorization
        TrustManagerFactory tmf;
		try {
			tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	        tmf.init(ks);
	        return tmf.getTrustManagers();
		
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
	}

	private KeyManager[] getKeyManagers(KeyStore ks){
		//KMF uses the credentials given in the keystore to authenticate to the remote server
		try {
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, ksPassword);
			return kmf.getKeyManagers();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		return null;
	}*/
	
	private KeyStore getTrustStore(){
		KeyStore ts;
		try {
			ts = KeyStore.getInstance("BKS");
	        InputStream is = context.getResources().openRawResource(R.raw.truststore);
	        ts.load(is, tsPassword);
	        is.close();
	        return ts;
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private KeyStore getKeyStore(){
        //Get or Create file to store certificates and private key
        KeyStore ks;
		try {
			ks = KeyStore.getInstance("PKCS12");
        	InputStream is = context.getResources().openRawResource(R.raw.keystore);
        	ks.load(is, ksPassword);
        	is.close();
	        
	        return ks;
		
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
