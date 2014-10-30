/*
 * TODO: UPDATE COPYRIGHT
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

/**
 * Provides Base64 encoding and decoding as defined by RFC 2045.
 * <p>
 * <p>
 * This class implements section <cite>6.8. Base64 Content-Transfer-Encoding</cite> from RFC 2045 <cite>Multipurpose
 * Internet Mail Extensions (MIME) Part One: Format of Internet Message Bodies</cite> by Freed and Borenstein.
 * </p>
 * <p>
 * Since this class operates directly on byte streams, and not character streams, it is hard-coded to only encode/decode
 * character encodings which are compatible with the lower 127 ASCII chart (ISO-8859-1, Windows-1252, UTF-8, etc).
 * </p>
 *
 * @author Apache Software Foundation
 * @version $Id: Base64.java 1489533 2013-06-04 17:49:00Z sebb $
 * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>
 * @since 2.2
 */
public class Base64 {
    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * This array is a lookup table that translates 6-bit positive integer index values into their "Base64 Alphabet"
     * equivalents as specified in Table 1 of RFC 2045.
     * <p>
     * Thanks to "commons" project in ws.apache.org for this code.
     * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
     */
    private static final byte[] STANDARD_ENCODE_TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    /**
     * Byte used to pad output.
     */
    private static final byte PAD = '=';

