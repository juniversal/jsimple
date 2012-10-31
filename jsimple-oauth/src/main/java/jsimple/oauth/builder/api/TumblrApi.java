package jsimple.oauth.builder.api;

import jsimple.oauth.model.Token;

public class TumblrApi extends DefaultApi10a {
    private static final String REQUEST_TOKEN_RESOURCE = "http://www.tumblr.com/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "http://www.tumblr.com/oauth/access_token";

    @Override public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_RESOURCE;
    }

    @Override public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_RESOURCE;
    }

    @Override public String getAuthorizationUrl(Token requestToken) {
        return "https://www.tumblr.com/oauth/authorize?oauth_token=" + requestToken.getTokenString();
    }
}
