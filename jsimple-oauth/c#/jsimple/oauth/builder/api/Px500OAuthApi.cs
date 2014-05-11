namespace jsimple.oauth.builder.api {

    using Token = jsimple.oauth.model.Token;

    public class Px500OAuthApi : DefaultOAuthApi10a {
        public override string AccessTokenEndpoint {
            get {
                return "https://api.500px.com/v1/oauth/access_token";
            }
        }

        public override string RequestTokenEndpoint {
            get {
                return "https://api.500px.com/v1/oauth/request_token";
            }
        }

        public override string getAuthorizationUrl(Token requestToken) {
            return "https://api.500px.com/v1/oauth/authorize?oauth_token=" + requestToken.TokenString;
        }
    }
}