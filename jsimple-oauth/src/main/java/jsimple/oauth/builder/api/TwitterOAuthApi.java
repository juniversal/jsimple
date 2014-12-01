/*
 * Portions copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from the Scribe Java OAuth
 * library https://github.com/fernandezpablo85/scribe-java (collectively, “Third
 * Party Code”). Microsoft Mobile is not the original author of the Third Party
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

package jsimple.oauth.builder.api;

import jsimple.oauth.model.Token;

public class TwitterOAuthApi extends DefaultOAuthApi10a {
    private static final String REQUEST_TOKEN_RESOURCE = "api.twitter.com/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "api.twitter.com/oauth/access_token";

    @Override public String getAccessTokenEndpoint() {
        return "http://" + ACCESS_TOKEN_RESOURCE;
    }

    @Override public String getRequestTokenEndpoint() {
        return "http://" + REQUEST_TOKEN_RESOURCE;
    }

    @Override public String getAuthorizationUrl(Token requestToken) {
        return "https://api.twitter.com/oauth/authorize?oauth_token=" + requestToken.getTokenString();
    }

    public static class SSL extends TwitterOAuthApi {
        @Override public String getAccessTokenEndpoint() {
            return "https://" + ACCESS_TOKEN_RESOURCE;
        }

        @Override public String getRequestTokenEndpoint() {
            return "https://" + REQUEST_TOKEN_RESOURCE;
        }
    }

    /**
     * Twitter 'friendlier' authorization endpoint for OAuth.
     * <p/>
     * Uses SSL.
     */
    public static class Authenticate extends SSL {
        @Override public String getAuthorizationUrl(Token requestToken) {
            return "https://api.twitter.com/oauth/authenticate?oauth_token=" + requestToken.getTokenString();
        }
    }

    /**
     * Just an alias to the default (SSL) authorization endpoint.
     * <p/>
     * Need to include this for symmetry with 'Authenticate' only.
     */
    public static class Authorize extends SSL {
    }
}
