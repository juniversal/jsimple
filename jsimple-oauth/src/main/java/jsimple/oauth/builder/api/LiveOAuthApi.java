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

package jsimple.oauth.builder.api;

import jsimple.oauth.extractors.AccessTokenExtractor;
import jsimple.oauth.extractors.JsonTokenExtractor;
import jsimple.oauth.model.OAuthConfig;
import jsimple.oauth.utils.OAuthEncoder;

public class LiveOAuthApi extends DefaultOAuthApi20 {
    @Override public String getAccessTokenEndpoint() {
        return "https://login.live.com/oauth20_token.srf?grant_type=authorization_code";
    }

    @Override public String getAuthorizationUrl(OAuthConfig config) {
        // Old code:
        // Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Live does not support OOB");
        assert config.getCallback() != null && !config.getCallback().isEmpty() :
                "Must provide a valid url as callback. Live does not support OOB";

        String url = "https://oauth.live.com/authorize?client_id=" + config.getApiKey() + "&redirect_uri=" +
                OAuthEncoder.encode(config.getCallback()) + "&response_type=code";
        // Append scope if present
        String scope = config.getScope();
        if (scope != null)
            url += OAuthEncoder.encode(scope);

        return url;
    }

    @Override public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override public String getRefreshTokenParameterName() {
        return "refresh_token";
    }
}