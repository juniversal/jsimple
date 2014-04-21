namespace jsimple.oauth.builder.api {

    using jsimple.oauth.extractors;
    using OAuthConfig = jsimple.oauth.model.OAuthConfig;
    using Token = jsimple.oauth.model.Token;
    using Verb = jsimple.oauth.model.Verb;
    using OAuth10aServiceImpl = jsimple.oauth.oauth.OAuth10aServiceImpl;
    using OAuthService = jsimple.oauth.oauth.OAuthService;
    using HMACSha1SignatureService = jsimple.oauth.services.HMACSha1SignatureService;
    using SignatureService = jsimple.oauth.services.SignatureService;
    using TimestampService = jsimple.oauth.services.TimestampService;
    using TimestampServiceImpl = jsimple.oauth.services.TimestampServiceImpl;

    /// <summary>
    /// Default implementation of the OAuth protocol, version 1.0a
    /// <p/>
    /// This class is meant to be extended by concrete implementations of the API, providing the endpoints and
    /// endpoint-http-verbs.
    /// <p/>
    /// If your Api adheres to the 1.0a protocol correctly, you just need to extend this class and define the getters for
    /// your endpoints.
    /// <p/>
    /// If your Api does something a bit different, you can override the different extractors or services, in order to
    /// fine-tune the process. Please read the javadocs of the interfaces to get an idea of what to do.
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public abstract class DefaultOAuthApi10a : OAuthApi {
        /// <summary>
        /// Returns the access token extractor.
        /// </summary>
        /// <returns> access token extractor </returns>
        public virtual AccessTokenExtractor AccessTokenExtractor {
            get {
                return new TokenExtractorImpl();
            }
        }

        /// <summary>
        /// Returns the base string extractor.
        /// </summary>
        /// <returns> base string extractor </returns>
        public virtual BaseStringExtractor BaseStringExtractor {
            get {
                return new BaseStringExtractorImpl();
            }
        }

        /// <summary>
        /// Returns the header extractor.
        /// </summary>
        /// <returns> header extractor </returns>
        public virtual HeaderExtractor HeaderExtractor {
            get {
                return new HeaderExtractorImpl();
            }
        }

        /// <summary>
        /// Returns the request token extractor.
        /// </summary>
        /// <returns> request token extractor </returns>
        public virtual RequestTokenExtractor RequestTokenExtractor {
            get {
                return new TokenExtractorImpl();
            }
        }

        /// <summary>
        /// Returns the signature service.
        /// </summary>
        /// <returns> signature service </returns>
        public virtual SignatureService SignatureService {
            get {
                return new HMACSha1SignatureService();
            }
        }

        /// <summary>
        /// Returns the timestamp service.
        /// </summary>
        /// <returns> timestamp service </returns>
        public virtual TimestampService TimestampService {
            get {
                return new TimestampServiceImpl();
            }
        }

        /// <summary>
        /// Returns the method for the access token endpoint (defaults to POST)
        /// </summary>
        /// <returns> access token endpoint verb </returns>
        public virtual string AccessTokenVerb {
            get {
                return Verb.POST;
            }
        }

        /// <summary>
        /// Returns the verb for the request token endpoint (defaults to POST)
        /// </summary>
        /// <returns> request token endpoint verb </returns>
        public virtual string RequestTokenVerb {
            get {
                return Verb.POST;
            }
        }

        /// <summary>
        /// Returns the URL that receives the request token requests.
        /// </summary>
        /// <returns> request token URL </returns>
        public abstract string RequestTokenEndpoint {get;}

        /// <summary>
        /// Returns the URL that receives the access token requests.
        /// </summary>
        /// <returns> access token URL </returns>
        public abstract string AccessTokenEndpoint {get;}

        /// <summary>
        /// Returns the URL where you should redirect your users to authenticate your application.
        /// </summary>
        /// <param name="requestToken"> the request token you need to authorize </param>
        /// <returns> the URL where you should redirect your users </returns>
        public abstract string getAuthorizationUrl(Token requestToken);

        /// <summary>
        /// Returns the <seealso cref="OAuthService"/> for this Api
        /// </summary>
        /// <param name="config"> config info (apiKey, apiSecret, callback, scope) </param>
        public virtual OAuthService createService(OAuthConfig config) {
            return new OAuth10aServiceImpl(this, config);
        }
    }

}