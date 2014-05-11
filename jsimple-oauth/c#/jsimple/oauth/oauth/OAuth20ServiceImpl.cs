using System;

namespace jsimple.oauth.oauth {

    using DefaultOAuthApi20 = jsimple.oauth.builder.api.DefaultOAuthApi20;
    using jsimple.oauth.model;


    public class OAuth20ServiceImpl : OAuthService {
        private const string VERSION = "2.0";

        private readonly DefaultOAuthApi20 api;
        private readonly OAuthConfig config;

        /// <summary>
        /// Default constructor
        /// </summary>
        /// <param name="api">    OAuth2.0 api information </param>
        /// <param name="config"> OAuth 2.0 configuration param object </param>
        public OAuth20ServiceImpl(DefaultOAuthApi20 api, OAuthConfig config) {
            this.api = api;
            this.config = config;
        }

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public virtual Token getAccessToken(Token requestToken, Verifier verifier) {
            OAuthRequest request = new OAuthRequest(api.AccessTokenVerb, api.AccessTokenEndpoint);
            request.addQueryStringParameter(OAuthConstants.CLIENT_ID, config.ApiKey);
            request.addQueryStringParameter(OAuthConstants.CLIENT_SECRET, config.ApiSecret);
            request.addQueryStringParameter(OAuthConstants.CODE, verifier.Value);
            request.addQueryStringParameter(OAuthConstants.REDIRECT_URI, config.Callback);
            string scope = config.Scope;
            if (scope != null)
                request.addQueryStringParameter(OAuthConstants.SCOPE, scope);
            OAuthResponse response = request.send();
            return api.AccessTokenExtractor.extract(response.Body);
        }

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public virtual Token refreshAccessToken(Token refreshOrAccessToken, bool includeSecret) {
            string accessTokenEndpoint = api.AccessTokenEndpoint;
            if (accessTokenEndpoint.Contains("?grant_type="))
                // handle the ugly case where the grant_type parameter is already hardcoded in the constant url
                accessTokenEndpoint = accessTokenEndpoint.Substring(0, accessTokenEndpoint.IndexOf("?", StringComparison.Ordinal));

            OAuthRequest request = new OAuthRequest(api.AccessTokenVerb, accessTokenEndpoint);
            request.addQueryStringParameter(OAuthConstants.CLIENT_ID, config.ApiKey);
            if (includeSecret)
                request.addQueryStringParameter(OAuthConstants.CLIENT_SECRET, config.ApiSecret);
            request.addQueryStringParameter(OAuthConstants.REDIRECT_URI, config.Callback);
            request.addQueryStringParameter(OAuthConstants.GRANT_TYPE, api.RefreshTokenParameterName);
            request.addQueryStringParameter(api.RefreshTokenParameterName, refreshOrAccessToken.TokenString);
            OAuthResponse response = request.send();
            return api.AccessTokenExtractor.extract(response.Body);
        }

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public virtual Token RequestToken {
            get {
                throw new System.NotSupportedException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
            }
        }

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public virtual string Version {
            get {
                return VERSION;
            }
        }

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public virtual void signRequest(Token accessToken, OAuthRequest request) {
            request.addQueryStringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.TokenString);
        }

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public virtual string getAuthorizationUrl(Token requestToken) {
            return api.getAuthorizationUrl(config);
        }
    }

}