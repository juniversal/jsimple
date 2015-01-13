/*
 * Copyright (c) 2012-2015, Microsoft Mobile
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

package goodfriends.facebook;

import goodfriends.util.Connector;
import goodfriends.util.Constants;
import jsimple.oauth.builder.ServiceBuilder;
import jsimple.oauth.builder.api.FacebookOAuthApi;
import jsimple.oauth.model.Token;
import jsimple.oauth.model.Verifier;
import jsimple.oauth.oauth.OAuthService;

/**
 * Handles facebook authentication routine
 */
public class FacebookAuthenticator extends Connector {
    private OAuthService authService;

    /**
     * Will build a jsimple facebook oauth service
     *
     * @param scope the permissions that the application will require from the  user : comma separated strings
     */
    public FacebookAuthenticator(String scope) {
        ServiceBuilder serviceBuilder = new ServiceBuilder();
        serviceBuilder.provider(new FacebookOAuthApi());
        serviceBuilder.apiKey(Constants.FACEBOOK_APP_ID);
        serviceBuilder.apiSecret(Constants.FACEBOOK_APP_SECRET);
        serviceBuilder.callback(Constants.FACEBOOK_CALLBACK);
        serviceBuilder.scope(scope);

        authService = serviceBuilder.build();
    }

    public String getAuthorizationUrl() {
        return authService.getAuthorizationUrl(null);
    }

    public Token getAccessToken(String code) {
        Verifier ver = new Verifier(code);
        return authService.getAccessToken(null, ver);
    }

    public Token getRefreshedToken(Token expiredAccessToken) {
        authService.refreshAccessToken(expiredAccessToken, true);
        return expiredAccessToken;
    }
}
