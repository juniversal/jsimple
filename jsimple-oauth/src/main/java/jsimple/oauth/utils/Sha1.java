package jsimple.oauth.utils;

import jsimple.util.PlatformUtils;
import jsimple.util.StringUtils;

/**
 * @author Sent from Nurmi Raimo.T (Nokia-MP/Espoo) <raimo.t.nurmi@nokia.com> & Lindholm Samu (Nokia-MP/Espoo)
 *         <Samu.Lindholm@nokia.com>.  They said "this is heavily used and tested [on S40 I assume].  I'm not sure where
 *         the code originally came from before that.  It's original package name was "mango.client.util".
 * @since 10/26/12 2:15 AM
 */
public class Sha1 {
    public final static int SIZE = 20;

    private int[] m_state = new int[]{0x67452301, -271733879, -1732584194, 0x10325476, -1009589776};
    private long m_lCount = 0;
    private byte[] m_digestBits = new byte[SIZE];
    private int[] m_block = new int[16];
    private int m_nBlockIndex = 0;

    public Sha1() {
    }

    private static final int rol(final int nValue, final int nBits) {
        return ((nValue << nBits) | (nValue >>> (32 - nBits)));
    }

    private final int blk0(final int nI) {
        final int[] b = m_block;
        return b[nI] =
                ((rol(b[nI], 24) & -16711936) |           // Need to express as negative decimal, not hex, so C# treats as int, not long
                        (rol(b[nI], 8) & 0x00ff00ff));
    }

    private final int blk(final int nI) {
        final int[] b = m_block;
        return (b[nI & 15] =
                rol(
                        b[(nI + 13) & 15] ^
                                b[(nI + 8) & 15] ^
                                b[(nI + 2) & 15] ^
                                b[nI & 15],
                        1));
    }

    private void transform() {
        final int[] data = new int[5];
        data[0] = m_state[0];
        data[1] = m_state[1];
        data[2] = m_state[2];
        data[3] = m_state[3];
        data[4] = m_state[4];

        int nV = 1;
        int nW = 2;
        int nX = 3;
        int nY = 4;
        int nZ = 0;
        int phase = 0;
        for (int nI = 0; nI < 80; nI++) {
            //argument rotation
            int tmp = nZ;
            nZ = nY;
            nY = nX;
            nX = nW;
            nW = nV;
            nV = tmp;

            //Inlined r0,r1,r2,r3,r4 boxes
            switch (phase) {
                //0..15
                case 0: {
                    data[nZ] += ((data[nW] & (data[nX] ^ data[nY])) ^ data[nY])
                            + blk0(nI)
                            + 0x5a827999
                            + rol(data[nV], 5);
                    data[nW] = rol(data[nW], 30);
                    if (nI == 15) {
                        phase++;
                    }
                }
                continue;

                //16..19
                case 1: {
                    data[nZ] += ((data[nW] & (data[nX] ^ data[nY])) ^ data[nY])
                            + blk(nI)
                            + 0x5a827999
                            + rol(data[nV], 5);
                    data[nW] = rol(data[nW], 30);
                    if (nI == 19) {
                        phase++;
                    }
                }
                continue;

                //20..39
                case 2: {
                    data[nZ] += (data[nW] ^ data[nX] ^ data[nY])
                            + blk(nI)
                            + 0x6eD9eba1
                            + rol(data[nV], 5);
                    data[nW] = rol(data[nW], 30);
                    if (nI == 39) {
                        phase++;
                    }
                }
                continue;

                //39..59
                case 3: {
                    data[nZ]
                            += (((data[nW] | data[nX]) & data[nY]) | (data[nW] & data[nX]))
                            + blk(nI)
                            + -1894007588        // Need to express as negative decimal, not hex, so C# treats as int, not long
                            + rol(data[nV], 5);
                    data[nW] = rol(data[nW], 30);
                    if (nI == 59) {
                        phase++;
                    }
                }
                continue;

                //59..79
                case 4: {
                    data[nZ] += (data[nW] ^ data[nX] ^ data[nY])
                            + blk(nI)
                            + -899497514        // Need to express as negative decimal, not hex, so C# treats as int, not long
                            + rol(data[nV], 5);
                    data[nW] = rol(data[nW], 30);
                }
                continue;
            }
        }

        m_state[0] += data[0];
        m_state[1] += data[1];
        m_state[2] += data[2];
        m_state[3] += data[3];
        m_state[4] += data[4];
    }

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

    public void update(byte[] data) {
        update(data, 0, data.length);
    }

    public void update(byte[] data, int nOfs, int nLen) {
        for (int nEnd = nOfs + nLen; nOfs < nEnd; nOfs++) {
            update(data[nOfs]);
        }
    }

    public void doFinal(byte[] data) {
        update(data, 0, data.length);
        finish();
    }


    /**
     * Finalizes the digest.
     */
    private void finish() {
        int nI;
        byte bits[] = new byte[8];

        for (nI = 0; nI < 8; nI++) {
            bits[nI] = (byte) ((m_lCount >>> (((7 - nI) << 3))) & 0xff);
        }

        byte neg128 = -128;
        update(neg128);            // Was 128 in original code but -128 for signed byte 0x80 is more correct
        while (m_nBlockIndex != 56) {
            update((byte) 0);
        }

        for (nI = 0; nI < bits.length; nI++) {
            update(bits[nI]);
        }

        for (nI = 0; nI < 20; nI++) {
            m_digestBits[nI] =
                    (byte) ((m_state[nI >> 2] >> ((3 - (nI & 3)) << 3)) & 0xff);
        }
    }


    /**
     * Retrieves the digest.
     *
     * @return the digst bytes as an array if DIGEST_SIZE bytes
     */
    public byte[] getDigest() {
        byte[] result = new byte[SIZE];
        PlatformUtils.copyBytes(m_digestBits, 0, result, 0, SIZE);
        return result;
    }

    /*
    * makes a binhex string representation of the current digest
    * @return the string representation
    */
    @Override public String toString() {
        return StringUtils.toHexStringFromBytes(m_digestBits);
    }

    public static byte[] mac(final byte[] key, final byte[] text) {
        final byte[] pkey = new byte[64];
        final byte[] buf = new byte[64];
        if (key != null && key.length > 0) {
            if (key.length <= 64) {
                PlatformUtils.copyBytes(key, 0, pkey, 0, key.length);
            } else {
                final Sha1 shakey = new Sha1();
                shakey.doFinal(key);
                final byte[] keya = shakey.getDigest();
                PlatformUtils.copyBytes(keya, 0, pkey, 0, keya.length);
            }
        }

        for (int i = 0; i < 64; i++) {
            buf[i] = (byte) ((0x36 ^ pkey[i]) & 0xff);
        }

        final Sha1 sha1 = new Sha1();
        sha1.update(buf);
        sha1.doFinal(text);
        final byte[] h1 = sha1.getDigest();

        for (int i = 0; i < 64; i++) {
            buf[i] = (byte) ((0x5C ^ pkey[i]) & 0xff);
        }

        final Sha1 sha2 = new Sha1();
        sha2.update(buf);
        sha2.doFinal(h1);
        return sha2.getDigest();
    }
}
