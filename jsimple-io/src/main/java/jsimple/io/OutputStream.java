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

import jsimple.util.ByteArrayRange;
import jsimple.util.StringUtils;
import org.jetbrains.annotations.Nullable;

/**
 * This class was based on, and modified from, the Apache Harmony java.io.OutputStream class.  Unlike the standard Java
 * OutputStream class, this doesn't throw any checked exceptions.
 * <p/>
 * The base class for all output streams. An output stream is a means of writing data to a target in a byte-wise manner.
 * Most output streams expect the {@link #flush()} method to be called before closing the stream, to ensure all data is
 * actually written through.
 * <p/>
 * This abstract class does not provide a fully working implementation, so it needs to be subclassed, and at least the
 * {@link #write(int)} method needs to be overridden. Overriding some of the non-abstract methods is also often advised,
 * since it might result in higher efficiency.
 *
 * @author Bret Johnson
 * @see jsimple.io.InputStream
 * @since 10/7/12 12:31 AM
 */
public abstract class OutputStream extends jsimple.lang.AutoCloseable {
    private @Nullable ClosedListener closedListener = null;

    /**
     * Writes the entire contents of the byte array {@code buffer} to this stream.
     *
     * @param buffer the buffer to be written
     * @throws IOException if an error occurs while writing to this stream
     */
    public void write(byte[] buffer) {
        write(buffer, 0, buffer.length);
    }

    /**
     * Writes {@code count} bytes from the byte array {@code buffer} starting at position {@code offset} to this
     * stream.
     *
     * @param buffer the buffer to be written
     * @param offset the start position in {@code buffer} from where to get bytes
     * @param length the number of bytes from {@code buffer} to write to this stream
     * @throws IOException if an error occurs while writing to this stream
     */
    public void write(byte[] buffer, int offset, int length) {
        for (int i = offset; i < offset + length; i++)
            write(buffer[i]);
    }

    public void write(ByteArrayRange byteArrayRange) {
        write(byteArrayRange.getBytes(), byteArrayRange.getPosition(), byteArrayRange.getLength());
    }

    /**
     * Writes a single byte to this stream. Only the least significant byte of the integer {@code oneByte} is written to
     * the stream.
     *
     * @param oneByte the byte to be written.
     * @throws java.io.IOException if an error occurs while writing to this stream.
     */
    public abstract void write(int oneByte);

    /**
     * Write the string, assumed to contain only Latin1 characters (Unicode low 256 characters), to the stream.  A
     * single byte is written for each character; this the most commonly used single byte encoding for text.  If the
     * string contains any non-Latin1 characters, an exception is thrown.
     *
     * @param s string to write
     */
    public void writeLatin1EncodedString(String s) {
        write(StringUtils.toLatin1BytesFromString(s));
    }

    /**
     * Flushes this stream. Implementations of this method should ensure that any buffered data is written out. This
     * implementation does nothing.
     *
     * @throws IOException if an error occurs while flushing this stream
     */
    public void flush() {
    }

    /**
     * Write the string to the stream using UTF-8 encoding.
     *
     * @param s string to write
     */
    public void writeUtf8EncodedString(String s) {
        Utf8OutputStreamWriter writer = new Utf8OutputStreamWriter(this, false);
        try {
            writer.write(s);
        } finally {
            writer.close();
        }
    }

    /**
     * Closes this stream, freeing any resources associated with it and then calling the closedListener, if one is set.
     * If the stream is closed multiple times, the closedListener is called exactly once, on the first close.
     *
     * @throws IOException if an error occurs while closing this stream.
     */
    @Override public final void close() {
        doClose();

        // If there's a closedListener, null it out (so we don't call it multiple times), then call it.  Note that we
        // null it out before calling onClosed, so a close() call from inside onClosed won't generate another call
        // to onClosed.
        if (closedListener != null) {
            ClosedListener localClosedListener = closedListener;
            closedListener = null;
            localClosedListener.onClosed();
        }
    }

    /**
     * This is an internal method, implemented by subclasses to close any resources associated with the stream. The
     * external close() method calls this method, then calls all the closed listeners (if any), assuming this method
     * didn't throw an exception, to notify of the stream being closed.  Implementations of this method should free any
     * resources used by the stream.
     *
     * @throws IOException if an error occurs while closing this stream.
     */
    protected abstract void doClose();

    @Nullable public ClosedListener getClosedListener() {
        return closedListener;
    }

    /**
     * A ClosedListener provides a way to be notified when the stream is closed.
     *
     * @param closedListener
     */
    public void setClosedListener(@Nullable ClosedListener closedListener) {
        this.closedListener = closedListener;
    }
}
