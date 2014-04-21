using System;
using System.Diagnostics;

namespace jsimple.oauth.services {

    using OAuthSignatureException = jsimple.oauth.exceptions.OAuthSignatureException;
    using OAuthEncoder = jsimple.oauth.utils.OAuthEncoder;

    /// <summary>
    /// plaintext implementation of {@SignatureService}
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public class PlaintextSignatureService : SignatureService {
        private const string METHOD = "plaintext";

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public virtual string getSignature(string baseString, string apiSecret, string tokenSecret) {
            try {
                Debug.Assert(apiSecret.Trim().Length > 0, "Api secret cant be null or empty string");
                return OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret);
            }
            catch (Exception e) {
                throw new OAuthSignatureException(baseString, e);
            }
        }

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public virtual string SignatureMethod {
            get {
                return METHOD;
            }
        }
    }


}