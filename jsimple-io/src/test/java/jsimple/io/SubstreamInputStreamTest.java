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
 * This file is based on or incorporates material from Apache Harmony
 * http://harmony.apache.org (collectively, "Third Party Code"). Microsoft Mobile
 * is not the original author of the Third Party Code. The original copyright
 * notice and the license, under which Microsoft Mobile received such Third Party
 * Code, are set forth below.
 *
 *
 * Copyright 2006, 2010 The Apache Software Foundation.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jsimple.io;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 8/22/13 4:58 PM
 */
public class SubstreamInputStreamTest extends UnitTest {
    @Test public void testReadByte() {
        byte[] testData = new byte[100];
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (byte) i;
        }

        byte[] buffer = new byte[4];
        int byteRead;

        // Test reading up to end

        ByteArrayInputStream outerStream = new ByteArrayInputStream(testData);
        SubstreamInputStream substreamInputStream = new SubstreamInputStream(outerStream, 2);

        byteRead = substreamInputStream.read();
        assertEquals(0, byteRead);

        byteRead = substreamInputStream.read();
        assertEquals(1, byteRead);

        byteRead = substreamInputStream.read();
        assertEquals(-1, byteRead);

        // Test premature end of stream

        outerStream = new ByteArrayInputStream(testData);
        substreamInputStream = new SubstreamInputStream(outerStream, 200);

        for (int i = 0; i < 100; i++) {
            byteRead = substreamInputStream.read();
            assertEquals(i, byteRead);
        }

        boolean exceptionThrown = false;
        try {
            substreamInputStream.read();
        } catch (IOException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test public void testRead() {
        byte[] testData = new byte[100];
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (byte) i;
        }

        byte[] buffer = new byte[4];
        int amountRead;

        // Test reading up to end

        ByteArrayInputStream outerStream = new ByteArrayInputStream(testData);
        SubstreamInputStream substreamInputStream = new SubstreamInputStream(outerStream, 4);

        amountRead = substreamInputStream.read(buffer);
        assertEquals(4, amountRead);
        assertEquals(3, buffer[3]);

        amountRead = substreamInputStream.read(buffer);
        assertEquals(-1, amountRead);

        amountRead = substreamInputStream.read(buffer);
        assertEquals(-1, amountRead);

        // Should always return 0 if asked to read 0 bytes, even at end of stream
        amountRead = substreamInputStream.read(buffer, 2, 0);
        assertEquals(0, amountRead);

        // Test reading past end of substream

        outerStream = new ByteArrayInputStream(testData);
        substreamInputStream = new SubstreamInputStream(outerStream, 4);

        buffer = new byte[10];
        amountRead = substreamInputStream.read(buffer);
        assertEquals(4, amountRead);
        assertEquals(3, buffer[3]);

        amountRead = substreamInputStream.read(buffer);
        assertEquals(-1, amountRead);

        // Test premature end of stream

        outerStream = new ByteArrayInputStream(testData);
        substreamInputStream = new SubstreamInputStream(outerStream, 200);

        buffer = new byte[150];
        amountRead = substreamInputStream.read(buffer);
        assertEquals(100, amountRead);

        boolean exceptionThrown = false;
        try {
            substreamInputStream.read(buffer);
        } catch (IOException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}
