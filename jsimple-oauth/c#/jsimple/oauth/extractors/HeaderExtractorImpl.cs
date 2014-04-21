using System.Collections.Generic;
using System.Text;

namespace jsimple.oauth.extractors {

    using OAuthParametersMissingException = jsimple.oauth.exceptions.OAuthParametersMissingException;
    using OAuthRequest = jsimple.oauth.model.OAuthRequest;
    using OAuthEncoder = jsimple.oauth.utils.OAuthEncoder;

    /// <summary>
    /// Default implementation of <seealso cref="HeaderExtractor"/>. Conforms to OAuth 1.0a
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public class HeaderExtractorImpl : HeaderExtractor {
        private const string PARAM_SEPARATOR = ", ";
        private const string PREAMBLE = "OAuth ";

        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public virtual string extract(OAuthRequest request) {
            checkPreconditions(request);
            IDictionary<string, string> parameters = request.OauthParameters;
            StringBuilder header = new StringBuilder(parameters.Count * 20);
            header.Append(PREAMBLE);
            foreach (string key in parameters.Keys) {
                if (header.Length > PREAMBLE.Length)
                    header.Append(PARAM_SEPARATOR);
                header.Append(key + "=\"" + OAuthEncoder.encode(parameters.GetValueOrNull(key)) + "\"");
            }
            return header.ToString();
        }

        private void checkPreconditions(OAuthRequest request) {
            if (request.OauthParameters == null || request.OauthParameters.Count <= 0)
                throw new OAuthParametersMissingException(request);
        }
    }

}