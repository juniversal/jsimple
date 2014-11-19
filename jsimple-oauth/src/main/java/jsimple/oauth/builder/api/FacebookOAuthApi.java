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

import jsimple.oauth.model.OAuthConfig;
import jsimple.oauth.utils.OAuthEncoder;

public class FacebookOAuthApi extends DefaultOAuthApi20 {
    private static final String AUTHORIZE_URL = "https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://graph.facebook.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        // Old code:
        // Preconditions.checkValidUrl(config.getCallback(),
        //      "Must provide a valid url as callback. Facebook does not support OOB");
        assert config.getCallback() != null && !config.getCallback().isEmpty() :
                "Must provide a valid url as callback. Facebook does not support OOB";

        String url = "https://www.facebook.com/dialog/oauth?client_id=" + config.getApiKey() + "&redirect_uri=" +
                OAuthEncoder.encode(config.getCallback());

        // Append scope if present
        String scope = config.getScope();
        if (scope != null)
            url += "&scope=" + OAuthEncoder.encode(scope);

        return url;
    }

    @Override public String getRefreshTokenParameterName() {
        return "fb_exchange_token";
    }
}
