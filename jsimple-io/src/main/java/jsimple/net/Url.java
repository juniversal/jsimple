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
 */

package jsimple.net;

import org.jetbrains.annotations.Nullable;

/**
 * @author Bret Johnson
 * @since 10/7/12 3:28 AM
 */
public class Url {
    private String url;

    public Url(String url) {
        this.url = url;
    }

    /**
     * Return the query part of the URL or null if there is no query party.  For example:
     * <p/>
     * http://www.acme.com/abc?foo=3&bar=4#abcdef  =>  foo=3&bar=4
     * <p/>
     * http://www.acme.com/abc  =>  null
     *
     * @return query part of URL or null
     */
    public @Nullable String getQuery() {
        int questionMarkIndex = url.indexOf('?');
        if (questionMarkIndex == -1)
            return null;
        else {
            int queryStartIndex = questionMarkIndex + 1;

            int hashIndex = url.indexOf('#', queryStartIndex);
            if (hashIndex == -1)
                return url.substring(queryStartIndex);
            else return url.substring(queryStartIndex, hashIndex);
        }
    }
}
