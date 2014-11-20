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
 * This code was adapted from Apache Commons Net
 * (http://commons.apache.org/proper/commons-net/).
 * The original Apache Commons copyright is below.
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package jsimple.util;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * Test base 64 encoding/decoding.   Some tests below were taken from http://stackoverflow.com/questions/12069598/is-there-a-dataset-available-to-fully-test-a-base64-encode-decoder.
 */
public class Base64Test extends UnitTest {
    @Test public void testRFC4648() {
        testEncodeAndDecode("", "");
        testEncodeAndDecode("f", "Zg==");
        testEncodeAndDecode("fo", "Zm8=");
        testEncodeAndDecode("foo", "Zm9v");
        testEncodeAndDecode("foob", "Zm9vYg==");
        testEncodeAndDecode("fooba", "Zm9vYmE=");
        testEncodeAndDecode("foobar", "Zm9vYmFy");
    }

    private void testEncodeAndDecode(String original, String expectedEncoded) {
        byte[] originalBytes = StringUtils.toLatin1BytesFromString(original);

        String actualEncoded = Base64.encodeBase64String(originalBytes);

        assertEquals(expectedEncoded, actualEncoded);

        byte[] actualDecoded = Base64.decodeBase64(actualEncoded);
        String actualDecodedString = StringUtils.toStringFromLatin1Bytes(actualDecoded);

        assertEquals(original, actualDecodedString);
    }

    @Test public void testIsBase64() {
        assertTrue(Base64.isBase64((byte) 'b'));
        assertFalse(Base64.isBase64((byte) ' '));
    }

    @Test public void testEncodeBase64EmptyArray() {
        byte[] emptyArray = new byte[0];
        assertArrayEquals(emptyArray, Base64.encodeBase64(emptyArray));
    }

    @Test public void testEncodeBase64ByteArrayBooleanBooleanInt() {
        byte[] binaryData = new byte[]{'1', '2', '3'};

        byte[] encoded = Base64.encodeBase64(binaryData);
        assertNotNull(encoded);
        assertEquals(4, encoded.length);
    }
}
