/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from BlowfishJ
 * http://blowfishj.sourceforge.net (collectively, “Third Party Code”). Microsoft
 * Mobile is not the original author of the Third Party Code. The original
 * copyright notice and the license, under which Microsoft Mobile received such
 * Third Party Code, are set forth below. Such licenses and notices are provided
 * for informational purposes only.
 *
 *
 * Copyright 2004 Markus Hahn
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
 */

package jsimple.oauth.utils;

/**
 * @author Bret Johnson
 * @since 8/18/2014 5:10 PM
 */

import jsimple.util.PlatformUtils;

/**
 * SHA-1 message digest implementation, translated from C source code (the origin is unknown).
 */
public final class Sha1 {
    /**
     * size of a SHA-1 digest in octets
     */
    public final static int DIGEST_SIZE = 20;

    private int[] m_state;
    private long m_lCount;
    private byte[] m_digestBits;
    private int[] m_block;
    private int m_nBlockIndex;

    /**
     * Default constructor.
     */
    public Sha1() {
        reset();
    }

    static int rol(int nValue, int nBits) {
        return ((nValue << nBits) | (nValue >>> (32 - nBits)));
    }

    private int blk0(int nI) {
        return m_block[nI] =
                ((rol(m_block[nI], 24) & 0xff00ff00) |
                        (rol(m_block[nI], 8) & 0x00ff00ff));
    }

    final int blk(int nI) {
        return (
                m_block[nI & 15] =
                        rol(
                                m_block[(nI + 13) & 15] ^
                                        m_block[(nI + 8) & 15] ^
                                        m_block[(nI + 2) & 15] ^
                                        m_block[nI & 15],
                                1));
    }

    private void r0(int[] data, int nV, int nW, int nX, int nY, int nZ, int nI) {
        data[nZ] += ((data[nW] & (data[nX] ^ data[nY])) ^ data[nY])
                + blk0(nI)
                + 0x5a827999
                + rol(data[nV], 5);
        data[nW] = rol(data[nW], 30);
    }

    private void r1(int[] data, int nV, int nW, int nX, int nY, int nZ, int nI) {
        data[nZ] += ((data[nW] & (data[nX] ^ data[nY])) ^ data[nY])
                + blk(nI)
                + 0x5a827999
                + rol(data[nV], 5);
        data[nW] = rol(data[nW], 30);
    }

    private void r2(int[] data, int nV, int nW, int nX, int nY, int nZ, int nI) {
        data[nZ] += (data[nW] ^ data[nX] ^ data[nY])
                + blk(nI)
                + 0x6eD9eba1
                + rol(data[nV], 5);
        data[nW] = rol(data[nW], 30);
    }

    private void r3(int[] data, int nV, int nW, int nX, int nY, int nZ, int nI) {
        data[nZ]
                += (((data[nW] | data[nX]) & data[nY]) | (data[nW] & data[nX]))
                + blk(nI)
                + 0x8f1bbcdc
                + rol(data[nV], 5);
        data[nW] = rol(data[nW], 30);
    }

    final void r4(int[] data, int nV, int nW, int nX, int nY, int nZ, int nI) {
        data[nZ] += (data[nW] ^ data[nX] ^ data[nY])
                + blk(nI)
                + 0xca62c1d6
                + rol(data[nV], 5);
        data[nW] = rol(data[nW], 30);
    }

