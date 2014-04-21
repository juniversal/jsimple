namespace jsimple.oauth.extractors {

    using OAuthRequest = jsimple.oauth.model.OAuthRequest;

    /// <summary>
    /// Simple command object that extracts a base string from a <seealso cref="OAuthRequest"/>
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public interface BaseStringExtractor {
        /// <summary>
        /// Extracts an url-encoded base string from the <seealso cref="OAuthRequest"/>.
        /// <p/>
        /// See <a href="http://oauth.net/core/1.0/#anchor14">the oauth spec</a> for more info on this.
        /// </summary>
        /// <param name="request"> the OAuthRequest </param>
        /// <returns> the url-encoded base string </returns>
        string extract(OAuthRequest request);
    }

}