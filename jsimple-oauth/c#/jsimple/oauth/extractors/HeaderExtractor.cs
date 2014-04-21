namespace jsimple.oauth.extractors {

    using OAuthRequest = jsimple.oauth.model.OAuthRequest;

    /// <summary>
    /// Simple command object that generates an OAuth Authorization header to include in the request.
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public interface HeaderExtractor {
        /// <summary>
        /// Generates an OAuth 'Authorization' Http header to include in requests as the signature.
        /// </summary>
        /// <param name="request"> the OAuthRequest to inspect and generate the header </param>
        /// <returns> the Http header value </returns>
        string extract(OAuthRequest request);
    }

}