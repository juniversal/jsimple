/*
 * Copyright (c) 2012-2014, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 * This code was adapted from the Scribe Java OAuth library,
 * https://github.com/fernandezpablo85/scribe-java, with modest changes made
 * for JUniversal.  Scribe itself is distributed under the MIT license.
 */

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
