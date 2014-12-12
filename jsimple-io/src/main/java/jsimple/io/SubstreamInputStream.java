/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
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

/**
 * @author Bret Johnson
 * @since 8/22/13 3:19 PM
 */
public class SubstreamInputStream extends InputStream {
    private InputStream outerInputStream;
    private int lengthRemaining;

    public SubstreamInputStream(InputStream inputStream, int length) {
        this.outerInputStream = inputStream;
        this.lengthRemaining = length;
    }

    /**
     * For a SubstreamInputStream we of course don't want to close the outer stream when this stream is closed, as
     * there's more of the outer stream that follows this one.
     */
    @Override public void close() {
    }

    @Override public int read() {
        // If at the end of our substream, return -1
        if (lengthRemaining <= 0)
            return -1;

        int byteRead = outerInputStream.read();
        if (byteRead == -1)
            throw new IOException(
                    "SubstreamInputStream outer stream returned end of stream prematurely; there are {} bytes left to read",
                    lengthRemaining);

        lengthRemaining -= 1;
        return byteRead;
    }

    @Override public int read(byte[] buffer, int offset, int length) {
        // Return 0 if not asked to read anything, per the InputStream spec
        if (length == 0)
            return 0;

        // If at the end of our substream, return -1
        if (lengthRemaining <= 0)
            return -1;

        if (length > lengthRemaining)
            length = lengthRemaining;

        int amountRead = outerInputStream.read(buffer, offset, length);
        if (amountRead == -1)
            throw new IOException(
                    "SubstreamInputStream outer stream returned end of stream prematurely; there are {} bytes left to read",
                    lengthRemaining);

        lengthRemaining -= amountRead;
        return amountRead;
    }
}
