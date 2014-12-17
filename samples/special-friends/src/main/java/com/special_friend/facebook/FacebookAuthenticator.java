package main.java.com.special_friend.facebook;

import jsimple.oauth.builder.ServiceBuilder;
import jsimple.oauth.builder.api.FacebookOAuthApi;
import jsimple.oauth.model.Token;
import jsimple.oauth.model.Verifier;
import jsimple.oauth.oauth.OAuthService;
import main.java.com.special_friend.util.Connector;
import main.java.com.special_friend.util.Constants;

/**
 * Handles facebook authentication routine
 */
public class FacebookAuthenticator extends Connector {
	private OAuthService authService;

	/**
	 * Will build a jsimple facebook oauth service
	 * @param scope the permissions that the application will require from the  user : comma separated strings*/
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

	public Token getAccessToken(String code) {
		Verifier ver = new Verifier(code);
		Token tok = authService.getAccessToken(null, ver);
		return tok;
	}
	
	public Token getRefreshedToken(Token expiredAcessToken) {
		authService.refreshAccessToken(expiredAcessToken, true);
		return expiredAcessToken;

	}
}
