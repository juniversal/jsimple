namespace jsimple.oauth.services {

    /// <summary>
    /// Signs a base string, returning the OAuth signature
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public interface SignatureService {
        /// <summary>
        /// Returns the signature
        /// </summary>
        /// <param name="baseString">  url-encoded string to sign </param>
        /// <param name="apiSecret">   api secret for your app </param>
        /// <param name="tokenSecret"> token secret (empty string for the request token step) </param>
        /// <returns> signature </returns>
        string getSignature(string baseString, string apiSecret, string tokenSecret);

        /// <summary>
        /// Returns the signature method/algorithm
        /// 
        /// @return
        /// </summary>
        string SignatureMethod {get;}
    }

}