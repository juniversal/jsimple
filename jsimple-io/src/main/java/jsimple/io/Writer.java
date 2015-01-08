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

import jsimple.util.PlatformUtil;

/**
 * This class was based on, and modified from, the Apache Harmony java.io.Writer class.  Unlike the standard Java Writer
 * class, this doesn't do locking and doesn't throw any checked exceptions.
 * <p/>
 * The base class for all writers. A writer is a means of writing data to a target in a character-wise manner. Most
 * output streams expect the {@link #flush()} method to be called before closing the stream, to ensure all data is
 * actually written out.
 * <p/>
 * This abstract class does not provide a fully working implementation, so it needs to be subclassed, and at least the
 * {@link #write(char[], int, int)}, {@link #close()} and {@link #flush()} methods needs to be overridden. Overriding
 * some of the non-abstract methods is also often advised, since it might result in higher efficiency.
 *
 * @see Reader
 */
public abstract class Writer extends jsimple.lang.AutoCloseable {
    private String lineSeparator = PlatformUtil.getLineSeparator();

    /**
     * Closes this writer. Implementations of this method should free any resources associated with the writer.
     *
     * @throws IOException if an error occurs while closing this writer.
     */
    @Override public abstract void close();

    /**
     * Flushes this writer. Implementations of this method should ensure that all buffered characters are written to the
     * target.
     *
     * @throws IOException if an error occurs while flushing this writer.
     */
    public abstract void flush();

    /**
     * Writes the entire character buffer {@code buf} to the target.
     *
     * @param buf the non-null array containing characters to write
     * @throws IOException if this writer is closed or another I/O error occurs.
     */
    public void write(char[] buf) {
        write(buf, 0, buf.length);
    }

    /**
     * Writes {@code count} characters starting at {@code offset} in {@code buf} to the target.
     *
     * @param buf    the non-null character array to write.
     * @param offset the index of the first character in {@code buf} to write.
     * @param count  the maximum number of characters to write.
     * @throws IOException if this writer is closed or another I/O error occurs.
     */
    public abstract void write(char[] buf, int offset, int count);

    /**
     * Writes one character to the target. Only the two least significant bytes of the integer {@code oneChar} are
     * written.
     *
     * @param oneChar the character to write to the target.
     * @throws IOException if this writer is closed or another I/O error occurs.
     */
    public void write(int oneChar) {
        char[] oneCharArray = new char[1];
        oneCharArray[0] = (char) oneChar;
        write(oneCharArray);
    }

    /**
     * Writes the characters from the specified string to the target.
     *
     * @param str the non-null string containing the characters to write
     * @throws IOException if this writer is closed or another I/O error occurs
     */
    public void write(String str) {
        write(str, 0, str.length());
    }

    /**
     * Writes {@code count} characters from {@code str} starting at {@code offset} to the target.
     *
     * @param str    the non-null string containing the characters to write
     * @param offset the index of the first character in {@code str} to write
     * @param count  the number of characters from {@code str} to write.
     * @throws IOException if this writer is closed or another I/O error occurs
     */
    public void write(String str, int offset, int count) {
        char[] charBuffer = new char[count];
        for (int i = 0; i < count; i++)
            charBuffer[i] = str.charAt(offset + i);

        write(charBuffer, 0, charBuffer.length);
    }

    /**
     * Get the line separator being used for this writer.  By default, it's the platform default line separator.
     *
     * @return current line separator
     */
    public String getLineSeparator() {
        return lineSeparator;
    }

    /**
     * Set the line separator for this writer.
     *
     * @param lineSeparator line separator
     */
    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    /**
     * Write the line separator.
     */
    public final void writeln() {
        write(lineSeparator);
    }

    /**
     * Writes the characters from the specified string followed by a line separator.
     *
     * @param str string to write
     */
    public final void writeln(String str) {
        write(str);
        writeln();
    }
}
