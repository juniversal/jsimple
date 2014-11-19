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

import jsimple.unit.UnitTest;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

public class UrlTest extends UnitTest {
    @Test public void testUrl() {
        assertQueryStringMatches("foo&bar", "http://www.mit.edu?foo&bar#abc");
        assertQueryStringMatches("foo&bar", "http://www.mit.edu?foo&bar");
        assertQueryStringMatches("foo&bar?def", "http://www.mit.edu?foo&bar?def");
        assertQueryStringMatches("foo&bar?def", "http://www.mit.edu?foo&bar?def#abc#def");
        assertQueryStringMatches("", "http://www.mit.edu?");
        assertQueryStringMatches(null, "http://www.mit.edu");
    }

    public void assertQueryStringMatches(@Nullable String expectedQueryString, String urlString) {
        @Nullable String queryString = new Url(urlString).getQuery();

        if (expectedQueryString == null || queryString == null) {
            if (!(expectedQueryString == null && queryString == null))
                fail("One of queryString & expectedQueryString is null & the other not null");
        } else assertEquals(expectedQueryString, queryString);
    }
}
