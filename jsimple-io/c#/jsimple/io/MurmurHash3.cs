using System;

namespace jsimple.io {

    /// <summary>
    /// MurmurHash3 is a modern, fast, well regarded non-cryptographic hashing algorithm.  Non-cryptographic means that while
    /// hashes are very well distributed for arbitrary data, if an attacker tries to attack the algorithm they might be able
    /// to find some sort of pattern.  Non-cryptographic hashes trade security for speed; cryptographic hashes, like SHA-256,
    /// are slower.
    /// <p/>
    /// There are multiple variants of MurmurHash3.  This one is x86_128 (aka Murmur 3C), little endian.
    /// <p/>
    /// I selected it when looking for hash/checksum algorithm meeting these criteria: (1) Can generate 64 bit hashes.  32
    /// bits wasn't enough to ensure the odds of collision are virtually 0. (2) Is fast, as fast as possible. (3) Is fast
    /// even on 32 bit architectures, as most mobile devices today have 32 bit processors.
    /// <p/>
    /// The Murmur3 x86_128 algorithm met those criteria well.  32 bits output wasn't enough, so we use the 128 bit variant
    /// and throw the top 64 bits away (though with the API below, you can get all 128 bits if you want). The x86 variant
    /// uses all 32 bit arithmetic, so it runs quickly on 32 & 64 bit processors.  x64 Murmur is somewhat faster on 64 bit
    /// machines, but much slower on 32 bit, so it wasn't a good choice.  Other modern hash algorithms generating > 32 bits
    /// output (e.g. SpookyHash and CityHash) use 64 arithmetic, with no 32 bit variant available, so they weren't good
    /// choices.
    /// <p/>
    /// This Java code was implemented from the C reference implementation, here:
    /// <p/>
    /// http://code.google.com/p/smhasher/source/browse/trunk/MurmurHash3.cpp?r=150
    /// 
    /// @author Austin Appleby (MurmurHash3 algorithm) and C reference code
    /// @author Bret Johnson (Java port)
    /// @since 5/10/13 9:51 PM
    /// </summary>
    public class MurmurHash3 {
        private int h1; // uint32_t
        private int h2; // uint32_t
        private int h3; // uint32_t
        private int h4; // uint32_t

        internal readonly int c1 = 0x239b961b; // const uint32_t
        internal readonly int c2 = unchecked((int)0xab0e9789); // const uint32_t
        internal readonly int c3 = 0x38b34ae5; // const uint32_t
        internal readonly int c4 = unchecked((int)0xa1e38b93); // const uint32_t

        public const int BUFFER_SIZE = 16 * 4; // 16 16-byte blocks (256 bytes)
        internal int[] buffer = new int[BUFFER_SIZE];
        internal int bufferOffset = 0;
        internal int totalLengthInBytes = 0;
        internal bool finished = false;

        public MurmurHash3(int seed) {
            h1 = seed;
            h2 = seed;
            h3 = seed;
            h4 = seed;
        }

        public MurmurHash3() : this(0) {
        }

        public virtual void addByte(sbyte b) {
            emptyBufferIfFull();
            buffer[bufferOffset++] = b;
            totalLengthInBytes += 1;
        }

        public virtual void addBoolean(bool b) {
            addByte(b ? (sbyte) 1 : (sbyte) 0);
        }

        public virtual void addChar(char c) {
            emptyBufferIfFull();
            buffer[bufferOffset++] = c;
            totalLengthInBytes += 2;
        }

        public virtual void addInt(int i) {
            emptyBufferIfFull();
            buffer[bufferOffset++] = i;
            totalLengthInBytes += 4;
        }

        public virtual void addLong(long l) {
            addInt((int) l);
            addInt((int)(l >> 32));
        }

        public virtual void addString(string s) {
            int length = s.Length;
            int evenLength = (length / 2) * 2;

            for (int i = 0; i < evenLength - 1;) {
                addInt(s[i + 1] << 16 | (s[i] & 0xFFFF));
                i += 2;
            }

            // If there's an odd number of characters, add the last one zero padded, filling up a full int
            if (length > evenLength)
                addChar(s[length - 1]);
        }

        public virtual void addBytes(sbyte[] data, int offset, int length) {
            int bytesToAdd = length - offset;
            int fullIntsToAdd = bytesToAdd / 4;

            while (fullIntsToAdd > 0) {
                emptyBufferIfFull();

                int intsToAddThisPass = Math.Min(fullIntsToAdd, BUFFER_SIZE - bufferOffset);

                for (int i = 0; i < intsToAddThisPass; ++i) {
                    buffer[bufferOffset++] = (data[offset + 0] & 0xff) | ((data[offset + 1] & 0xff) << 8) | ((data[offset + 2] & 0xff) << 16) | ((data[offset + 3] & 0xff) << 24);
                    offset += 4;
                }

                fullIntsToAdd -= intsToAddThisPass;
            }

            // If 1, 2, or 3 bytes are left over, add one more int that includes them (zero padded)
            if (bytesToAdd % 4 != 0) {
                sbyte b1 = 0;
                sbyte b2 = 0;
                sbyte b3 = 0;

                switch (bytesToAdd % 4) {
                    case 3:
                        b3 = data[offset + 2];
                        goto case 2;
                    case 2:
                        b2 = data[offset + 1];
                        goto case 1;
                    case 1:
                        b1 = data[offset + 0];
                    break;
                }

                emptyBufferIfFull();
                buffer[bufferOffset++] = (b1 & 0xff) | ((b2 & 0xff) << 8) | ((b3 & 0xff) << 16);
            }

            totalLengthInBytes += bytesToAdd;
        }

