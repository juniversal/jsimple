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

/**
 * This class was based on, and modified from, the Apache Harmony java.io.StringWriter class.  Unlike the standard Java
 * StringWriter class, this doesn't do locking, doesn't throw any checked exceptions, and uses a StringBuilder
 * internally instead of a StringBuffer (so it should be more efficient, though it's not intended to be thread safe).
 * <p/>
 * A specialized {@link Writer} that writes characters to a {@code StringBuffer} in a sequential manner, appending them
 * in the process. The result can later be queried using the {@link #StringWriter(int)} or {@link #toString()} methods.
 *
 * @see StringReader
 */
public class StringWriter extends Writer {
    private StringBuilder buffer;

    /**
     * Constructs a new {@code StringWriter} which has a {@link StringBuffer} allocated with the default size of 16
     * characters. The {@code StringBuffer} is also the {@code lock} used to synchronize access to this writer.
     */
    public StringWriter() {
        super();
        buffer = new StringBuilder(16);
    }

    /**
     * Constructs a new {@code StringWriter} which has a {@link StringBuffer} allocated with a size of {@code
     * initialSize} characters. The {@code StringBuffer} is also the {@code lock} used to synchronize access to this
     * writer.
     *
     * @param initialSize the initial size of the target string buffer.
     */
    public StringWriter(int initialSize) {
        buffer = new StringBuilder(initialSize);
    }

    /**
     * Calling this method has no effect. In contrast to most {@code Writer} subclasses, the other methods in {@code
     * StringWriter} do not throw an {@code IOException} if {@code close()} has been called.
     */
    @Override public void close() {
    }

    /**
     * Calling this method has no effect.
     */
    @Override public void flush() {
    }

    /**
     * Gets a reference to this writer's internal {@link StringBuilder}. Any changes made to the returned buffer are
     * reflected in this writer.
     *
     * @return a reference to this writer's internal {@code StringBuilder}.
     */
    public StringBuilder getBuffer() {
        return buffer;
    }

    /**
     * Gets a copy of the contents of this writer as a string.
     *
     * @return this writer's contents as a string.
     */
    @Override public String toString() {
        return buffer.toString();
    }

    /**
     * Writes {@code count} characters starting at {@code offset} in {@code buffer} to this writer's {@code
     * StringBuffer}.
     *
     * @param cbuf   the non-null character array to write.
     * @param offset the index of the first character in {@code cbuf} to write.
     * @param count  the maximum number of characters to write.
     */
    @Override public void write(char[] cbuf, int offset, int count) {
        buffer.append(cbuf, offset, count);
    }

    /**
     * Writes one character to this writer's {@code StringBuffer}. Only the two least significant bytes of the integer
     * {@code oneChar} are written.
     *
     * @param oneChar the character to write to this writer's {@code StringBuffer}.
     */
    @Override public void write(int oneChar) {
        buffer.append((char) oneChar);
    }

    /**
     * Writes the characters from the specified string to this writer's {@code StringBuffer}.
     *
     * @param str the non-null string containing the characters to write.
     */
    @Override public void write(String str) {
        buffer.append(str);
    }

    /**
     * Writes {@code count} characters from {@code str} starting at {@code offset} to this writer's {@code
     * StringBuffer}.
     *
     * @param str    the non-null string containing the characters to write.
     * @param offset the index of the first character in {@code str} to write.
     * @param count  the number of characters from {@code str} to write.
     */
    @Override public void write(String str, int offset, int count) {
        String sub = str.substring(offset, offset + count);
        buffer.append(sub);
    }
}
