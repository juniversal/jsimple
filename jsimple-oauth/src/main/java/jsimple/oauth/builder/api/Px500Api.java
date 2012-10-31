package jsimple.oauth.builder.api;

import jsimple.oauth.model.Token;

public class Px500Api extends DefaultApi10a {
    @Override public String getAccessTokenEndpoint() {
        return "https://api.500px.com/v1/oauth/access_token";
    }

    @Override public String getRequestTokenEndpoint() {
        return "https://api.500px.com/v1/oauth/request_token";
    }

    @Override public String getAuthorizationUrl(Token requestToken) {
        return "https://api.500px.com/v1/oauth/authorize?oauth_token=" + requestToken.getTokenString();
    }
}