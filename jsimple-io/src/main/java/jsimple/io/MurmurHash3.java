/*
 * Copyright (c) 2012-2014, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jsimple.io;

import jsimple.lang.Math;

/**
 * MurmurHash3 is a modern, fast, well regarded non-cryptographic hashing algorithm.  Non-cryptographic means that while
 * hashes are very well distributed for arbitrary data, if an attacker tries to attack the algorithm they might be able
 * to find some sort of pattern.  Non-cryptographic hashes trade security for speed; cryptographic hashes, like SHA-256,
 * are slower.
 * <p/>
 * There are multiple variants of MurmurHash3.  This one is x86_128 (aka Murmur 3C), little endian.
 * <p/>
 * I selected it when looking for hash/checksum algorithm meeting these criteria: (1) Can generate 64 bit hashes.  32
 * bits wasn't enough to ensure the odds of collision are virtually 0. (2) Is fast, as fast as possible. (3) Is fast
 * even on 32 bit architectures, as most mobile devices today have 32 bit processors.
 * <p/>
 * The Murmur3 x86_128 algorithm met those criteria well.  32 bits output wasn't enough, so we use the 128 bit variant
 * and throw the top 64 bits away (though with the API below, you can get all 128 bits if you want). The x86 variant
 * uses all 32 bit arithmetic, so it runs quickly on 32 & 64 bit processors.  x64 Murmur is somewhat faster on 64 bit
 * machines, but much slower on 32 bit, so it wasn't a good choice.  Other modern hash algorithms generating > 32 bits
 * output (e.g. SpookyHash and CityHash) use 64 arithmetic, with no 32 bit variant available, so they weren't good
 * choices.
 * <p/>
 * This Java code was implemented from the C reference implementation, here:
 * <p/>
 * http://code.google.com/p/smhasher/source/browse/trunk/MurmurHash3.cpp?r=150
 *
 * @author Austin Appleby (MurmurHash3 algorithm) and C reference code
 * @author Bret Johnson (Java port)
 * @since 5/10/13 9:51 PM
 */
public class MurmurHash3 {
    private int /* uint32_t */ h1;
    private int /* uint32_t */ h2;
    private int /* uint32_t */ h3;
    private int /* uint32_t */ h4;

    final int /* const uint32_t */ c1 = 0x239b961b;
    final int /* const uint32_t */ c2 = 0xab0e9789;
    final int /* const uint32_t */ c3 = 0x38b34ae5;
    final int /* const uint32_t */ c4 = 0xa1e38b93;

    public final static int BUFFER_SIZE = 16 * 4;       // 16 16-byte blocks (256 bytes)
    int[] buffer = new int[BUFFER_SIZE];
    int bufferOffset = 0;
    int totalLengthInBytes = 0;
    boolean finished = false;

    public MurmurHash3(int seed) {
        h1 = seed;
        h2 = seed;
        h3 = seed;
        h4 = seed;
    }

    public MurmurHash3() {
        this(0);
    }

    public void addByte(byte b) {
        emptyBufferIfFull();
        buffer[bufferOffset++] = b;
        totalLengthInBytes += 1;
    }

    public void addBoolean(boolean b) {
        addByte(b ? (byte) 1 : (byte) 0);
    }

    public void addChar(char c) {
        emptyBufferIfFull();
        buffer[bufferOffset++] = c;
        totalLengthInBytes += 2;
    }

    public void addInt(int i) {
        emptyBufferIfFull();
        buffer[bufferOffset++] = i;
        totalLengthInBytes += 4;
    }

    public void addLong(long l) {
        addInt((int) l);
        addInt((int) (l >> 32));
    }

    public void addString(String s) {
        int length = s.length();
        int evenLength = (length / 2) * 2;

        for (int i = 0; i < evenLength - 1; ) {
            addInt(s.charAt(i + 1) << 16 | (s.charAt(i) & 0xFFFF));
            i += 2;
        }

        // If there's an odd number of characters, add the last one zero padded, filling up a full int
        if (length > evenLength)
            addChar(s.charAt(length - 1));
    }

    public void addBytes(byte[] data, int offset, int length) {
        int bytesToAdd = length - offset;
        int fullIntsToAdd = bytesToAdd / 4;

        while (fullIntsToAdd > 0) {
            emptyBufferIfFull();

            int intsToAddThisPass = Math.min(fullIntsToAdd, BUFFER_SIZE - bufferOffset);

            for (int i = 0; i < intsToAddThisPass; ++i) {
                buffer[bufferOffset++] = (data[offset + 0] & 0xff) |
                        ((data[offset + 1] & 0xff) << 8) |
                        ((data[offset + 2] & 0xff) << 16) |
                        ((data[offset + 3] & 0xff) << 24);
                offset += 4;
            }

            fullIntsToAdd -= intsToAddThisPass;
        }

        // If 1, 2, or 3 bytes are left over, add one more int that includes them (zero padded)
        if (bytesToAdd % 4 != 0) {
            byte b1 = 0;
            byte b2 = 0;
            byte b3 = 0;

            switch (bytesToAdd % 4) {
                case 3:
                    b3 = data[offset + 2];
                case 2:
                    b2 = data[offset + 1];
                case 1:
                    b1 = data[offset + 0];
            }

            emptyBufferIfFull();
            buffer[bufferOffset++] = (b1 & 0xff) |
                    ((b2 & 0xff) << 8) |
                    ((b3 & 0xff) << 16);
        }

        totalLengthInBytes += bytesToAdd;
    }

