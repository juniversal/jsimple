package jsimple.oauth.services;

import jsimple.oauth.exceptions.OAuthSignatureException;
import jsimple.oauth.utils.OAuthEncoder;

/**
 * plaintext implementation of {@SignatureService}
 *
 * @author Pablo Fernandez
 */
public class PlaintextSignatureService implements SignatureService {
    private static final String METHOD = "plaintext";

    /**
     * {@inheritDoc}
     */
    public String getSignature(String baseString, String apiSecret, String tokenSecret) {
        try {
            assert !apiSecret.trim().isEmpty() : "Api secret cant be null or empty string";
            return OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret);
        } catch (Exception e) {
            throw new OAuthSignatureException(baseString, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getSignatureMethod() {
        return METHOD;
    }
}

