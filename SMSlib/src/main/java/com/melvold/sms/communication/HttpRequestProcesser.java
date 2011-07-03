package com.melvold.sms.communication;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;



public class HttpRequestProcesser implements HttpRequestInterceptor {

	public void process(HttpRequest request, HttpContext context)
	throws HttpException, IOException {
		//Contains information about the state of the authentication process
		AuthState authState = (AuthState)context.getAttribute(ClientContext.TARGET_AUTH_STATE);
		//Provide credentials for the authentication scope
		CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
		//Holds all of the variables needed to describe an HTTP connection to a host
		HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

		if (authState.getAuthScheme() == null) {
			AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
			Credentials creds = credsProvider.getCredentials(authScope);
			if (creds != null) {
				authState.setAuthScheme(new BasicScheme());
				authState.setCredentials(creds);
			}
		}

	}

}
