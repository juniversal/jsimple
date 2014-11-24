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

package jsimple.oauth.builder.api;

import jsimple.oauth.extractors.AccessTokenExtractor;
import jsimple.oauth.extractors.TokenExtractor20Impl;
import jsimple.oauth.model.OAuthConfig;
import jsimple.oauth.model.Verb;
import jsimple.oauth.oauth.OAuth20ServiceImpl;
import jsimple.oauth.oauth.OAuthService;

/**
 * Default implementation of the OAuth protocol, version 2.0 (draft 11)
 * <p/>
 * This class is meant to be extended by concrete implementations of the API, providing the endpoints and
 * endpoint-http-verbs.
 * <p/>
 * If your Api adheres to the 2.0 (draft 11) protocol correctly, you just need to extend this class and define the
 * getters for your endpoints.
 * <p/>
 * If your Api does something a bit different, you can override the different extractors or services, in order to
 * fine-tune the process. Please read the javadocs of the interfaces to get an idea of what to do.
 *
 * @author Diego Silveira
 */
public abstract class DefaultOAuthApi20 implements OAuthApi {

    /**
     * Returns the access token extractor.
     *
     * @return access token extractor
     */
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new TokenExtractor20Impl();
    }

    /**
     * Returns the verb for the access token endpoint (defaults to GET)
     *
     * @return access token endpoint verb
     */
    public String getAccessTokenVerb() {
        return Verb.GET;
    }

    /**
     * Returns the URL that receives the access token requests.
     *
     * @return access token URL
     */
    public abstract String getAccessTokenEndpoint();

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param config OAuth 2.0 configuration param object
     * @return the URL where you should redirect your users
     */
    public abstract String getAuthorizationUrl(OAuthConfig config);

    /**
     * {@inheritDoc}
     */
    public OAuthService createService(OAuthConfig config) {
        return new OAuth20ServiceImpl(this, config);
    }

    /**
     * I (Bret Johnson) merged in refresh token support from the scribe fork here: https://github.com/fernandezpablo85/scribe-java/pull/253
     *
     * @return the parameter needed to refresh an access token
     */
    public String getRefreshTokenParameterName() {
        throw new RuntimeException("Refresh token not implemented");
    }
}
