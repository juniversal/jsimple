namespace jsimple.oauth.extractors {

    using OAuthParametersMissingException = jsimple.oauth.exceptions.OAuthParametersMissingException;
    using OAuthRequest = jsimple.oauth.model.OAuthRequest;
    using ParameterList = jsimple.oauth.model.ParameterList;
    using OAuthEncoder = jsimple.oauth.utils.OAuthEncoder;

    /// <summary>
    /// Default implementation of <seealso cref="BaseStringExtractor"/>. Conforms to OAuth 1.0a
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public class BaseStringExtractorImpl : BaseStringExtractor {
        /// <summary>
        /// {@inheritDoc}
        /// </summary>
        public virtual string extract(OAuthRequest request) {
            checkPreconditions(request);
            string verb = OAuthEncoder.encode(request.Verb);
            string url = OAuthEncoder.encode(request.SanitizedUrl);
            string @params = getSortedAndEncodedParams(request);
            return verb + "&" + url + "&" + @params;
        }

        private string getSortedAndEncodedParams(OAuthRequest request) {
            ParameterList @params = new ParameterList();
            @params.addAll(request.QueryStringParams);
            @params.addAll(request.BodyParams);
            @params.addAll(new ParameterList(request.OauthParameters));
            return @params.sort().asOauthBaseString();
        }

        private void checkPreconditions(OAuthRequest request) {
            if (request.OauthParameters == null || request.OauthParameters.Count <= 0)
                throw new OAuthParametersMissingException(request);
        }
    }

}