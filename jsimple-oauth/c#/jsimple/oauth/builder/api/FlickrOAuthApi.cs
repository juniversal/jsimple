namespace jsimple.oauth.builder.api {

    using Token = jsimple.oauth.model.Token;

    /// <summary>
    /// OAuth API for Flickr.
    /// 
    /// @author Darren Greaves </summary>
    /// <seealso cref= <a href="http://www.flickr.com/services/api/">Flickr API</a> </seealso>
    public class FlickrOAuthApi : DefaultOAuthApi10a {

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public override string AccessTokenEndpoint {
            get {
                return "http://www.flickr.com/services/oauth/access_token";
            }
        }

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public override string getAuthorizationUrl(Token requestToken) {
            return "http://www.flickr.com/services/oauth/authorize?oauth_token=" + requestToken.TokenString;
        }

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public override string RequestTokenEndpoint {
            get {
                return "http://www.flickr.com/services/oauth/request_token";
            }
        }
    }

}