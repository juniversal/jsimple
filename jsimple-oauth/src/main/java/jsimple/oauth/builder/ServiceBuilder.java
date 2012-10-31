package jsimple.oauth.builder;

import jsimple.oauth.builder.api.Api;
import jsimple.oauth.exceptions.OAuthException;
import jsimple.oauth.model.OAuthConfig;
import jsimple.oauth.model.OAuthConstants;
import jsimple.oauth.model.OAuthLogger;
import jsimple.oauth.model.SignatureType;
import jsimple.oauth.oauth.OAuthService;
import org.jetbrains.annotations.Nullable;


/**
 * Implementation of the Builder pattern, with a fluent interface that creates a {@link OAuthService}
 *
 * @author Pablo Fernandez
 */
public class ServiceBuilder {
    private @Nullable String apiKey;
    private @Nullable String apiSecret;
    private String callback;
    private @Nullable Api api;
    private @Nullable String scope;
    private SignatureType signatureType;
    private @Nullable OAuthLogger debugLogger;

    /**
     * Default constructor
     */
    public ServiceBuilder() {
        this.callback = OAuthConstants.OUT_OF_BAND;
        this.signatureType = SignatureType.Header;
        this.debugLogger = null;
    }

    /**
     * Configures the {@link Api}
     *
     * @param apiClass the class of one of the existent {@link Api}s on org.scribe.api package
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder provider(Class<? extends Api> apiClass) {
        this.api = createApi(apiClass);
        return this;
    }

    private Api createApi(Class<? extends Api> apiClass) {
        assert apiClass != null : "Api class cannot be null";

        Api api;
        try {
            api = apiClass.newInstance();
        } catch (Exception e) {
            throw new OAuthException("Error while creating the Api object", e);
        }
        return api;
    }

    /**
     * Configures the {@link Api}
     * <p/>
     * Overloaded version. Let's you use an instance instead of a class.
     *
     * @param api instance of {@link Api}s
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder provider(Api api) {
        assert api != null : "Api cannot be null";
        this.api = api;
        return this;
    }

    /**
     * Adds an OAuth callback url
     *
     * @param callback callback url. Must be a valid url or 'oob' for out of band OAuth
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder callback(String callback) {
        assert callback != null : "Callback can't be null";
        this.callback = callback;
        return this;
    }

    /**
     * Configures the api key
     *
     * @param apiKey The api key for your application
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder apiKey(String apiKey) {
        assert apiKey != null && !apiKey.isEmpty() : "Invalid Api key";
        this.apiKey = apiKey;
        return this;
    }

    /**
     * Configures the api secret
     *
     * @param apiSecret The api secret for your application
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder apiSecret(String apiSecret) {
        assert apiSecret != null && !apiSecret.isEmpty() : "Invalid Api secret";
        this.apiSecret = apiSecret;
        return this;
    }

    /**
     * Configures the OAuth scope. This is only necessary in some APIs (like Google's).
     *
     * @param scope The OAuth scope
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder scope(String scope) {
        assert scope != null && !scope.isEmpty() : "Invalid OAuth scope";
        this.scope = scope;
        return this;
    }

    /**
     * Configures the signature type, choose between header, querystring, etc. Defaults to Header
     *
     * @param type signature type
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ServiceBuilder signatureType(SignatureType type) {
        assert type != null : "Signature type can't be null";
        this.signatureType = type;
        return this;
    }

    public ServiceBuilder debugLogger(OAuthLogger logger) {
        assert logger != null : "debug stream can't be null";
        this.debugLogger = logger;
        return this;
    }

    /*
    public ServiceBuilder debug() {
        this.debugLogger(System.out);
        return this;
    }
    */

    /**
     * Returns the fully configured {@link OAuthService}
     *
     * @return fully configured {@link OAuthService}
     */
    public OAuthService build() {
        assert api != null : "nullness";          // You must specify a valid api through the provider() method"
        assert apiKey != null : "nullness";       // You must provide an api key
        assert apiSecret != null : "nullness";    // You must provide an api secret
        return api.createService(new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope, debugLogger));
    }
}
