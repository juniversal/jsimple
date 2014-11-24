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

/**
 * This class contains OAuth constants, used project-wide
 *
 * @author Pablo Fernandez
 */
public class OAuthConstants {
    public static final String TIMESTAMP = "oauth_timestamp";
    public static final String SIGN_METHOD = "oauth_signature_method";
    public static final String SIGNATURE = "oauth_signature";
    public static final String CONSUMER_SECRET = "oauth_consumer_secret";
    public static final String CONSUMER_KEY = "oauth_consumer_key";
    public static final String CALLBACK = "oauth_callback";
    public static final String VERSION = "oauth_version";
    public static final String NONCE = "oauth_nonce";
    public static final String PARAM_PREFIX = "oauth_";
    public static final String TOKEN = "oauth_token";
    public static final String TOKEN_SECRET = "oauth_token_secret";
    public static final String OUT_OF_BAND = "oob";
    public static final String VERIFIER = "oauth_verifier";
    public static final String HEADER = "Authorization";
    public static final Token EMPTY_TOKEN = new Token("", "");
    public static final String SCOPE = "scope";

    //OAuth 2.0
    public static final String ACCESS_TOKEN = "access_token";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String CODE = "code";
    public static final String GRANT_TYPE = "grant_type";
}
