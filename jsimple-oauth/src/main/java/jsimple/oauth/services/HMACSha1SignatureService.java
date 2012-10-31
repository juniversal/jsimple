package jsimple.oauth.services;

import jsimple.base64.Base64;
import jsimple.io.IOUtils;
import jsimple.oauth.exceptions.OAuthSignatureException;
import jsimple.oauth.utils.OAuthEncoder;
import jsimple.oauth.utils.Sha1;

/**
 * HMAC-SHA1 implementation of {@SignatureService}
 *
 * @author Pablo Fernandez
 */
public class HMACSha1SignatureService implements SignatureService {
    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String UTF8 = "UTF-8";
    private static final String HMAC_SHA1 = "HmacSHA1";
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

    private String doSign(String toSign, String keyString) throws Exception {
        //byte[] toUtf8BytesFromString(String s, int[] length) {

        byte[] signature = Sha1.mac(IOUtils.toUtf8BytesFromString(keyString), IOUtils.toUtf8BytesFromString(toSign));
        return new String(Base64.encodeBase64(signature)).replace(CARRIAGE_RETURN, EMPTY_STRING);

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
