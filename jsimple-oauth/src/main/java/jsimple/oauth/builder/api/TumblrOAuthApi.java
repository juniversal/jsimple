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

import jsimple.oauth.model.Token;

public class TumblrOAuthApi extends DefaultOAuthApi10a {
    private static final String REQUEST_TOKEN_RESOURCE = "http://www.tumblr.com/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "http://www.tumblr.com/oauth/access_token";

    @Override public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_RESOURCE;
    }

    @Override public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_RESOURCE;
    }

    @Override public String getAuthorizationUrl(Token requestToken) {
        return "https://www.tumblr.com/oauth/authorize?oauth_token=" + requestToken.getTokenString();
    }
}