    private void transform() {

        int[] dd = new int[5];
        dd[0] = m_state[0];
        dd[1] = m_state[1];
        dd[2] = m_state[2];
        dd[3] = m_state[3];
        dd[4] = m_state[4];
        r0(dd, 0, 1, 2, 3, 4, 0);
        r0(dd, 4, 0, 1, 2, 3, 1);
        r0(dd, 3, 4, 0, 1, 2, 2);
        r0(dd, 2, 3, 4, 0, 1, 3);
        r0(dd, 1, 2, 3, 4, 0, 4);
        r0(dd, 0, 1, 2, 3, 4, 5);
        r0(dd, 4, 0, 1, 2, 3, 6);
        r0(dd, 3, 4, 0, 1, 2, 7);
        r0(dd, 2, 3, 4, 0, 1, 8);
        r0(dd, 1, 2, 3, 4, 0, 9);
        r0(dd, 0, 1, 2, 3, 4, 10);
        r0(dd, 4, 0, 1, 2, 3, 11);
        r0(dd, 3, 4, 0, 1, 2, 12);
        r0(dd, 2, 3, 4, 0, 1, 13);
        r0(dd, 1, 2, 3, 4, 0, 14);
        r0(dd, 0, 1, 2, 3, 4, 15);
        r1(dd, 4, 0, 1, 2, 3, 16);
        r1(dd, 3, 4, 0, 1, 2, 17);
        r1(dd, 2, 3, 4, 0, 1, 18);
        r1(dd, 1, 2, 3, 4, 0, 19);
        r2(dd, 0, 1, 2, 3, 4, 20);
        r2(dd, 4, 0, 1, 2, 3, 21);
        r2(dd, 3, 4, 0, 1, 2, 22);
        r2(dd, 2, 3, 4, 0, 1, 23);
        r2(dd, 1, 2, 3, 4, 0, 24);
        r2(dd, 0, 1, 2, 3, 4, 25);
        r2(dd, 4, 0, 1, 2, 3, 26);
        r2(dd, 3, 4, 0, 1, 2, 27);
        r2(dd, 2, 3, 4, 0, 1, 28);
        r2(dd, 1, 2, 3, 4, 0, 29);
        r2(dd, 0, 1, 2, 3, 4, 30);
        r2(dd, 4, 0, 1, 2, 3, 31);
        r2(dd, 3, 4, 0, 1, 2, 32);
        r2(dd, 2, 3, 4, 0, 1, 33);
        r2(dd, 1, 2, 3, 4, 0, 34);
        r2(dd, 0, 1, 2, 3, 4, 35);
        r2(dd, 4, 0, 1, 2, 3, 36);
        r2(dd, 3, 4, 0, 1, 2, 37);
        r2(dd, 2, 3, 4, 0, 1, 38);
        r2(dd, 1, 2, 3, 4, 0, 39);
        r3(dd, 0, 1, 2, 3, 4, 40);
        r3(dd, 4, 0, 1, 2, 3, 41);
        r3(dd, 3, 4, 0, 1, 2, 42);
        r3(dd, 2, 3, 4, 0, 1, 43);
        r3(dd, 1, 2, 3, 4, 0, 44);
        r3(dd, 0, 1, 2, 3, 4, 45);
        r3(dd, 4, 0, 1, 2, 3, 46);
        r3(dd, 3, 4, 0, 1, 2, 47);
        r3(dd, 2, 3, 4, 0, 1, 48);
        r3(dd, 1, 2, 3, 4, 0, 49);
        r3(dd, 0, 1, 2, 3, 4, 50);
        r3(dd, 4, 0, 1, 2, 3, 51);
        r3(dd, 3, 4, 0, 1, 2, 52);
        r3(dd, 2, 3, 4, 0, 1, 53);
        r3(dd, 1, 2, 3, 4, 0, 54);
        r3(dd, 0, 1, 2, 3, 4, 55);
        r3(dd, 4, 0, 1, 2, 3, 56);
        r3(dd, 3, 4, 0, 1, 2, 57);
        r3(dd, 2, 3, 4, 0, 1, 58);
        r3(dd, 1, 2, 3, 4, 0, 59);
        r4(dd, 0, 1, 2, 3, 4, 60);
        r4(dd, 4, 0, 1, 2, 3, 61);
        r4(dd, 3, 4, 0, 1, 2, 62);
        r4(dd, 2, 3, 4, 0, 1, 63);
        r4(dd, 1, 2, 3, 4, 0, 64);
        r4(dd, 0, 1, 2, 3, 4, 65);
        r4(dd, 4, 0, 1, 2, 3, 66);
        r4(dd, 3, 4, 0, 1, 2, 67);
        r4(dd, 2, 3, 4, 0, 1, 68);
        r4(dd, 1, 2, 3, 4, 0, 69);
        r4(dd, 0, 1, 2, 3, 4, 70);
        r4(dd, 4, 0, 1, 2, 3, 71);
        r4(dd, 3, 4, 0, 1, 2, 72);
        r4(dd, 2, 3, 4, 0, 1, 73);
        r4(dd, 1, 2, 3, 4, 0, 74);
        r4(dd, 0, 1, 2, 3, 4, 75);
        r4(dd, 4, 0, 1, 2, 3, 76);
        r4(dd, 3, 4, 0, 1, 2, 77);
        r4(dd, 2, 3, 4, 0, 1, 78);
        r4(dd, 1, 2, 3, 4, 0, 79);
        m_state[0] += dd[0];
        m_state[1] += dd[1];
        m_state[2] += dd[2];
        m_state[3] += dd[3];
        m_state[4] += dd[4];
    }

    /**
     * Initializes (or resets) the hasher for a new session.
     */
    public void reset() {
        m_state = new int[5];
        m_state[0] = 0x67452301;
        m_state[1] = 0xefcdab89;
        m_state[2] = 0x98badcfe;
        m_state[3] = 0x10325476;
        m_state[4] = 0xc3d2e1f0;

        m_lCount = 0;
        m_nBlockIndex = 0;
        m_block = new int[16];

        m_digestBits = new byte[DIGEST_SIZE];
    }

