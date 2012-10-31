package jsimple.oauth.model;

import org.jetbrains.annotations.Nullable;

/**
 * Parameter object that groups OAuth config values
 *
 * @author Pablo Fernandez
 */
public class OAuthConfig {
    private final String apiKey;
    private final String apiSecret;
    private final String callback;
    private final SignatureType signatureType;
    private final @Nullable String scope;
    private final @Nullable OAuthLogger debugLogger;

    /*
    public OAuthConfig(String key, String secret)
    {
      this(key, secret, null, null, null, null);
    }
    (*/

    public OAuthConfig(String key, String secret, String callback, SignatureType type, @Nullable String scope,
                       @Nullable OAuthLogger debugLogger) {
        this.apiKey = key;
        this.apiSecret = secret;
        this.callback = callback;
        this.signatureType = type;
        this.scope = scope;
        this.debugLogger = debugLogger;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getCallback() {
        return callback;
    }

    public SignatureType getSignatureType() {
        return signatureType;
    }

    public @Nullable String getScope() {
        return scope;
    }

    public boolean hasScope() {
        return scope != null;
    }

    public void log(String message) {
        if (debugLogger != null)
            debugLogger.log(message);
    }
}
