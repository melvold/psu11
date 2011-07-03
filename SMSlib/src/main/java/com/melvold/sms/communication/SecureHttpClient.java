package com.melvold.sms.communication;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

public class SecureHttpClient extends DefaultHttpClient {
	// TODO: At the time this needs to be the correct certificate of the server
	public final String createCert = "-----BEGIN CERTIFICATE-----\n"
		+ "MIIC5jCCAk+gAwIBAgIBADANBgkqhkiG9w0BAQQFADBcMQswCQYDVQQGEwJERTEP\n"
		+ "MA0GA1UECBMGQmVybGluMQ8wDQYDVQQHEwZCZXJsaW4xFzAVBgNVBAoTDkFwYWNo\n"
		+ "ZSBGcmllbmRzMRIwEAYDVQQDEwlsb2NhbGhvc3QwHhcNMDQxMDAxMDkxMDMwWhcN\n"
		+ "MTAwOTMwMDkxMDMwWjBcMQswCQYDVQQGEwJERTEPMA0GA1UECBMGQmVybGluMQ8w\n"
		+ "DQYDVQQHEwZCZXJsaW4xFzAVBgNVBAoTDkFwYWNoZSBGcmllbmRzMRIwEAYDVQQD\n"
		+ "Ewlsb2NhbGhvc3QwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMzLZFTC+qN6\n"
		+ "gTZfG9UQgXW3QgIxg7HVWnZyane+YmkWq+s5ZrUgOTPRtAF9I0AknmAcqDKD6p3x\n"
		+ "8tnwGIWd4cDimf+JpPkVvV26PzkuJhRIgHXvtcCUbipi0kI0LEoVF1iwVZgRbpH9\n"
		+ "KA2AxSHCPvt4bzgxSnjygS2Fybgr8YbJAgMBAAGjgbcwgbQwHQYDVR0OBBYEFBP8\n"
		+ "X524EngQ0fE/DlKqi6VEk8dSMIGEBgNVHSMEfTB7gBQT/F+duBJ4ENHxPw5Sqoul\n"
		+ "RJPHUqFgpF4wXDELMAkGA1UEBhMCREUxDzANBgNVBAgTBkJlcmxpbjEPMA0GA1UE\n"
		+ "BxMGQmVybGluMRcwFQYDVQQKEw5BcGFjaGUgRnJpZW5kczESMBAGA1UEAxMJbG9j\n"
		+ "YWxob3N0ggEAMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEEBQADgYEAFaDLTAkk\n"
		+ "p8J2SJ84I7Fp6UVfnpnbkdE2SBLFRKccSYZpoX85J2Z7qmfaQ35p/ZJySLuOQGv/\n"
		+ "IHlXFTt9VWT8meCpubcFl/mI701KBGhAX0DwD5OmkiLk3yGOREhy4Q8ZI+Eg75k7\n"
		+ "WF65KAis5duvvVevPR1CwBk7H9CDe8czwrc=\n"
		+ "-----END CERTIFICATE-----\n";

	public SecureHttpClient() {
		super();
	}

	//Create connection manager supporting the https scheme
	@SuppressWarnings("deprecation")
	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		//Adding https scheme to registry
		registry.register(new Scheme("https", newSslSocketFactory(), 443));
		return new SingleClientConnManager(getParams(), registry);
	}

	//Create SSLSocketFactory to validate identity of the HTTPS server against trusted certificate and to authenticate client to server with private key.
	private SSLSocketFactory newSslSocketFactory() {

		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(null, null);
			//Contains a public key Certificate belonging to another party
			TrustedCertificateEntry certEntry = new TrustedCertificateEntry(generateCertificate());
			ks.setCertificateEntry("ServerCert", certEntry.getTrustedCertificate());
			//TODO: Write keystore fos, with users password?
			//System.out.println("Created SSLSocketFactory\n\t Used to:\n\to Validate the identity of the HTTPS server against trusted certificate\n\to Authenticate client to server with private key.)");
			return new SSLSocketFactory(ks);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	private X509Certificate generateCertificate() {
		ByteArrayInputStream is = new ByteArrayInputStream(
				this.createCert.getBytes());
		CertificateFactory cf;
		X509Certificate cert = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			cert = (X509Certificate) cf.generateCertificate(is);
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		return cert;
	}
}