    public void addStream(InputStream data) {
        byte[] byteBuffer = new byte[4096];                  // Have a 4K read buffer for stream input

        while (true) {
            int bytesRead = data.readFully(byteBuffer);
            if (bytesRead == -1)
                break;

            addBytes(byteBuffer, 0, bytesRead);
        }
    }

    private void emptyBufferIfFull() {
        if (bufferOffset >= BUFFER_SIZE) {
            body(buffer, BUFFER_SIZE / 4);
            bufferOffset = 0;
        }
    }

    private void ensureFinished() {
        if (finished)
            return;

        int fullBlocks = bufferOffset / 4;
        int intsInLastPartialBlock = bufferOffset % 4;

        // If the total bytes don't go to the end of the block, but the ints do go to the end of the block (which is
        // true when the last block has 13, 14, or 15 bytes), then treat the last block as a partial block, running tail
        // instead of body on it.  We just do that because the normal MurmurHash algorithm does it, in order to always
        // get exactly the same results as standard Murmur, when hashing a stream or byte array.
        if (bufferOffset > 0 && intsInLastPartialBlock == 0 && totalLengthInBytes % 16 != 0) {
            --fullBlocks;
            intsInLastPartialBlock = 4;
        }

        body(buffer, fullBlocks);

        if (intsInLastPartialBlock > 0) {
            int tailBlockOffset = fullBlocks * 4;

            int /* uint32_t */ k1;
            int /* uint32_t */ k2;
            int /* uint32_t */ k3;
            int /* uint32_t */ k4;

            switch (intsInLastPartialBlock) {
                case 4:
                    k4 = buffer[tailBlockOffset + 3];
                    k4 *= c4;
                    k4 = ((k4 << 18) | (k4 >>> (-18)));
                    k4 *= c1;
                    h4 ^= k4;

                case 3:
                    k3 = buffer[tailBlockOffset + 2];
                    k3 *= c3;
                    k3 = ((k3 << 17) | (k3 >>> (-17)));
                    k3 *= c4;
                    h3 ^= k3;

                case 2:
                    k2 = buffer[tailBlockOffset + 1];
                    k2 *= c2;
                    k2 = ((k2 << 16) | (k2 >>> (-16)));
                    k2 *= c3;
                    h2 ^= k2;

                case 1:
                    k1 = buffer[tailBlockOffset + 0];
                    k1 *= c1;
                    k1 = ((k1 << 15) | (k1 >>> (-15)));
                    k1 *= c2;
                    h1 ^= k1;
            }
        }

        finalization(totalLengthInBytes);

        finished = true;
    }

    public long getHash64() {
        ensureFinished();
        return (((long) h1) & 0xffffffffL) | ((((long) h2) & 0xffffffffL) << 32);
    }

    public byte[] getHash128() {
        ensureFinished();

        byte[] hash = new byte[16];
        putblock32(hash, 0, h1);
        putblock32(hash, 4, h2);
        putblock32(hash, 8, h3);
        putblock32(hash, 12, h4);
        return hash;
    }

    private void putblock32(byte[] buffer, int offset, int value) {
        buffer[offset + 0] = (byte) ((value >> 0) & 0xff);
        buffer[offset + 1] = (byte) ((value >> 8) & 0xff);
        buffer[offset + 2] = (byte) ((value >> 16) & 0xff);
        buffer[offset + 3] = (byte) ((value >> 24) & 0xff);
    }

    private void body(int[] buffer, int nblocks) {
        for (int i = 0; i < nblocks; ++i) {
            int /* uint32_t */ k1 = buffer[i * 4];
            int /* uint32_t */ k2 = buffer[i * 4 + 1];
            int /* uint32_t */ k3 = buffer[i * 4 + 2];
            int /* uint32_t */ k4 = buffer[i * 4 + 3];

            k1 *= c1;
            k1 = ((k1 << 15) | (k1 >>> (-15)));
            k1 *= c2;
            h1 ^= k1;

            h1 = ((h1 << 19) | (h1 >>> -19));
            h1 += h2;
            h1 = h1 * 5 + 0x561ccd1b;

            k2 *= c2;
            k2 = ((k2 << 16) | (k2 >>> -16));
            k2 *= c3;
            h2 ^= k2;

            h2 = ((h2 << 17) | (h2 >>> -17));
            h2 += h3;
            h2 = h2 * 5 + 0x0bcaa747;

            k3 *= c3;
            k3 = ((k3 << 17) | (k3 >>> -17));
            k3 *= c4;
            h3 ^= k3;

            h3 = ((h3 << 15) | (h3 >>> -15));
            h3 += h4;
            h3 = h3 * 5 + 0x96cd1c35;

            k4 *= c4;
            k4 = ((k4 << 18) | (k4 >>> -18));
            k4 *= c1;
            h4 ^= k4;

            h4 = ((h4 << 13) | (h4 >>> -13));
            h4 += h1;
            h4 = h4 * 5 + 0x32ac3b17;
        }
    }

    private void finalization(int length) {
        h1 ^= length;
        h2 ^= length;
        h3 ^= length;
        h4 ^= length;

        h1 += h2;
        h1 += h3;
        h1 += h4;
        h2 += h1;
        h3 += h1;
        h4 += h1;

        h1 = fmix32(h1);
        h2 = fmix32(h2);
        h3 = fmix32(h3);
        h4 = fmix32(h4);

        h1 += h2;
        h1 += h3;
        h1 += h4;
        h2 += h1;
        h3 += h1;
        h4 += h1;
    }

    //-----------------------------------------------------------------------------
    // Finalization mix - force all bits of a hash block to avalanche
    private static int fmix32(int h) {
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;

        return h;
    }
}
