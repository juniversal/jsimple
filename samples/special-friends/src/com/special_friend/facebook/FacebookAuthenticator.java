package com.special_friend.facebook;

import java.io.UnsupportedEncodingException;

import jsimple.net.HttpRequest;
import jsimple.oauth.builder.ServiceBuilder;
import jsimple.oauth.builder.api.FacebookOAuthApi;
import jsimple.oauth.oauth.OAuthService;

public class FacebookAuthenticator extends Connector{
	private OAuthService authService;
	
	private String getAcessTokenURL(String code) throws UnsupportedEncodingException {
		return "https://graph.facebook.com/oauth/access_token?" + "client_id=" + Constants.FACEBOOK_APP_ID + "&redirect_uri=" + Constants.FACEBOOK_CALLBACK + "&client_secret=" + Constants.FACEBOOK_APP_SECRET + "&code=" + code;
	}

	
	public FacebookAuthenticator(String scope) {
		ServiceBuilder serviceBuilder = new ServiceBuilder();
		serviceBuilder.provider(new FacebookOAuthApi());
		serviceBuilder.apiKey(Constants.FACEBOOK_APP_ID);
		serviceBuilder.apiSecret(Constants.FACEBOOK_APP_SECRET);
		serviceBuilder.callback(Constants.FACEBOOK_CALLBACK);
		serviceBuilder.scope(scope);
		
		authService = serviceBuilder.build();
	}
	
	public String getAuthorizationUrl() {
		return authService.getAuthorizationUrl(null);
	}
	
	public String getAcessToken(String code) throws UnsupportedEncodingException{
		String requestURL = getAcessTokenURL(code);
		HttpRequest request = HttpRequest.create(requestURL);
		String response = sendRequest(request);
		//remove "&expires=5182730" param
		int trimIndex = response.indexOf("&");
		response = response.substring(0, trimIndex); 
		response = response.replaceFirst("access_token=", "");
		return response;
	}
}
