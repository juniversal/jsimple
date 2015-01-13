/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2015 Microsoft Mobile.  All Rights Reserved.
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
 * This file is based on or incorporates material from the Scribe Java OAuth
 * library https://github.com/fernandezpablo85/scribe-java (collectively, "Third
 * Party Code"). Microsoft Mobile is not the original author of the Third Party
 * Code.
 *
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
 */

/*
TODO: MOVE THIS NOTE:
JSimple OAuth is taken from Scribe.  We also merged in this fork:
    https://github.com/fernandezpablo85/scribe-java/pull/253
to make it easier to refresh OAuth2 tokens.
 */

package jsimple.oauth.oauth;

import jsimple.oauth.builder.api.DefaultOAuthApi20;
import jsimple.oauth.model.*;
import jsimple.util.ProgrammerError;
import org.jetbrains.annotations.Nullable;

public class OAuth20ServiceImpl implements OAuthService {
    private static final String VERSION = "2.0";

    private final DefaultOAuthApi20 api;
    private final OAuthConfig config;

    /**
     * Default constructor
     *
     * @param api    OAuth2.0 api information
     * @param config OAuth 2.0 configuration param object
     */
    public OAuth20ServiceImpl(DefaultOAuthApi20 api, OAuthConfig config) {
        this.api = api;
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    public Token getAccessToken(@Nullable Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addQueryStringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addQueryStringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addQueryStringParameter(OAuthConstants.CODE, verifier.getValue());
        request.addQueryStringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        String scope = config.getScope();
        if (scope != null)
            request.addQueryStringParameter(OAuthConstants.SCOPE, scope);
        OAuthResponse response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    public Token refreshAccessToken(Token refreshOrAccessToken, boolean includeSecret) {
        String accessTokenEndpoint = api.getAccessTokenEndpoint();
        if (accessTokenEndpoint.contains("?grant_type=")) {
            // handle the ugly case where the grant_type parameter is already hardcoded in the constant url
            accessTokenEndpoint = accessTokenEndpoint.substring(0, accessTokenEndpoint.indexOf("?"));
        }

        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), accessTokenEndpoint);
        request.addQueryStringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        if (includeSecret)
            request.addQueryStringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addQueryStringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        request.addQueryStringParameter(OAuthConstants.GRANT_TYPE, api.getRefreshTokenParameterName());
        request.addQueryStringParameter(api.getRefreshTokenParameterName(), refreshOrAccessToken.getTokenString());
        OAuthResponse response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    public Token getRequestToken() {
        throw new ProgrammerError("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
    }

    /**
     * {@inheritDoc}
     */
    public String getVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    public void signRequest(Token accessToken, OAuthRequest request) {
        request.addQueryStringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getTokenString());
    }

    /**
     * {@inheritDoc}
     */
    public String getAuthorizationUrl(@Nullable Token requestToken) {
        return api.getAuthorizationUrl(config);
    }
}
