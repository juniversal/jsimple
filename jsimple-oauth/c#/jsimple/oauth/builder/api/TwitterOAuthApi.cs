namespace jsimple.oauth.builder.api {

    using Token = jsimple.oauth.model.Token;

    public class TwitterOAuthApi : DefaultOAuthApi10a {
        private const string REQUEST_TOKEN_RESOURCE = "api.twitter.com/oauth/request_token";
        private const string ACCESS_TOKEN_RESOURCE = "api.twitter.com/oauth/access_token";

        public override string AccessTokenEndpoint {
            get {
                return "http://" + ACCESS_TOKEN_RESOURCE;
            }
        }

        public override string RequestTokenEndpoint {
            get {
                return "http://" + REQUEST_TOKEN_RESOURCE;
            }
        }

        public override string getAuthorizationUrl(Token requestToken) {
            return "https://api.twitter.com/oauth/authorize?oauth_token=" + requestToken.TokenString;
        }

        public class SSL : TwitterOAuthApi {
            public override string AccessTokenEndpoint {
                get {
                    return "https://" + ACCESS_TOKEN_RESOURCE;
                }
            }

            public override string RequestTokenEndpoint {
                get {
                    return "https://" + REQUEST_TOKEN_RESOURCE;
                }
            }
        }

        /// <summary>
        /// Twitter 'friendlier' authorization endpoint for OAuth.
        /// <p/>
        /// Uses SSL.
        /// </summary>
        public class Authenticate : SSL {
            public override string getAuthorizationUrl(Token requestToken) {
                return "https://api.twitter.com/oauth/authenticate?oauth_token=" + requestToken.TokenString;
            }
        }

        /// <summary>
        /// Just an alias to the default (SSL) authorization endpoint.
        /// <p/>
        /// Need to include this for symmetry with 'Authenticate' only.
        /// </summary>
        public class Authorize : SSL {
        }
    }

}