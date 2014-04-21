namespace jsimple.oauth.model {



    /// <summary>
    /// Parameter object that groups OAuth config values
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public class OAuthConfig {
        private readonly string apiKey;
        private readonly string apiSecret;
        private readonly string callback;
        private readonly SignatureType signatureType;
        private readonly string scope;
        private readonly OAuthLogger debugLogger;

        /*
        public OAuthConfig(String key, String secret)
        {
          this(key, secret, null, null, null, null);
        }
        (*/

        public OAuthConfig(string key, string secret, string callback, SignatureType type, string scope, OAuthLogger debugLogger) {
            this.apiKey = key;
            this.apiSecret = secret;
            this.callback = callback;
            this.signatureType = type;
            this.scope = scope;
            this.debugLogger = debugLogger;
        }

        public virtual string ApiKey {
            get {
                return apiKey;
            }
        }

        public virtual string ApiSecret {
            get {
                return apiSecret;
            }
        }

        public virtual string Callback {
            get {
                return callback;
            }
        }

        public virtual SignatureType SignatureType {
            get {
                return signatureType;
            }
        }

        public virtual string Scope {
            get {
                return scope;
            }
        }

        public virtual bool hasScope() {
            return scope != null;
        }

        public virtual void log(string message) {
            if (debugLogger != null)
                debugLogger.log(message);
        }
    }

}