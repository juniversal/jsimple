using System.Diagnostics;

namespace jsimple.oauth.builder.api {

    using OAuthConfig = jsimple.oauth.model.OAuthConfig;
    using OAuthEncoder = jsimple.oauth.utils.OAuthEncoder;

    public class FacebookOAuthApi : DefaultOAuthApi20 {
        private const string AUTHORIZE_URL = "https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s";
        private static readonly string SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

        public override string AccessTokenEndpoint {
            get {
                return "https://graph.facebook.com/oauth/access_token";
            }
        }

        public override string getAuthorizationUrl(OAuthConfig config) {
            // Old code:
            // Preconditions.checkValidUrl(config.getCallback(),
            //      "Must provide a valid url as callback. Facebook does not support OOB");
            Debug.Assert(config.Callback != null && config.Callback.Length > 0, "Must provide a valid url as callback. Facebook does not support OOB");

            string url = "https://www.facebook.com/dialog/oauth?client_id=" + config.ApiKey + "&redirect_uri=" + OAuthEncoder.encode(config.Callback);

            // Append scope if present
            string scope = config.Scope;
            if (scope != null)
                url += "&scope=" + OAuthEncoder.encode(scope);

            return url;
        }

        public override string RefreshTokenParameterName {
            get {
                return "fb_exchange_token";
            }
        }
    }

}