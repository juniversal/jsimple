/*
 * Copyright (c) 2012-2015 Microsoft Mobile.  All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * This file is based on or incorporates material from Apache Commons Net
 * http://commons.apache.org/proper/commons-net (collectively, "Third Party Code").
 * Microsoft Mobile is not the original author of the Third Party Code. The
 * original copyright notice and the license, under which Microsoft Mobile received
 * such Third Party Code, are set forth below.
 *
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
        byte[] binaryData = new byte[]{(byte) '1', (byte) '2', (byte) '3'};

        byte[] encoded = Base64.encodeBase64(binaryData);
        assertNotNull(encoded);
        assertEquals(4, encoded.length);
    }
}
