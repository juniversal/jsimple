package jsimple.io;

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

    MurmurHash3(int seed) {
        h1 = seed;
        h2 = seed;
        h3 = seed;
        h4 = seed;
    }

    public MurmurHash3() {
        this(0);
    }

    public void computeMurmurHash3_x86_128(InputStream data) {
        final int BUFFER_SIZE = 4096;

        byte[] buffer = new byte[4096];
        int length = 0;

        while (true) {
            int amountRead = data.readFully(buffer);
            if (amountRead == -1)
                break;
            length += amountRead;

            int bufferOffset = body(buffer, amountRead);

            if (amountRead != BUFFER_SIZE) {
                tail(buffer, bufferOffset, length & 15);
                break;
            }
        }

        finalization(length);
    }

    public long getHash64() {
        return (((long) h1) & 0xffffffffL) | ((((long) h2) & 0xffffffffL) << 32);
    }

    public byte[] getHash128() {
        byte[] hash = new byte[16];

        putblock32(hash, 0, h1);
        putblock32(hash, 4, h2);
        putblock32(hash, 8, h3);
        putblock32(hash, 12, h4);

        return hash;
    }

    private int getblock32(byte[] buffer, int offset) {
        return (buffer[offset + 0] & 0xff) |
                ((buffer[offset + 1] & 0xff) << 8) |
                ((buffer[offset + 2] & 0xff) << 16) |
                ((buffer[offset + 3] & 0xff) << 24);
    }

    private void putblock32(byte[] buffer, int offset, int value) {
        buffer[offset + 0] = (byte) ((value >> 0) & 0xff);
        buffer[offset + 1] = (byte) ((value >> 8) & 0xff);
        buffer[offset + 2] = (byte) ((value >> 16) & 0xff);
        buffer[offset + 3] = (byte) ((value >> 24) & 0xff);
    }

    private int body(byte[] buffer, int length) {
        int bufferOffset = 0;
        while (length - bufferOffset >= 16) {
            int /* uint32_t */ k1 = getblock32(buffer, bufferOffset + 0);
            int /* uint32_t */ k2 = getblock32(buffer, bufferOffset + 4);
            int /* uint32_t */ k3 = getblock32(buffer, bufferOffset + 8);
            int /* uint32_t */ k4 = getblock32(buffer, bufferOffset + 12);

            k1 *= c1;
            k1 = ((k1 << 15) | (k1 >>> (-15)));
            k1 *= c2;
            h1 ^= k1;

            h1 = ((h1 << 19) | (h1 >>> (-19)));
            h1 += h2;
            h1 = h1 * 5 + 0x561ccd1b;

            k2 *= c2;
            k2 = ((k2 << 16) | (k2 >>> (-16)));
            k2 *= c3;
            h2 ^= k2;

            h2 = ((h2 << 17) | (h2 >>> (-17)));
            h2 += h3;
            h2 = h2 * 5 + 0x0bcaa747;

            k3 *= c3;
            k3 = ((k3 << 17) | (k3 >>> (-17)));
            k3 *= c4;
            h3 ^= k3;

            h3 = ((h3 << 15) | (h3 >>> (-15)));
            h3 += h4;
            h3 = h3 * 5 + 0x96cd1c35;

            k4 *= c4;
            k4 = ((k4 << 18) | (k4 >>> (-18)));
            k4 *= c1;
            h4 ^= k4;

            h4 = ((h4 << 13) | (h4 >>> (-13)));
            h4 += h1;
            h4 = h4 * 5 + 0x32ac3b17;

            bufferOffset += 16;
        }

        return bufferOffset;
    }

    private void tail(byte[] buffer, int bufferOffset, int amountLeft) {
        int /* uint32_t */ k1 = 0;
        int /* uint32_t */ k2 = 0;
        int /* uint32_t */ k3 = 0;
        int /* uint32_t */ k4 = 0;

        switch (amountLeft) {
            case 15:
                k4 ^= ((int) buffer[bufferOffset + 14] & 0xff) << 16;
            case 14:
                k4 ^= ((int) buffer[bufferOffset + 13] & 0xff) << 8;
            case 13:
                k4 ^= ((int) buffer[bufferOffset + 12] & 0xff) << 0;
                k4 *= c4;
                k4 = ((k4 << 18) | (k4 >>> (-18)));
                k4 *= c1;
                h4 ^= k4;

            case 12:
                k3 ^= ((int) buffer[bufferOffset + 11] & 0xff) << 24;
            case 11:
                k3 ^= ((int) buffer[bufferOffset + 10] & 0xff) << 16;
            case 10:
                k3 ^= ((int) buffer[bufferOffset + 9] & 0xff) << 8;
            case 9:
                k3 ^= ((int) buffer[bufferOffset + 8] & 0xff) << 0;
                k3 *= c3;
                k3 = ((k3 << 17) | (k3 >>> (-17)));
                k3 *= c4;
                h3 ^= k3;

            case 8:
                k2 ^= ((int) buffer[bufferOffset + 7] & 0xff) << 24;
            case 7:
                k2 ^= ((int) buffer[bufferOffset + 6] & 0xff) << 16;
            case 6:
                k2 ^= ((int) buffer[bufferOffset + 5] & 0xff) << 8;
            case 5:
                k2 ^= ((int) buffer[bufferOffset + 4] & 0xff) << 0;
                k2 *= c2;
                k2 = ((k2 << 16) | (k2 >>> (-16)));
                k2 *= c3;
                h2 ^= k2;

            case 4:
                k1 ^= ((int) buffer[bufferOffset + 3] & 0xff) << 24;
            case 3:
                k1 ^= ((int) buffer[bufferOffset + 2] & 0xff) << 16;
            case 2:
                k1 ^= ((int) buffer[bufferOffset + 1] & 0xff) << 8;
            case 1:
                k1 ^= ((int) buffer[bufferOffset + 0] & 0xff) << 0;
                k1 *= c1;
                k1 = ((k1 << 15) | (k1 >>> (-15)));
                k1 *= c2;
                h1 ^= k1;
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
