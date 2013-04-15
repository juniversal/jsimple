package jsimple.oauth.oauth;

import jsimple.oauth.model.OAuthRequest;
import jsimple.oauth.model.Token;
import jsimple.oauth.model.Verifier;

import javax.annotation.Nullable;

/**
 * The main Scribe object.
 * <p/>
 * A facade responsible for the retrieval of request and access tokens and for the signing of HTTP requests.
 *
 * @author Pablo Fernandez
 */
public interface OAuthService {
    /**
     * Retrieve the request token.
     *
     * @return request token
     */
    public Token getRequestToken();

    /**
     * Retrieve the access token
     *
     * @param requestToken request token (obtained previously; not required for OAuth 2)
     * @param verifier     verifier code
     * @return access token
     */
    public Token getAccessToken(@Nullable Token requestToken, Verifier verifier);

    /**
     * Refresh the access token to extend its expiration date.
     * <p/>
     * For the token in parameter, Facebook needs the access_token, while Live needs the refresh_token (which can be
     * found only in the
     * {@link jsimple.oauth.model.Token#getRawResponse()} returned by
     * {@link #getAccessToken(jsimple.oauth.model.Token, jsimple.oauth.model.Verifier)})
     * <p/>
     * As for the client secret, for some providers in some scenarios it's not required.  In particular, for Live Connect
     * (taken from http://msdn.microsoft.com/en-us/library/live/hh243647.aspx):
     * "Because apps must also refresh access tokens, the Live Connect app management site allows apps to be
     * marked as mobile client apps. When this marker is specified and the special redirect URL
     * (https://login.live.com/oauth20_desktop.srf) is used, the client secret is not required to refresh the access
     * token.".
     *
     * @param refreshOrAccessToken access or refresh token, depending on the OAuth provider
     * @param includeSecret        whether or not to include the client secret; Windows Live, for mobile apps,
     *                             doesn't require it
     * @return fresh access token
     */
    public Token refreshAccessToken(Token refreshOrAccessToken, boolean includeSecret);

    /**
     * Signs am OAuth request
     *
     * @param accessToken access token (obtained previously)
     * @param request     request to sign
     */
    public void signRequest(Token accessToken, OAuthRequest request);

    /**
     * Returns the OAuth version of the service.
     *
     * @return oauth version as string
     */
    public String getVersion();

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param requestToken the request token you need to authorize; not used for OAuth2
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(@Nullable Token requestToken);
}
