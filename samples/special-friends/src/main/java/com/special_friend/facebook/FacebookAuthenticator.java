package com.special_friend.facebook;

import jsimple.net.HttpRequest;
import jsimple.oauth.builder.ServiceBuilder;
import jsimple.oauth.builder.api.FacebookOAuthApi;
import jsimple.oauth.oauth.OAuthService;

public class FacebookAuthenticator extends Connector{
	private OAuthService authService;
	
	/**
	 * End-point for code -> access token exchange
	 * @param code
	 * @return
	 */
	private String getAcessTokenURL(String code)  {
		return "https://graph.facebook.com/oauth/access_token?" + "client_id=" + Constants.FACEBOOK_APP_ID + "&redirect_uri=" + Constants.FACEBOOK_CALLBACK + "&client_secret=" + Constants.FACEBOOK_APP_SECRET + "&code=" + code;
	}

	/**
	 * Will build a jsimple facebook oauth service
	 * @param scope the permissions that the application will require from the user : comma separated strings
	 */
	public FacebookAuthenticator(String scope) {
		ServiceBuilder serviceBuilder = new ServiceBuilder();
		serviceBuilder.provider(new FacebookOAuthApi());
		serviceBuilder.apiKey(Constants.FACEBOOK_APP_ID);
		serviceBuilder.apiSecret(Constants.FACEBOOK_APP_SECRET);
		serviceBuilder.callback(Constants.FACEBOOK_CALLBACK);
		serviceBuilder.scope(scope);
		
		authService = serviceBuilder.build();
	}
	/**
	 * Wrapper method for jsimble facebook authorization so authUrl can be extracted
	 * @return
	 */
	public String getAuthorizationUrl() {
		return authService.getAuthorizationUrl(null);
	}
	
	/**
	 * Exchanges the facebook authorization code for an acess token
	 * @param code
	 * @return
	 */
	public String getAcessToken(String code) {
		//send the code to the code to access_token url
		String requestURL = getAcessTokenURL(code);
		HttpRequest request = HttpRequest.create(requestURL);
		//receive the acess token and the expiration time 
		String response = sendRequest(request);
		
		//we dont need the expiration time parameter for the scope of this application: remove it
		int trimIndex = response.indexOf("&");
		response = response.substring(0, trimIndex); 
		//we need only the acess token value , not the metadata
		response = response.replaceFirst("access_token=", "");
		return response;
	}
}
