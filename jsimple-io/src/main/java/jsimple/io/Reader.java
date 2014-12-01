/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from Apache Harmony
 * http://harmony.apache.org (collectively, “Third Party Code”). Microsoft Mobile
 * is not the original author of the Third Party Code. The original copyright
 * notice and the license, under which Microsoft Mobile received such Third Party
 * Code, are set forth below.
 *
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
 * This class was based on, and modified from, the Apache Harmony java.io.Reader class.  Unlike the Java Reader class,
 * this doesn't do locking and doesn't throw any checked exceptions.
 * <p/>
 * The base class for all readers. A reader is a means of reading data from a source in a character-wise manner.
 * <p/>
 * This abstract class does not provide a fully working implementation, so it needs to be subclassed, and at least the
 * {@link #read(char[], int, int)} and {@link #close()} methods needs to be overridden. Overriding some of the
 * non-abstract methods is also often advised, since it might result in higher efficiency.
 */
public abstract class Reader extends jsimple.lang.AutoCloseable {
    /**
     * Reads a single character from this reader and returns it as an integer with the two higher-order bytes set to 0.
     * Returns -1 if the end of the reader has been reached.
     *
     * @return the character read or -1 if the end of the reader has been reached
     * @throws IOException if this reader is closed or some other I/O error occurs
     */
    public int read() {
        char[] charArray = new char[1];
        if (read(charArray, 0, 1) != -1)
            return charArray[0];
        return -1;
    }

    /**
     * Reads characters from this reader and stores them in the character array {@code buf} starting at offset 0.
     * Returns the number of characters actually read or -1 if the end of the reader has been reached.
     *
     * @param buffer character array to store the characters read.
     * @return the number of characters read or -1 if the end of the reader has been reached
     * @throws IOException if this reader is closed or some other I/O error occurs
     */
    public int read(char[] buffer) {
        return read(buffer, 0, buffer.length);
    }

    /**
     * Reads at most {@code count} characters from this reader and stores them at {@code offset} in the character array
     * {@code buf}. Returns the number of characters actually read or -1 if the end of the reader has been reached.
     *
     * @param buffer the character array to store the characters read
     * @param offset the initial position in {@code buffer} to store the characters read from this reader
     * @param count  the maximum number of characters to read
     * @return the number of characters read or -1 if the end of the reader has been reached
     * @throws IOException if this reader is closed or some other I/O error occurs
     */
    public abstract int read(char[] buffer, int offset, int count);
}
