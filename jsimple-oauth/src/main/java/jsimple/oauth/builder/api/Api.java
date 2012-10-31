package jsimple.oauth.builder.api;

import jsimple.oauth.model.OAuthConfig;
import jsimple.oauth.oauth.OAuthService;

/**
 * Contains all the configuration needed to instantiate a valid {@link OAuthService}
 *
 * @author Pablo Fernandez
 */
public interface Api {
    /**
     * Creates an {@link OAuthService}
     *
     * @param config config info (apiKey, apiSecret, callback, scope)
     * @return fully configured {@link OAuthService}
     */
    OAuthService createService(OAuthConfig config);
}
