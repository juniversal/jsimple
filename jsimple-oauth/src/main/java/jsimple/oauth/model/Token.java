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
 * This code was adapted from the Scribe Java OAuth library
 * (https://github.com/fernandezpablo85/scribe-java), with modest changes made
 * for JUniversal.  Scribe itself is distributed under the MIT license.
 */

package jsimple.oauth.model;

import org.jetbrains.annotations.Nullable;

/**
 * Represents an OAuth token (either request or access token) and its secret
 *
 * @author Pablo Fernandez
 */
public class Token /* implements Serializable */ {
    //private static final long serialVersionUID = 715000866082812683L;

    private final String token;
    private final String secret;
    private final @Nullable String rawResponse;

    /**
     * Default constructor
     *
     * @param token  token value
     * @param secret token secret
     */
    public Token(String token, String secret) {
        this(token, secret, null);
    }

    public Token(String token, String secret, @Nullable String rawResponse) {
        this.token = token;
        this.secret = secret;
        this.rawResponse = rawResponse;
    }

    public String getTokenString() {
        return token;
    }

    public String getSecret() {
        return secret;
    }

    public String getRawResponse() {
        if (rawResponse == null)
            throw new RuntimeException("This token object was not constructed by scribe and does not have a rawResponse");
        return rawResponse;
    }

    @Override
    public String toString() {
        //return String.format("Token[%s , %s]", token, secret);
        return "Token[" + token + " , " + secret + "]";
    }
}
