namespace jsimple.oauth.extractors {

    using Token = jsimple.oauth.model.Token;

    /// <summary>
    /// Simple command object that extracts a <seealso cref="Token"/> from a String
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public interface AccessTokenExtractor {
        /// <summary>
        /// Extracts the access token from the contents of an Http Response
        /// </summary>
        /// <param name="response"> the contents of the response </param>
        /// <returns> OAuth access token </returns>
        Token extract(string response);
    }

}