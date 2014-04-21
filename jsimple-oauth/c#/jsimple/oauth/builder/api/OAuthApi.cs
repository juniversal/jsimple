namespace jsimple.oauth.builder.api {

    using OAuthConfig = jsimple.oauth.model.OAuthConfig;
    using OAuthService = jsimple.oauth.oauth.OAuthService;

    /// <summary>
    /// Contains all the configuration needed to instantiate a valid <seealso cref="OAuthService"/>
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public interface OAuthApi {
        /// <summary>
        /// Creates an <seealso cref="OAuthService"/>
        /// </summary>
        /// <param name="config"> config info (apiKey, apiSecret, callback, scope) </param>
        /// <returns> fully configured <seealso cref="OAuthService"/> </returns>
        OAuthService createService(OAuthConfig config);
    }

}