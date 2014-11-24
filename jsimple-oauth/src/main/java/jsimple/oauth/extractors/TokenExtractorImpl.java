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

package jsimple.oauth.extractors;

import jsimple.oauth.model.Token;
import jsimple.oauth.utils.OAuthEncoder;
import jsimple.util.CharIterator;

/**
 * Default implementation of {@RequestTokenExtractor} and {@AccessTokenExtractor}. Conforms to OAuth 1.0a
 * <p/>
 * The process for extracting access and request tokens is similar so this class can do both things.
 *
 * @author Pablo Fernandez
 */
public class TokenExtractorImpl implements RequestTokenExtractor, AccessTokenExtractor {
    /**
     * {@inheritDoc}
     */
    public Token extract(String response) {
        // Original code regexes
        // private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
        // private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");

        CharIterator tokenIterator = new CharIterator(response);
        tokenIterator.skipAheadPast("oauth_token=");
        StringBuilder tokenBuffer = new StringBuilder();
        while (!tokenIterator.isWhitespace() && tokenIterator.curr() != '&' && !tokenIterator.atEnd())
            tokenBuffer.append(tokenIterator.read());
        if (tokenBuffer.toString().isEmpty())
            throw new RuntimeException("oauth_token is empty string");

        CharIterator secretIterator = new CharIterator(response);
        secretIterator.skipAheadPast("oauth_token_secret=");
        StringBuilder secretBuffer = new StringBuilder();
        while (!secretIterator.isWhitespace() && secretIterator.curr() != '&' && !secretIterator.atEnd())
            tokenBuffer.append(secretIterator.read());

        String token = OAuthEncoder.decode(tokenBuffer.toString());
        String secret = OAuthEncoder.decode(secretBuffer.toString());
        return new Token(token, secret, response);
    }
}
