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