    /**
     * Adds a single byte to the digest.
     *
     * @param bB the byte to add
     */
    public void update(byte bB) {
        int nMask = (m_nBlockIndex & 3) << 3;

        m_lCount += 8;
        m_block[m_nBlockIndex >> 2] &= ~(0xff << nMask);
        m_block[m_nBlockIndex >> 2] |= (bB & 0xff) << nMask;
        m_nBlockIndex++;
        if (m_nBlockIndex == 64) {
            transform();
            m_nBlockIndex = 0;
        }
    }

    /**
     * Adds a byte array to the digest.
     *
     * @param data the data to add
     */
    public void update(byte[] data) {
        update(data, 0, data.length);
    }

    /**
     * Adds a portion of a byte array to the digest.
     *
     * @param data the data to add
     */
    public void update(byte[] data, int nOfs, int nLen) {
        for (int nEnd = nOfs + nLen; nOfs < nEnd; nOfs++) {
            update(data[nOfs]);
        }
    }

    /**
     * Finalizes the digest.
     */
    private void finish() {
        int nI;
        byte[] bits = new byte[8];

        for (nI = 0; nI < 8; nI++) {
            bits[nI] = (byte) ((m_lCount >>> (((7 - nI) << 3))) & 0xff);
        }

        update((byte) 128);
        while (m_nBlockIndex != 56) {
            update((byte) 0);
        }

        for (nI = 0; nI < bits.length; nI++) {
            update(bits[nI]);
        }

        for (nI = 0; nI < 20; nI++) {
            m_digestBits[nI] = (byte) ((m_state[nI >> 2] >> ((3 - (nI & 3)) << 3)) & 0xff);
        }
    }

    /**
     * Finishes computing the digest & then returns it.   After that, the instance is reset.
     *
     * @return the digest bytes as an array if DIGEST_SIZE bytes
     */
    public byte[] digest() {
        finish();

        byte[] result = m_digestBits;
        reset();
        return result;
    }

    /**
     * Does a final update, with the passed input, then computes the SHA1 digest, which is returned.   After that the
     * instance is reset.
     *
     * @param input data to update
     * @return SHA1 digest
     */
    public byte[] digest(byte[] input) {
        update(input);
        return digest();
    }

    /**
     * Compute the HMAC-SHA1 for the specified message & key.   The implementation here was taken from
     * http://en.wikipedia.org/wiki/Hash-based_message_authentication_code pseudocode, then implement by me (Bret).
     *
     * @param key     HMAC key
     * @param message message
     * @return SHA1 HMAC
     */
    public static byte[] hmac(byte[] key, final byte[] message) {
        final int BLOCK_SIZE = 64;

        if (key.length > BLOCK_SIZE) {
            key = new Sha1().digest(key);
        }

        byte[] normalizedKey;
        if (key.length < BLOCK_SIZE) {
            // Zero pad the key up to BLOCK_SIZE.   Take advantage of the fact that arrays are always initialized to 0
            // in Java
            normalizedKey = new byte[BLOCK_SIZE];
            PlatformUtils.copyBytes(key, 0, normalizedKey, 0, key.length);
        } else normalizedKey = key;

        Sha1 sha1Inner = new Sha1();

        for (int i = 0; i < BLOCK_SIZE; i++) {
            byte b = (byte) ((byte) 0x36 ^ normalizedKey[i]);
            sha1Inner.update(b);
        }

        byte[] hashInner = sha1Inner.digest(message);

        Sha1 sha1Outer = new Sha1();
        for (int i = 0; i < BLOCK_SIZE; i++) {
            byte b = (byte) ((byte) 0x5c ^ normalizedKey[i]);
            sha1Outer.update(b);
        }

        return sha1Outer.digest(hashInner);
    }

    /**
     * Runs an integrity test.
     */
    public static void main(String[] args) {
        final String SELFTEST_MESSAGE =
                "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq";

        final byte[] SELFTEST_DIGEST =
                {
                        (byte) 0x84, (byte) 0x98, (byte) 0x3e, (byte) 0x44, (byte) 0x1c,
                        (byte) 0x3b, (byte) 0xd2, (byte) 0x6e, (byte) 0xba, (byte) 0xae,
                        (byte) 0x4a, (byte) 0xa1, (byte) 0xf9, (byte) 0x51, (byte) 0x29,
                        (byte) 0xe5, (byte) 0xe5, (byte) 0x46, (byte) 0x70, (byte) 0xf1
                };

        Sha1 tester = new Sha1();

        for (int nI1 = 0, nC = SELFTEST_MESSAGE.length(); nI1 < nC; nI1++) {
            tester.update((byte) (SELFTEST_MESSAGE.charAt(nI1) & 0x0ff));
        }

        byte[] digest = tester.digest();

        boolean failed = false;
        for (int nI = 0; nI < DIGEST_SIZE; nI++) {
            if (digest[nI] != SELFTEST_DIGEST[nI]) {
                failed = true;
                break;
            }
        }

        System.out.println(failed ? "Test failed!" : "Test succeeded");
    }
}
