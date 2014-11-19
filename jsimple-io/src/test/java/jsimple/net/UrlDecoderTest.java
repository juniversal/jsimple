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
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 11/25/12 11:16 PM
 */
public class UrlDecoderTest extends UnitTest {
    @Test public void testDecode() {
        assertEquals(".-_*", UrlDecoder.decode(".-_*"));   // None encoded
        assertEquals("$+!'(),", UrlDecoder.decode("%24%2B%21%27%28%29%2C"));   // All encoded
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
                UrlDecoder.decode("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));   // None encoded
        assertEquals("x\u00eb\u0106\u04b8abc\ufb9a", UrlDecoder.decode("x%C3%AB%C4%86%D2%B8abc%EF%AE%9A"));
        assertEquals("abc=def?sdf&hil sdlkfj l sdf slkdfj 123&def",
                UrlDecoder.decode("abc%3Ddef%3Fsdf%26hil+sdlkfj+l+sdf+slkdfj+123%26def"));
    }

    @Test public void testDecodePath() {
        assertEquals("/", UrlDecoder.decodePath("/").toString());
        assertEquals("/", UrlDecoder.decodePath("").toString());

        assertEquals("/abc", UrlDecoder.decodePath("abc").toString());

        assertEquals("/abc/def", UrlDecoder.decodePath("abc/def").toString());
        assertEquals("/abc/def", UrlDecoder.decodePath("abc/def/").toString());

        assertEquals("/a!@#/def", UrlDecoder.decodePath("a!%40%23/def/").toString());
        assertEquals("/ab/c/def", UrlDecoder.decodePath("/ab%2Fc/def").toString());
        assertEquals("ab/c", UrlDecoder.decodePath("/ab%2Fc/def").getFirstElement());
    }
}
