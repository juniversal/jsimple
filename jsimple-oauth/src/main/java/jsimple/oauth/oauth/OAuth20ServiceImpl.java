package jsimple.oauth.oauth;

import jsimple.oauth.builder.api.DefaultOAuthApi20;
import jsimple.oauth.model.*;
import org.jetbrains.annotations.Nullable;

public class OAuth20ServiceImpl implements OAuthService {
    private static final String VERSION = "2.0";

    private final DefaultOAuthApi20 api;
    private final OAuthConfig config;

    /**
     * Default constructor
     *
     * @param api    OAuth2.0 api information
     * @param config OAuth 2.0 configuration param object
     */
    public OAuth20ServiceImpl(DefaultOAuthApi20 api, OAuthConfig config) {
        this.api = api;
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    public Token getAccessToken(@Nullable Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addQueryStringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addQueryStringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addQueryStringParameter(OAuthConstants.CODE, verifier.getValue());
        request.addQueryStringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        String scope = config.getScope();
        if (scope != null)
            request.addQueryStringParameter(OAuthConstants.SCOPE, scope);
        OAuthResponse response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    public Token getRequestToken() {
        throw new UnsupportedOperationException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
    }

    /**
     * {@inheritDoc}
     */
    public String getVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    public void signRequest(Token accessToken, OAuthRequest request) {
        request.addQueryStringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getTokenString());
    }

    /**
     * {@inheritDoc}
     */
    public String getAuthorizationUrl(@Nullable Token requestToken) {
        return api.getAuthorizationUrl(config);
    }
}
