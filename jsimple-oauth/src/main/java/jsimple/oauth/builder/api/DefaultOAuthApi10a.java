package jsimple.oauth.builder.api;

import jsimple.oauth.extractors.*;
import jsimple.oauth.model.OAuthConfig;
import jsimple.oauth.model.Token;
import jsimple.oauth.model.Verb;
import jsimple.oauth.oauth.OAuth10aServiceImpl;
import jsimple.oauth.oauth.OAuthService;
import jsimple.oauth.services.HMACSha1SignatureService;
import jsimple.oauth.services.SignatureService;
import jsimple.oauth.services.TimestampService;
import jsimple.oauth.services.TimestampServiceImpl;

/**
 * Default implementation of the OAuth protocol, version 1.0a
 * <p/>
 * This class is meant to be extended by concrete implementations of the API, providing the endpoints and
 * endpoint-http-verbs.
 * <p/>
 * If your Api adheres to the 1.0a protocol correctly, you just need to extend this class and define the getters for
 * your endpoints.
 * <p/>
 * If your Api does something a bit different, you can override the different extractors or services, in order to
 * fine-tune the process. Please read the javadocs of the interfaces to get an idea of what to do.
 *
 * @author Pablo Fernandez
 */
public abstract class DefaultOAuthApi10a implements OAuthApi {
    /**
     * Returns the access token extractor.
     *
     * @return access token extractor
     */
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new TokenExtractorImpl();
    }

    /**
     * Returns the base string extractor.
     *
     * @return base string extractor
     */
    public BaseStringExtractor getBaseStringExtractor() {
        return new BaseStringExtractorImpl();
    }

    /**
     * Returns the header extractor.
     *
     * @return header extractor
     */
    public HeaderExtractor getHeaderExtractor() {
        return new HeaderExtractorImpl();
    }

    /**
     * Returns the request token extractor.
     *
     * @return request token extractor
     */
    public RequestTokenExtractor getRequestTokenExtractor() {
        return new TokenExtractorImpl();
    }

    /**
     * Returns the signature service.
     *
     * @return signature service
     */
    public SignatureService getSignatureService() {
        return new HMACSha1SignatureService();
    }

    /**
     * Returns the timestamp service.
     *
     * @return timestamp service
     */
    public TimestampService getTimestampService() {
        return new TimestampServiceImpl();
    }

    /**
     * Returns the method for the access token endpoint (defaults to POST)
     *
     * @return access token endpoint verb
     */
    public String getAccessTokenVerb() {
        return Verb.POST;
    }

    /**
     * Returns the verb for the request token endpoint (defaults to POST)
     *
     * @return request token endpoint verb
     */
    public String getRequestTokenVerb() {
        return Verb.POST;
    }

    /**
     * Returns the URL that receives the request token requests.
     *
     * @return request token URL
     */
    public abstract String getRequestTokenEndpoint();

    /**
     * Returns the URL that receives the access token requests.
     *
     * @return access token URL
     */
    public abstract String getAccessTokenEndpoint();

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    public abstract String getAuthorizationUrl(Token requestToken);

    /**
     * Returns the {@link OAuthService} for this Api
     *
     * @param config config info (apiKey, apiSecret, callback, scope)
     */
    public OAuthService createService(OAuthConfig config) {
        return new OAuth10aServiceImpl(this, config);
    }
}
