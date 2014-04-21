namespace jsimple.oauth.oauth {

    using OAuthRequest = jsimple.oauth.model.OAuthRequest;
    using Token = jsimple.oauth.model.Token;
    using Verifier = jsimple.oauth.model.Verifier;

    /// <summary>
    /// The main Scribe object.
    /// <p/>
    /// A facade responsible for the retrieval of request and access tokens and for the signing of HTTP requests.
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public interface OAuthService {
        /// <summary>
        /// Retrieve the request token.
        /// </summary>
        /// <returns> request token </returns>
        Token RequestToken {get;}

        /// <summary>
        /// Retrieve the access token
        /// </summary>
        /// <param name="requestToken"> request token (obtained previously; not required for OAuth 2) </param>
        /// <param name="verifier">     verifier code </param>
        /// <returns> access token </returns>
        Token getAccessToken(Token requestToken, Verifier verifier);

        /// <summary>
        /// Refresh the access token to extend its expiration date.
        /// <p/>
        /// For the token in parameter, Facebook needs the access_token, while Live needs the refresh_token (which can be
        /// found only in the <seealso cref="jsimple.oauth.model.Token#getRawResponse()"/> returned by {@link
        /// #getAccessToken(jsimple.oauth.model.Token, jsimple.oauth.model.Verifier)})
        /// <p/>
        /// As for the client secret, for some providers in some scenarios it's not required.  In particular, for Live
        /// Connect (taken from http://msdn.microsoft.com/en-us/library/live/hh243647.aspx): "Because apps must also refresh
        /// access tokens, the Live Connect app management site allows apps to be marked as mobile client apps. When this
        /// marker is specified and the special redirect URL (https://login.live.com/oauth20_desktop.srf) is used, the client
        /// secret is not required to refresh the access token.".
        /// </summary>
        /// <param name="refreshOrAccessToken"> access or refresh token, depending on the OAuth provider </param>
        /// <param name="includeSecret">        whether or not to include the client secret; Windows Live, for mobile apps, doesn't
        ///                             require it </param>
        /// <returns> fresh access token </returns>
        Token refreshAccessToken(Token refreshOrAccessToken, bool includeSecret);

        /// <summary>
        /// Signs am OAuth request
        /// </summary>
        /// <param name="accessToken"> access token (obtained previously) </param>
        /// <param name="request">     request to sign </param>
        void signRequest(Token accessToken, OAuthRequest request);

        /// <summary>
        /// Returns the OAuth version of the service.
        /// </summary>
        /// <returns> oauth version as string </returns>
        string Version {get;}

        /// <summary>
        /// Returns the URL where you should redirect your users to authenticate your application.
        /// </summary>
        /// <param name="requestToken"> the request token you need to authorize; not used for OAuth2 </param>
        /// <returns> the URL where you should redirect your users </returns>
        string getAuthorizationUrl(Token requestToken);
    }

}