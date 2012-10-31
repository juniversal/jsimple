package jsimple.oauth.builder.api;

import jsimple.oauth.model.Token;

public class TwitterApi extends DefaultApi10a {
    private static final String REQUEST_TOKEN_RESOURCE = "api.twitter.com/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "api.twitter.com/oauth/access_token";

    @Override public String getAccessTokenEndpoint() {
        return "http://" + ACCESS_TOKEN_RESOURCE;
    }

    @Override public String getRequestTokenEndpoint() {
        return "http://" + REQUEST_TOKEN_RESOURCE;
    }

    @Override public String getAuthorizationUrl(Token requestToken) {
        return "https://api.twitter.com/oauth/authorize?oauth_token=" + requestToken.getTokenString();
    }

    public static class SSL extends TwitterApi {
        @Override public String getAccessTokenEndpoint() {
            return "https://" + ACCESS_TOKEN_RESOURCE;
        }

        @Override public String getRequestTokenEndpoint() {
            return "https://" + REQUEST_TOKEN_RESOURCE;
        }
    }

    /**
     * Twitter 'friendlier' authorization endpoint for OAuth.
     * <p/>
     * Uses SSL.
     */
    public static class Authenticate extends SSL {
        @Override public String getAuthorizationUrl(Token requestToken) {
            return "https://api.twitter.com/oauth/authenticate?oauth_token=" + requestToken.getTokenString();
        }
    }

    /**
     * Just an alias to the default (SSL) authorization endpoint.
     * <p/>
     * Need to include this for symmetry with 'Authenticate' only.
     */
    public static class Authorize extends SSL {
    }
}