    /**
     * This array is a lookup table that translates Unicode characters drawn from the "Base64 Alphabet" (as specified in
     * Table 1 of RFC 2045) into their 6-bit positive integer equivalents. Characters that are not in the Base64
     * alphabet but fall within the bounds of the array are translated to -1.
     * <p>
     * Note: '+' and '-' both decode to 62. '/' and '_' both decode to 63. This means decoder seamlessly handles both
     * URL_SAFE and STANDARD base64. (The encoder, on the other hand, needs to know ahead of time what to emit).
     * <p>
     * Thanks to "commons" project in ws.apache.org for this code.
     * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
     */
    private static final byte[] DECODE_TABLE = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54,
            55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
            5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
            24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34,
            35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51
    };

    /**
     * Mask used to extract 6 bits, used when encoding
     */
    private static final int MASK_6BITS = 0x3f;

    /**
     * Mask used to extract 8 bits, used in decoding base64 bytes
     */
    private static final int MASK_8BITS = 0xff;

    // The static final fields above are used for the original static byte[] methods on Base64.
    // The private member fields below are used with the new streaming approach, which requires
    // some state be preserved between calls of encode() and decode().

    /**
     * Convenience constant to help us determine when our buffer is going to run out of room and needs resizing.
     * <code>DECODE_SIZE = 3 + lineSeparator.length;</code>
     */
    private static final int DECODE_SIZE = 3;

    /**
     * Convenience constant to help us determine when our buffer is going to run out of room and needs resizing.
     * <code>ENCODE_SIZE = 4 + lineSeparator.length;</code>
     */
    private static final int ENCODE_SIZE = 4;

    /**
     * Buffer for streaming.
     */
    private byte[] buffer;

    /**
     * Position where next character should be written in the buffer.
     */
    private int pos;

    /**
     * Position where next character should be read from the buffer.
     */
    private int readPos;

    /**
     * Writes to the buffer only occur after every 3 reads when encoding, an every 4 reads when decoding. This variable
     * helps track that.
     */
    private int modulus;

    /**
     * Boolean flag to indicate the EOF has been reached. Once EOF has been reached, this Base64 object becomes useless,
     * and must be thrown away.
     */
    private boolean eof;

    /**
     * Place holder for the 3 bytes we're dealing with for our base64 logic. Bitwise operations store and extract the
     * base64 encoding or decoding from this variable.
     */
    private int x;

    /**
     * Returns whether or not the <code>octet</code> is in the base 64 alphabet.
     *
     * @param octet The value to test
     * @return <code>true</code> if the value is defined in the the base 64 alphabet, <code>false</code> otherwise.
     * @since 1.4
     */
    public static boolean isBase64(byte octet) {
        return octet == PAD || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1);
    }

    /**
     * Encodes binary data using the base64 algorithm.
     *
     * @param binaryData binary data to encode
     * @return String containing Base64 characters.
     * @since 3.2
     */
    public static String encodeBase64String(byte[] binaryData) {
        return StringUtils.toStringFromLatin1Bytes(encodeBase64(binaryData));
    }

    /**
     * Encodes binary data using the base64 algorithm.
     *
     * @param binaryData Array containing binary data to encode.
     * @return Base64-encoded data.
     * @throws IllegalArgumentException Thrown when the input array needs an output array bigger than maxResultSize
     * @since 1.4
     */
    public static byte[] encodeBase64(byte[] binaryData) {
        return new Base64().encode(binaryData);
    }

    /**
     * Decodes a Base64 String into octets
     *
     * @param base64String String containing Base64 data
     * @return Array containing decoded data.
     * @since 1.4
     */
    public static byte[] decodeBase64(String base64String) {
        return new Base64().decode(base64String);
    }

    /**
     * Decodes Base64 data into octets
     *
     * @param base64Data Byte array containing Base64 data
     * @return Array containing decoded data.
     */
    public static byte[] decodeBase64(byte[] base64Data) {
        return new Base64().decode(base64Data);
    }

    /**
     * Creates a Base64 codec.
     *
     * @since 1.4
     */
    public Base64() {
    }

    /**
     * Returns the amount of buffered data available for reading.
     *
     * @return The amount of buffered data available for reading.
     */
    int avail() {
        return buffer != null ? pos - readPos : 0;
    }

    /**
     * Doubles our buffer.
     */
    private void resizeBuffer() {
        if (buffer == null) {
            buffer = new byte[DEFAULT_BUFFER_SIZE];
            pos = 0;
            readPos = 0;
        } else {
            byte[] b = new byte[buffer.length * DEFAULT_BUFFER_RESIZE_FACTOR];
            PlatformUtils.copyBytes(buffer, 0, b, 0, buffer.length);
            buffer = b;
        }
    }

    /**
     * Extracts buffered data into the provided byte[] array, starting at position bPos, up to a maximum of bAvail
     * bytes. Returns how many bytes were actually extracted.
     *
     * @param b      byte[] array to extract the buffered data into.
     * @param bPos   position in byte[] array to start extraction at.
     * @param bAvail amount of bytes we're allowed to extract. We may extract fewer (if fewer are available).
     * @return The number of bytes successfully extracted into the provided byte[] array.
     */
    int readResults(byte[] b, int bPos, int bAvail) {
        if (buffer != null) {
            int len = Math.min(avail(), bAvail);
            if (buffer != b) {
                PlatformUtils.copyBytes(buffer, readPos, b, bPos, len);
                readPos += len;
                if (readPos >= pos) {
                    buffer = null;
                }
            } else {
                // Re-using the original consumer's output array is only
                // allowed for one round.
                buffer = null;
            }
            return len;
        }
        return eof ? -1 : 0;
    }

    /**
     * Sets the streaming buffer. This is a small optimization where we try to buffer directly to the consumer's output
     * array for one round (if the consumer calls this method first) instead of starting our own buffer.
     *
     * @param out      byte[] array to buffer directly to.
     * @param outPos   Position to start buffering into.
     * @param outAvail Amount of bytes available for direct buffering.
     */
    void setInitialBuffer(byte[] out, int outPos, int outAvail) {
        // We can re-use consumer's original output array under
        // special circumstances, saving on some System.arraycopy().
        if (out != null && out.length == outAvail) {
            buffer = out;
            pos = outPos;
            readPos = outPos;
        }
    }

    /**
     * <p>
     * Encodes all of the provided data, starting at inPos, for inAvail bytes. Must be called at least twice: once with
     * the data to encode, and once with inAvail set to "-1" to alert encoder that EOF has been reached, so flush last
     * remaining bytes (if not multiple of 3).
     * </p>
     * <p>
     * Thanks to "commons" project in ws.apache.org for the bitwise operations, and general approach.
     * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
     * </p>
     *
     * @param in      byte[] array of binary data to base64 encode.
     * @param inPos   Position to start reading data from.
     * @param inAvail Amount of bytes available from input for encoding.
     */
    void encode(byte[] in, int inPos, int inAvail) {
        if (eof) {
            return;
        }
        // inAvail < 0 is how we're informed of EOF in the underlying data we're
        // encoding.
        if (inAvail < 0) {
            eof = true;
            if (buffer == null || buffer.length - pos < ENCODE_SIZE) {
                resizeBuffer();
            }
            switch (modulus) {
                case 1:
                    buffer[pos++] = STANDARD_ENCODE_TABLE[(x >> 2) & MASK_6BITS];
                    buffer[pos++] = STANDARD_ENCODE_TABLE[(x << 4) & MASK_6BITS];
                    // URL-SAFE skips the padding to further reduce size.
                    buffer[pos++] = PAD;
                    buffer[pos++] = PAD;
                    break;

                case 2:
                    buffer[pos++] = STANDARD_ENCODE_TABLE[(x >> 10) & MASK_6BITS];
                    buffer[pos++] = STANDARD_ENCODE_TABLE[(x >> 4) & MASK_6BITS];
                    buffer[pos++] = STANDARD_ENCODE_TABLE[(x << 2) & MASK_6BITS];
                    buffer[pos++] = PAD;
                    break;
                default:
                    break;  // other values ignored
            }
        } else {
            for (int i = 0; i < inAvail; i++) {
                if (buffer == null || buffer.length - pos < ENCODE_SIZE) {
                    resizeBuffer();
                }
                modulus = (++modulus) % 3;
                int b = in[inPos++];
                if (b < 0) {
                    b += 256;
                }
                x = (x << 8) + b;
                if (0 == modulus) {
                    buffer[pos++] = STANDARD_ENCODE_TABLE[(x >> 18) & MASK_6BITS];
                    buffer[pos++] = STANDARD_ENCODE_TABLE[(x >> 12) & MASK_6BITS];
                    buffer[pos++] = STANDARD_ENCODE_TABLE[(x >> 6) & MASK_6BITS];
                    buffer[pos++] = STANDARD_ENCODE_TABLE[x & MASK_6BITS];
                }
            }
        }
    }

    /**
     * <p>
     * Decodes all of the provided data, starting at inPos, for inAvail bytes. Should be called at least twice: once
     * with the data to decode, and once with inAvail set to "-1" to alert decoder that EOF has been reached. The "-1"
     * call is not necessary when decoding, but it doesn't hurt, either.
     * </p>
     * <p>
     * Ignores all non-base64 characters. This is how chunked (e.g. 76 character) data is handled, since CR and LF are
     * silently ignored, but has implications for other bytes, too. This method subscribes to the garbage-in,
     * garbage-out philosophy: it will not check the provided data for validity.
     * </p>
     * <p>
     * Thanks to "commons" project in ws.apache.org for the bitwise operations, and general approach.
     * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
     * </p>
     *
     * @param in      byte[] array of ascii data to base64 decode.
     * @param inPos   Position to start reading data from.
     * @param inAvail Amount of bytes available from input for encoding.
     */
    void decode(byte[] in, int inPos, int inAvail) {
        if (eof) {
            return;
        }
        if (inAvail < 0) {
            eof = true;
        }
        for (int i = 0; i < inAvail; i++) {
            if (buffer == null || buffer.length - pos < DECODE_SIZE) {
                resizeBuffer();
            }
            byte b = in[inPos++];
            if (b == PAD) {
                // We're done.
                eof = true;
                break;
            } else {
                if (b >= 0 && b < DECODE_TABLE.length) {
                    int result = DECODE_TABLE[b];
                    if (result >= 0) {
                        modulus = (++modulus) % 4;
                        x = (x << 6) + result;
                        if (modulus == 0) {
                            buffer[pos++] = (byte) ((x >> 16) & MASK_8BITS);
                            buffer[pos++] = (byte) ((x >> 8) & MASK_8BITS);
                            buffer[pos++] = (byte) (x & MASK_8BITS);
                        }
                    }
                }
            }
        }

        // Two forms of EOF as far as base64 decoder is concerned: actual
        // EOF (-1) and first time '=' character is encountered in stream.
        // This approach makes the '=' padding characters completely optional.
        if (eof && modulus != 0) {
            x = x << 6;
            switch (modulus) {
                case 2:
                    x = x << 6;
                    buffer[pos++] = (byte) ((x >> 16) & MASK_8BITS);
                    break;
                case 3:
                    buffer[pos++] = (byte) ((x >> 16) & MASK_8BITS);
                    buffer[pos++] = (byte) ((x >> 8) & MASK_8BITS);
                    break;
                default:
                    break;  // other values ignored
            }
        }
    }

    /**
     * Decodes a String containing containing characters in the Base64 alphabet.
     *
     * @param pArray A String containing Base64 character data
     * @return a byte array containing binary data
     * @since 1.4
     */
    public byte[] decode(String pArray) {
        return decode(StringUtils.toLatin1BytesFromString(pArray));
    }

    /**
     * Decodes a byte[] containing containing characters in the Base64 alphabet.
     *
     * @param pArray A byte array containing Base64 character data
     * @return a byte array containing binary data
     */
    public byte[] decode(byte[] pArray) {
        reset();
        if (pArray.length == 0) {
            return pArray;
        }

        long len = (pArray.length * 3) / 4;
        byte[] buf = new byte[(int) len];
        setInitialBuffer(buf, 0, buf.length);
        decode(pArray, 0, pArray.length);
        decode(pArray, 0, -1); // Notify decoder of EOF.

        // Would be nice to just return buf (like we sometimes do in the encode
        // logic), but we have no idea what the line-length was (could even be
        // variable).  So we cannot determine ahead of time exactly how big an
        // array is necessary.  Hence the need to construct a 2nd byte array to
        // hold the final result:

        byte[] result = new byte[pos];
        readResults(result, 0, result.length);
        return result;
    }

    /**
     * Encodes a byte[] containing binary data, into a byte[] containing characters in the Base64 alphabet.
     *
     * @param pArray a byte array containing binary data
     * @return A byte array containing only Base64 character data
     */
    public byte[] encode(byte[] pArray) {
        reset();
        if (pArray.length == 0) {
            return pArray;
        }
        long len = getEncodeLength(pArray);
        byte[] buf = new byte[(int) len];
        setInitialBuffer(buf, 0, buf.length);
        encode(pArray, 0, pArray.length);
        encode(pArray, 0, -1); // Notify encoder of EOF.
        // Encoder might have resized, even though it was unnecessary.
        if (buffer != buf) {
            readResults(buf, 0, buf.length);
        }
        return buf;
    }

    /**
     * Pre-calculates the amount of space needed to base64-encode the supplied array.
     *
     * @param pArray byte[] array which will later be encoded
     * @return amount of space needed to encoded the supplied array.  Returns
     * a long since a max-len array will require Integer.MAX_VALUE + 33%.
     */
    private static long getEncodeLength(byte[] pArray) {
        long len = (pArray.length * 4) / 3;
        long mod = len % 4;
        if (mod != 0) {
            len += 4 - mod;
        }

        return len;
    }

    /**
     * Resets this Base64 object to its initial newly constructed state.
     */
    private void reset() {
        buffer = null;
        pos = 0;
        readPos = 0;
        modulus = 0;
        eof = false;
    }
}