        public virtual void addStream(InputStream data) {
            sbyte[] byteBuffer = new sbyte[4096]; // Have a 4K read buffer for stream input

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

                int k1; // uint32_t
                int k2; // uint32_t
                int k3; // uint32_t
                int k4; // uint32_t

                switch (intsInLastPartialBlock) {
                    case 4:
                        k4 = buffer[tailBlockOffset + 3];
                        k4 *= c4;
                        k4 = ((k4 << 18) | ((int)((uint)k4 >> (-18))));
                        k4 *= c1;
                        h4 ^= k4;

                        goto case 3;
                    case 3:
                        k3 = buffer[tailBlockOffset + 2];
                        k3 *= c3;
                        k3 = ((k3 << 17) | ((int)((uint)k3 >> (-17))));
                        k3 *= c4;
                        h3 ^= k3;

                        goto case 2;
                    case 2:
                        k2 = buffer[tailBlockOffset + 1];
                        k2 *= c2;
                        k2 = ((k2 << 16) | ((int)((uint)k2 >> (-16))));
                        k2 *= c3;
                        h2 ^= k2;

                        goto case 1;
                    case 1:
                        k1 = buffer[tailBlockOffset + 0];
                        k1 *= c1;
                        k1 = ((k1 << 15) | ((int)((uint)k1 >> (-15))));
                        k1 *= c2;
                        h1 ^= k1;
                    break;
                }
            }

            finalization(totalLengthInBytes);

            finished = true;
        }

        public virtual long Hash64 {
            get {
                ensureFinished();
                return (((long) h1) & 0xffffffffL) | ((((long) h2) & 0xffffffffL) << 32);
            }
        }

        public virtual sbyte[] Hash128 {
            get {
                ensureFinished();
    
                sbyte[] hash = new sbyte[16];
                putblock32(hash, 0, h1);
                putblock32(hash, 4, h2);
                putblock32(hash, 8, h3);
                putblock32(hash, 12, h4);
                return hash;
            }
        }

        private void putblock32(sbyte[] buffer, int offset, int value) {
            buffer[offset + 0] = unchecked((sbyte)((value >> 0) & 0xff));
            buffer[offset + 1] = unchecked((sbyte)((value >> 8) & 0xff));
            buffer[offset + 2] = unchecked((sbyte)((value >> 16) & 0xff));
            buffer[offset + 3] = unchecked((sbyte)((value >> 24) & 0xff));
        }

        private void body(int[] buffer, int nblocks) {
            for (int i = 0; i < nblocks; ++i) {
                int k1 = buffer[i * 4]; // uint32_t
                int k2 = buffer[i * 4 + 1]; // uint32_t
                int k3 = buffer[i * 4 + 2]; // uint32_t
                int k4 = buffer[i * 4 + 3]; // uint32_t

                k1 *= c1;
                k1 = ((k1 << 15) | ((int)((uint)k1 >> (-15))));
                k1 *= c2;
                h1 ^= k1;

                h1 = ((h1 << 19) | ((int)((uint)h1 >> -19)));
                h1 += h2;
                h1 = h1 * 5 + 0x561ccd1b;

                k2 *= c2;
                k2 = ((k2 << 16) | ((int)((uint)k2 >> -16)));
                k2 *= c3;
                h2 ^= k2;

                h2 = ((h2 << 17) | ((int)((uint)h2 >> -17)));
                h2 += h3;
                h2 = h2 * 5 + 0x0bcaa747;

                k3 *= c3;
                k3 = ((k3 << 17) | ((int)((uint)k3 >> -17)));
                k3 *= c4;
                h3 ^= k3;

                h3 = ((h3 << 15) | ((int)((uint)h3 >> -15)));
                h3 += h4;
                h3 = h3 * 5 + unchecked((int)0x96cd1c35);

                k4 *= c4;
                k4 = ((k4 << 18) | ((int)((uint)k4 >> -18)));
                k4 *= c1;
                h4 ^= k4;

                h4 = ((h4 << 13) | ((int)((uint)h4 >> -13)));
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
            h ^= (int)((uint)h >> 16);
            h *= unchecked((int)0x85ebca6b);
            h ^= (int)((uint)h >> 13);
            h *= unchecked((int)0xc2b2ae35);
            h ^= (int)((uint)h >> 16);

            return h;
        }
    }

}