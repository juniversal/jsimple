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

/**
 * This class was based on, and modified from, the Apache Harmony java.io.InputStream class.  Unlike the Java
 * InputStream class, this doesn't support mark, reset, or skip, doesn't throw any checked exceptions, and none of the
 * methods are synchronized.
 * <p/>
 * The base class for all input streams. An input stream is a means of reading data from a source in a byte-wise
 * manner.
 * <p/>
 * Some input streams also support marking a position in the input stream and returning to this position later. This
 * abstract class does not provide a fully working implementation, so it needs to be subclassed, and at least the {@link
 * #read()} method needs to be overridden. Overriding some of the non-abstract methods is also often advised, since it
 * might result in higher efficiency.
 *
 * @author Bret Johnson
 * @see jsimple.io.OutputStream
 * @since 10/7/12 12:31 AM
 */
public abstract class InputStream extends jsimple.lang.AutoCloseable {
    /**
     * Closes this stream.  If the stream is already closed, then this method should do nothing.  Concrete
     * implementations of this class should free any resources during close.
     *
     * @throws IOException if an error occurs while closing this stream
     */
    @Override public abstract void close();

    protected void finalize() {
        close();
    }

    /**
     * Reads a single byte from this stream and returns it as an integer in the range from 0 to 255. Returns -1 if the
     * end of the stream has been reached. Blocks until one byte has been read, the end of the source stream is detected
     * or an exception is thrown.
     *
     * @return the byte read or -1 if the end of stream has been reached
     * @throws IOException if an error occurs while reading
     */
    public abstract int read();
    /* {
        byte[] buffer = new byte[1];
        int result = read(buffer, 0, 1);
        return result == -1 ? -1 : buffer[0] & 0xff;
    } */

    /**
     * Reads bytes from this stream and stores them in the byte array {@code b}.
     *
     * @param buffer the byte array in which to store the bytes read
     * @return the number of bytes actually read or -1 if the end of the stream has been reached
     * @throws IOException if an error occurs while reading
     */
    public int read(byte[] buffer) {
        return read(buffer, 0, buffer.length);
    }

    /**
     * This method is the same as read except that it's guaranteed to read as much as possible, blocking until it's read
     * length bytes or the end of stream is reached.  That is, if it returns < length bytes read, then it's guaranteed
     * that that's all the data left in the stream and the next call to read will return -1.  Subclasses are free to
     * override this method and implement it directly if they already read as much as possible anyway, and thus can
     * implement it a little more efficiently just doing whatever read does.
     *
     * @param buffer the byte array in which to store the bytes read
     * @return the number of bytes actually read or -1 if the end of the stream has been reached
     * @throws IOException if an error occurs while reading
     */
    public int readFully(byte[] buffer) {
        return readFully(buffer, 0, buffer.length);
    }

    /**
     * Reads at most {@code length} bytes from this stream and stores them in the byte array {@code b} starting at
     * {@code offset}.
     *
     * @param buffer the byte array in which to store the bytes read
     * @param offset the initial position in {@code buffer} to store the bytes read from this stream
     * @param length the maximum number of bytes to store in {@code b}
     * @return the number of bytes actually read or -1 if the end of the stream has been reached
     * @throws IOException if an error occurs while reading
     */
    public abstract int read(byte[] buffer, int offset, int length);
    /*
    {
        for (int i = 0; i < length; i++) {
            int c;
            try {
                if ((c = read()) == -1)
                    return i == 0 ? -1 : i;
            } catch (IOException e) {
                if (i != 0)
                    return i;
                throw e;
            }
            buffer[offset + i] = (byte) c;
        }
        return length;
    }
    */

    /**
     * This method is the same as read except that it's guaranteed to read as much as possible, blocking until it's read
     * length bytes or the end of stream is reached.  That is, if it returns < length bytes read, then it's guaranteed
     * that that's all the data left in the stream and the next call to read will return -1.  Subclasses are free to
     * override this method and implement it directly if they already read as much as possible anyway, and thus can
     * implement it a little more efficiently just doing whatever read does.
     *
     * @param buffer the byte array in which to store the bytes read
     * @param offset the initial position in {@code buffer} to store the bytes read from this stream
     * @param length the maximum number of bytes to store in {@code b}
     * @return the number of bytes actually read (which is guaranteed to be as much as possible) or -1 if the end of the
     * stream has been reached
     */
    public int readFully(byte[] buffer, int offset, int length) {
        int amountRead = read(buffer, offset, length);
        if (amountRead <= 0)
            return amountRead;

        while (amountRead < length) {
            int amountThisRead = read(buffer, offset + amountRead, length - amountRead);
            if (amountThisRead < 0)
                break;
            amountRead += amountThisRead;
        }

        return amountRead;
    }

    /**
     * Write the remaining contents of this stream to the specified output stream, closing this input stream when done.
     * The inputStream is closed after it's completely read, though it won't be closed if an exception occurs in the
     * middle of reading from it.
     *
     * @param outputStream output stream to copy to
     */
    public void copyTo(OutputStream outputStream) {
        byte[] buffer = new byte[8 * 1024];

        while (true) {
            int bytesRead = read(buffer);
            if (bytesRead < 0)
                break;
            outputStream.write(buffer, 0, bytesRead);
        }

        close();
    }

    /**
     * Write the remaining contents of this stream to a byte array, closing this input stream when done. The inputStream
     * is closed after it's completely read, though it won't be closed if an exception occurs in the middle of reading
     * from it.
     *
     * @return ByteArrayRange containing the stream contents
     */
    public ByteArrayRange copyToByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyTo(byteArrayOutputStream);
        return byteArrayOutputStream.closeAndGetByteArray();
    }
}
