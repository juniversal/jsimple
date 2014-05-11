namespace jsimple.oauth.builder.api {

    using Token = jsimple.oauth.model.Token;

    public class TumblrOAuthApi : DefaultOAuthApi10a {
        private const string REQUEST_TOKEN_RESOURCE = "http://www.tumblr.com/oauth/request_token";
        private const string ACCESS_TOKEN_RESOURCE = "http://www.tumblr.com/oauth/access_token";

        public override string AccessTokenEndpoint {
            get {
                return ACCESS_TOKEN_RESOURCE;
            }
        }

        public override string RequestTokenEndpoint {
            get {
                return REQUEST_TOKEN_RESOURCE;
            }
        }

        public override string getAuthorizationUrl(Token requestToken) {
            return "https://www.tumblr.com/oauth/authorize?oauth_token=" + requestToken.TokenString;
        }
    }

}