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

package jsimple.oauth.extractors;

import jsimple.oauth.model.Token;
import jsimple.oauth.utils.OAuthEncoder;
import jsimple.util.BasicException;
import jsimple.util.StringIterator;

/**
 * Default implementation of {@AccessTokenExtractor}. Conforms to OAuth 2.0
 */
public class TokenExtractor20Impl implements AccessTokenExtractor {
    private static final String EMPTY_SECRET = "";

    /**
     * {@inheritDoc}
     */
    public Token extract(String response) {
        // Original code regex:
        // private static final String TOKEN_REGEX = "access_token=([^&]+)";

        StringIterator tokenIterator = new StringIterator(response);
        tokenIterator.skipAheadPast("access_token=");
        StringBuilder tokenBuffer = new StringBuilder();
        while (!tokenIterator.isWhitespace() && tokenIterator.curr() != '&' && !tokenIterator.atEnd())
            tokenBuffer.append(tokenIterator.read());
        if (tokenBuffer.toString().isEmpty())
            throw new BasicException("oauth_token is empty string");

        String token = OAuthEncoder.decode(tokenBuffer.toString());
        return new Token(token, EMPTY_SECRET, response);
    }
}
