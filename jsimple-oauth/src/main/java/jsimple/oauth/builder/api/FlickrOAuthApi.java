package jsimple.oauth.builder.api;

import jsimple.oauth.model.Token;

/**
 * OAuth API for Flickr.
 *
 * @author Darren Greaves
 * @see <a href="http://www.flickr.com/services/api/">Flickr API</a>
 */
public class FlickrOAuthApi extends DefaultOAuthApi10a {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAccessTokenEndpoint() {
        return "http://www.flickr.com/services/oauth/access_token";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return "http://www.flickr.com/services/oauth/authorize?oauth_token=" + requestToken.getTokenString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestTokenEndpoint() {
        return "http://www.flickr.com/services/oauth/request_token";
    }
}