using System.Diagnostics;

namespace jsimple.oauth.builder {

    using OAuthApi = jsimple.oauth.builder.api.OAuthApi;
    using OAuthConfig = jsimple.oauth.model.OAuthConfig;
    using OAuthConstants = jsimple.oauth.model.OAuthConstants;
    using OAuthLogger = jsimple.oauth.model.OAuthLogger;
    using SignatureType = jsimple.oauth.model.SignatureType;
    using OAuthService = jsimple.oauth.oauth.OAuthService;



    /// <summary>
    /// Implementation of the Builder pattern, with a fluent interface that creates a <seealso cref="OAuthService"/>
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public class ServiceBuilder {
        private string apiKey_Renamed;
        private string apiSecret_Renamed;
        private string callback_Renamed;
        private OAuthApi api;
        private string scope_Renamed;
        private SignatureType signatureType_Renamed;
        private OAuthLogger debugLogger_Renamed;

        /// <summary>
        /// Default constructor
        /// </summary>
        public ServiceBuilder() {
            this.callback_Renamed = OAuthConstants.OUT_OF_BAND;
            this.signatureType_Renamed = SignatureType.Header;
            this.debugLogger_Renamed = null;
        }

        /// <summary>
        /// Configures the <seealso cref="jsimple.oauth.builder.api.OAuthApi"/>
        /// <p/>
        /// Overloaded version. Let's you use an instance instead of a class.
        /// </summary>
        /// <param name="api"> instance of <seealso cref="jsimple.oauth.builder.api.OAuthApi"/>s </param>
        /// <returns> the <seealso cref="ServiceBuilder"/> instance for method chaining </returns>
        public virtual ServiceBuilder provider(OAuthApi api) {
            Debug.Assert(api != null, "Api cannot be null");
            this.api = api;
            return this;
        }

        /// <summary>
        /// Adds an OAuth callback url
        /// </summary>
        /// <param name="callback"> callback url. Must be a valid url or 'oob' for out of band OAuth </param>
        /// <returns> the <seealso cref="ServiceBuilder"/> instance for method chaining </returns>
        public virtual ServiceBuilder callback(string callback) {
            Debug.Assert(callback != null, "Callback can't be null");
            this.callback_Renamed = callback;
            return this;
        }

        /// <summary>
        /// Configures the api key
        /// </summary>
        /// <param name="apiKey"> The api key for your application </param>
        /// <returns> the <seealso cref="ServiceBuilder"/> instance for method chaining </returns>
        public virtual ServiceBuilder apiKey(string apiKey) {
            Debug.Assert(apiKey.Length > 0, "Invalid Api key");
            this.apiKey_Renamed = apiKey;
            return this;
        }

        /// <summary>
        /// Configures the api secret
        /// </summary>
        /// <param name="apiSecret"> The api secret for your application </param>
        /// <returns> the <seealso cref="ServiceBuilder"/> instance for method chaining </returns>
        public virtual ServiceBuilder apiSecret(string apiSecret) {
            Debug.Assert(apiSecret.Length > 0, "Invalid Api secret");
            this.apiSecret_Renamed = apiSecret;
            return this;
        }

        /// <summary>
        /// Configures the OAuth scope. This is only necessary in some APIs (like Google's).
        /// </summary>
        /// <param name="scope"> The OAuth scope </param>
        /// <returns> the <seealso cref="ServiceBuilder"/> instance for method chaining </returns>
        public virtual ServiceBuilder scope(string scope) {
            Debug.Assert(scope.Length > 0, "Invalid OAuth scope");
            this.scope_Renamed = scope;
            return this;
        }

        /// <summary>
        /// Configures the signature type, choose between header, querystring, etc. Defaults to Header
        /// </summary>
        /// <param name="type"> signature type </param>
        /// <returns> the <seealso cref="ServiceBuilder"/> instance for method chaining </returns>
        public virtual ServiceBuilder signatureType(SignatureType type) {
            this.signatureType_Renamed = type;
            return this;
        }

        public virtual ServiceBuilder debugLogger(OAuthLogger logger) {
            Debug.Assert(logger != null, "debug stream can't be null");
            this.debugLogger_Renamed = logger;
            return this;
        }

        /*
        public ServiceBuilder debug() {
            this.debugLogger(System.out);
            return this;
        }
        */

        /// <summary>
        /// Returns the fully configured <seealso cref="OAuthService"/>
        /// </summary>
        /// <returns> fully configured <seealso cref="OAuthService"/> </returns>
        public virtual OAuthService build() {
            Debug.Assert(api != null, "nullness"); // You must specify a valid api through the provider() method"
            Debug.Assert(apiKey_Renamed != null, "nullness"); // You must provide an api key
            Debug.Assert(apiSecret_Renamed != null, "nullness"); // You must provide an api secret
            return api.createService(new OAuthConfig(apiKey_Renamed, apiSecret_Renamed, callback_Renamed, signatureType_Renamed, scope_Renamed, debugLogger_Renamed));
        }
    }

}