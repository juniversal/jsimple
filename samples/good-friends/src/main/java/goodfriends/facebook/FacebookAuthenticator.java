package goodfriends.facebook;

import goodfriends.util.Connector;
import goodfriends.util.Constants;
import jsimple.oauth.builder.ServiceBuilder;
import jsimple.oauth.builder.api.FacebookOAuthApi;
import jsimple.oauth.model.Token;
import jsimple.oauth.model.Verifier;
import jsimple.oauth.oauth.OAuthService;

/**
 * Handles facebook authentication routine
 */
public class FacebookAuthenticator extends Connector {
    private OAuthService authService;

    /**
     * Will build a jsimple facebook oauth service
     *
     * @param scope the permissions that the application will require from the  user : comma separated strings
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

    public String getAuthorizationUrl() {
        return authService.getAuthorizationUrl(null);
    }

    public Token getAccessToken(String code) {
        Verifier ver = new Verifier(code);
        return authService.getAccessToken(null, ver);
    }

    public Token getRefreshedToken(Token expiredAccessToken) {
        authService.refreshAccessToken(expiredAccessToken, true);
        return expiredAccessToken;
    }
}
