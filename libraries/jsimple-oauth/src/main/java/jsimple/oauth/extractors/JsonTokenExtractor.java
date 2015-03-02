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

import jsimple.oauth.exceptions.OAuthException;
import jsimple.oauth.model.Token;
import jsimple.util.StringIterator;
import jsimple.util.InvalidFormatException;

public class JsonTokenExtractor implements AccessTokenExtractor {
    public Token extract(String response) {
        assert response != null && !response.isEmpty() : "Cannot extract a token from a null or empty String";

        try {
            // Original regex
            // private Pattern accessTokenPattern = Pattern.compile("\"access_token\":\\s*\"(\\S*?)\"");

            StringIterator tokenIterator = new StringIterator(response);

            tokenIterator.skipAheadPast("\"access_token\":");
            tokenIterator.advancePastWhitespace();
            tokenIterator.checkAndAdvance('\"');

            StringBuilder tokenBuffer = new StringBuilder();
            while (!tokenIterator.isWhitespace() && tokenIterator.curr() != '"' && !tokenIterator.atEnd())
                tokenBuffer.append(tokenIterator.read());

            tokenIterator.checkAndAdvance('\"');

            return new Token(tokenBuffer.toString(), "", response);
        } catch (InvalidFormatException e) {
            throw new OAuthException("Cannot extract an access token. Response was: " + response, e);
        }
    }
}
