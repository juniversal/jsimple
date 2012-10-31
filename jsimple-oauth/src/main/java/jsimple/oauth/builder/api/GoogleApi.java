package jsimple.oauth.builder.api;

import jsimple.oauth.model.Token;
import jsimple.oauth.model.Verb;

public class GoogleApi extends DefaultApi10a {
    @Override public String getAccessTokenEndpoint() {
        return "https://www.google.com/accounts/OAuthGetAccessToken";
    }

    @Override public String getRequestTokenEndpoint() {
        return "https://www.google.com/accounts/OAuthGetRequestToken";
    }

    @Override public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override public Verb getRequestTokenVerb() {
        return Verb.GET;
    }

    @Override public String getAuthorizationUrl(Token requestToken) {
        return "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=" + requestToken.getTokenString();
    }
}
