package jsimple.oauth.services;

import jsimple.io.IOUtils;
import jsimple.oauth.exceptions.OAuthSignatureException;
import jsimple.oauth.utils.OAuthEncoder;
import jsimple.oauth.utils.Sha1;
import jsimple.util.Base64;

/**
 * HMAC-SHA1 implementation of {@SignatureService}
 *
 * @author Pablo Fernandez
 */
public class HMACSha1SignatureService implements SignatureService {
    private static final String METHOD = "HMAC-SHA1";

    /**
     * {@inheritDoc}
     */
    public String getSignature(String baseString, String apiSecret, String tokenSecret) {
        try {
            assert baseString != null && !baseString.isEmpty() : "Base string cant be null or empty string";
            assert apiSecret != null && !apiSecret.isEmpty() : "Api secret cant be null or empty string";

            return doSign(baseString, OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret));
        } catch (Exception e) {
            throw new OAuthSignatureException(baseString, e);
        }
    }

    private String doSign(String toSign, String keyString) {
        //byte[] toUtf8BytesFromString(String s, int[] length) {

        byte[] signature = Sha1.hmac(IOUtils.toUtf8BytesFromString(keyString).toByteArray(),
                IOUtils.toUtf8BytesFromString(toSign).toByteArray());
        return Base64.encodeBase64String(signature);

        /*
        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(UTF8), HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);
        byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
        return new String(Base64.encodeBase64(bytes)).replace(CARRIAGE_RETURN, EMPTY_STRING);
        */
    }

    /**
     * {@inheritDoc}
     */
    public String getSignatureMethod() {
        return METHOD;
    }
}
