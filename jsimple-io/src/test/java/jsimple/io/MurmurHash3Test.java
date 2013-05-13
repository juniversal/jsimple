package jsimple.io;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 5/12/13 12:07 AM
 */
public class MurmurHash3Test extends UnitTest {
    @Test public void testHssh() {
        byte[] key = new byte[500000];

        validateHashOfKeyLength(key, 0, 0x0L);
        validateHashOfKeyLength(key, 1, 0x54d201b988c4adecL);
        validateHashOfKeyLength(key, 2, 0x322a42b146aa39efL);
        validateHashOfKeyLength(key, 3, 0xff8d4773803e4c88L);
        validateHashOfKeyLength(key, 4, 0xfe8fc11ceb05649fL);
        validateHashOfKeyLength(key, 5, 0x84db37f726280240L);
        validateHashOfKeyLength(key, 6, 0xfcfefa8b9e15591cL);
        validateHashOfKeyLength(key, 7, 0x987e19702af4fda1L);
        validateHashOfKeyLength(key, 8, 0x946783c85e1f2504L);
        validateHashOfKeyLength(key, 9, 0x1f90e544bed09ef0L);
        validateHashOfKeyLength(key, 10, 0xf92d8f44f7643d6eL);
        validateHashOfKeyLength(key, 11, 0x4b8a3bd0147506bcL);
        validateHashOfKeyLength(key, 12, 0xb0ccd3b48fc43ad4L);
        validateHashOfKeyLength(key, 13, 0xe9f1a6aa96fa6c9bL);
        validateHashOfKeyLength(key, 14, 0x69dc6bc5a704946cL);
        validateHashOfKeyLength(key, 15, 0x164eb139b2986022L);
        validateHashOfKeyLength(key, 16, 0x30f4fe8ab88148c5L);
        validateHashOfKeyLength(key, 17, 0xba4fd76cffeed0d7L);
        validateHashOfKeyLength(key, 18, 0xa329e2bc7c1f6307L);
        validateHashOfKeyLength(key, 19, 0xa39b1b775ad46afL);
        validateHashOfKeyLength(key, 20, 0x43a25b8b8a31e38L);
        validateHashOfKeyLength(key, 21, 0xee1bbf3debb59d01L);
        validateHashOfKeyLength(key, 22, 0x2848e0eb8009234fL);
        validateHashOfKeyLength(key, 23, 0x1c9bce302a8e8252L);
        validateHashOfKeyLength(key, 24, 0x154b97a514e9fcacL);
        validateHashOfKeyLength(key, 25, 0x5613a267d141d469L);
        validateHashOfKeyLength(key, 26, 0xda06434ff5f6871fL);
        validateHashOfKeyLength(key, 27, 0xd7de8efc63634694L);
        validateHashOfKeyLength(key, 28, 0x99bcdc172e265fabL);
        validateHashOfKeyLength(key, 29, 0xf58a3364c5577d39L);
        validateHashOfKeyLength(key, 30, 0xf771fde79427e1b2L);
        validateHashOfKeyLength(key, 31, 0x5993f144aa85698eL);
        validateHashOfKeyLength(key, 32, 0x29bbd4a91a3dcb8L);
        validateHashOfKeyLength(key, 33, 0xb4bd0fff22cd9c3L);
        validateHashOfKeyLength(key, 34, 0x216c7ca819330abaL);
        validateHashOfKeyLength(key, 35, 0xe36fc8e7bd744056L);
        validateHashOfKeyLength(key, 36, 0x6ad6c3d78f4538e8L);
        validateHashOfKeyLength(key, 37, 0x46805080f6ccea18L);
        validateHashOfKeyLength(key, 38, 0x6a24dbe913756858L);
        validateHashOfKeyLength(key, 39, 0xede08b8192123ecbL);
        validateHashOfKeyLength(key, 40, 0xb6ef51e8548b450cL);
        validateHashOfKeyLength(key, 41, 0x6ee406cf0679b0ccL);
        validateHashOfKeyLength(key, 42, 0xddd4a5fc94298b04L);
        validateHashOfKeyLength(key, 43, 0xcb225c9f987d6518L);
        validateHashOfKeyLength(key, 44, 0x9c1152479ab397e6L);
        validateHashOfKeyLength(key, 45, 0x7c77f32398021497L);
        validateHashOfKeyLength(key, 46, 0x1c281225a898e694L);
        validateHashOfKeyLength(key, 47, 0xf87af8c2ba36dd39L);
        validateHashOfKeyLength(key, 48, 0x965d97e2226ac68L);
        validateHashOfKeyLength(key, 49, 0xd203ab4124f665e3L);
        validateHashOfKeyLength(key, 50, 0x24869fc93185f47bL);
        validateHashOfKeyLength(key, 51, 0x79a046f63d6806b3L);
        validateHashOfKeyLength(key, 52, 0x13022fe0034cb5b8L);
        validateHashOfKeyLength(key, 53, 0x16be4d397cf02e1cL);
        validateHashOfKeyLength(key, 54, 0x7baa9c0ce7d6380eL);
        validateHashOfKeyLength(key, 55, 0xafc704012a93fc3cL);
        validateHashOfKeyLength(key, 56, 0x772c48ae0135f611L);
        validateHashOfKeyLength(key, 57, 0x2f71515939879947L);
        validateHashOfKeyLength(key, 58, 0xeec0d61a98756b84L);
        validateHashOfKeyLength(key, 59, 0xf1c5e6be0a959ce2L);
        validateHashOfKeyLength(key, 60, 0x830e4b28b72edfcL);
        validateHashOfKeyLength(key, 61, 0x4bc23b462f3d4e18L);
        validateHashOfKeyLength(key, 62, 0x64a49d99e78a6ae2L);
        validateHashOfKeyLength(key, 63, 0x346a043b68acb5b4L);
        validateHashOfKeyLength(key, 64, 0xb2bf8ed2d7c7ae64L);
        validateHashOfKeyLength(key, 65, 0xc5e1137828a25953L);
        validateHashOfKeyLength(key, 66, 0xd413e7cfb41e38f2L);
        validateHashOfKeyLength(key, 67, 0xcd70d30fd165bc14L);
        validateHashOfKeyLength(key, 68, 0x324918ccc50857f4L);
        validateHashOfKeyLength(key, 69, 0x353bb0b53352da82L);
        validateHashOfKeyLength(key, 70, 0x928b60cebe08b87L);
        validateHashOfKeyLength(key, 71, 0x416f2bf6efbacb41L);
        validateHashOfKeyLength(key, 72, 0x1a6715b95487eff3L);
        validateHashOfKeyLength(key, 73, 0xb5b4546d4298f7cdL);
        validateHashOfKeyLength(key, 74, 0xf18e18161fecde0eL);
        validateHashOfKeyLength(key, 75, 0x83329a95984ee25bL);
        validateHashOfKeyLength(key, 76, 0xfdb06604080126aaL);

        validateHashOfKeyLength(key, 4090, 0xd61b714096d93c84L);
        validateHashOfKeyLength(key, 4091, 0x8ad9eedaa11bba8cL);
        validateHashOfKeyLength(key, 4092, 0xc28e93692931c1b6L);
        validateHashOfKeyLength(key, 4093, 0x2bc6f600222f0e7eL);
        validateHashOfKeyLength(key, 4094, 0x2d254e4dc1a542acL);
        validateHashOfKeyLength(key, 4095, 0x6c9cf1c094a483daL);
        validateHashOfKeyLength(key, 4096, 0xb3c621064774a0cdL);
        validateHashOfKeyLength(key, 4097, 0x393f1126a2d2a52eL);
        validateHashOfKeyLength(key, 4098, 0x67dde65c9e3ea93fL);
        validateHashOfKeyLength(key, 4099, 0xbb6022981acbca1eL);
        validateHashOfKeyLength(key, 4100, 0x9397696c9718ed0L);
        validateHashOfKeyLength(key, 4101, 0x7d04031dc45f50a2L);
        validateHashOfKeyLength(key, 4102, 0xfd8fcb2686538453L);

        validateHashOfKeyLengthWithSeed(key, 4090, 0x5712f636951d732dL);
        validateHashOfKeyLengthWithSeed(key, 4091, 0x68210611e6550627L);
        validateHashOfKeyLengthWithSeed(key, 4092, 0xc20b3bb02c21b979L);
        validateHashOfKeyLengthWithSeed(key, 4093, 0x1044592f9b5c0383L);
        validateHashOfKeyLengthWithSeed(key, 4094, 0x7690781a409ccaffL);
        validateHashOfKeyLengthWithSeed(key, 4095, 0x37da2837b8d41f1cL);
        validateHashOfKeyLengthWithSeed(key, 4096, 0x9158d9618a8e1b03L);
        validateHashOfKeyLengthWithSeed(key, 4097, 0xa34e2131068fc7adL);
        validateHashOfKeyLengthWithSeed(key, 4098, 0xc6bc5ac1f2443d21L);
        validateHashOfKeyLengthWithSeed(key, 4099, 0x8da2fb4054a41dd3L);
        validateHashOfKeyLengthWithSeed(key, 4100, 0xc4f63e5c906d12daL);
        validateHashOfKeyLengthWithSeed(key, 4101, 0x9f6d6ea923d37eb1L);
        validateHashOfKeyLengthWithSeed(key, 4102, 0x3862e2f85708efa9L);

        validateHashOfKeyLength(key, 151550, 0x47fd9691b08529dbL);
        validateHashOfKeyLength(key, 151551, 0x9ab6c561dc200ed5L);
        validateHashOfKeyLength(key, 151552, 0xbb2d5e62ceb2c886L);
        validateHashOfKeyLength(key, 151553, 0x6f6b70fe21cb32b0L);
        validateHashOfKeyLength(key, 151554, 0x8b559eaa6b4e58cdL);
        validateHashOfKeyLength(key, 151555, 0x6616315df32162c3L);
        validateHashOfKeyLength(key, 151556, 0xae0074163ce5cdb6L);
        validateHashOfKeyLength(key, 151557, 0x1b57c15e64f28802L);
        validateHashOfKeyLength(key, 151558, 0x2c75e3eb71f6eab7L);
        validateHashOfKeyLength(key, 151559, 0xe78b4965786df2a3L);
        validateHashOfKeyLength(key, 151560, 0xe8c3044bdfb7f3c6L);
        validateHashOfKeyLength(key, 151561, 0x403777270c98cc1dL);
        validateHashOfKeyLength(key, 151562, 0xe4c01aeb373ce5d5L);
        validateHashOfKeyLength(key, 151563, 0x845a4da9610067d4L);
        validateHashOfKeyLength(key, 151564, 0x670723f710c242dbL);
        validateHashOfKeyLength(key, 151565, 0xc4fde4ad3efd5d8fL);
        validateHashOfKeyLength(key, 151566, 0x260ecc65d480d690L);
        validateHashOfKeyLength(key, 151567, 0x5591c9673da1dcddL);
        validateHashOfKeyLength(key, 151568, 0x35c0dcf6430fe2efL);
        validateHashOfKeyLength(key, 151569, 0x9abec808ce0a9be7L);
        validateHashOfKeyLength(key, 151570, 0xef1473ff61c6d6c8L);
        validateHashOfKeyLength(key, 151571, 0xfe4eb5cfc8c8b029L);
        validateHashOfKeyLength(key, 151572, 0xe8677de47a8d8cacL);
        validateHashOfKeyLength(key, 151573, 0xf6101f04c3cbcde2L);
        validateHashOfKeyLength(key, 151574, 0x8056f6b5dcbed321L);
        validateHashOfKeyLength(key, 151575, 0x2e066809d705c659L);
        validateHashOfKeyLength(key, 151576, 0x56e39534f54d7aa7L);
        validateHashOfKeyLength(key, 151577, 0x478003681237e050L);
        validateHashOfKeyLength(key, 151578, 0xd98b04ddc48c1f22L);
        validateHashOfKeyLength(key, 151579, 0x46d4b91f76395026L);
        validateHashOfKeyLength(key, 151580, 0x533494edca2c161cL);
        validateHashOfKeyLength(key, 151581, 0xafb863c1cae88eccL);
        validateHashOfKeyLength(key, 151582, 0x50bee14f363dabebL);
        validateHashOfKeyLength(key, 151583, 0x991d0b11e8564ad0L);
        validateHashOfKeyLength(key, 151584, 0x5d297b4379aa7212L);
        validateHashOfKeyLength(key, 151585, 0xc085b992b2673fc8L);
        validateHashOfKeyLength(key, 151586, 0x2781ab112a3aba0dL);
        validateHashOfKeyLength(key, 151587, 0x9d0d415a84e0dc83L);
        validateHashOfKeyLength(key, 151588, 0x6f349794ae4366feL);
        validateHashOfKeyLength(key, 151589, 0x7d1629ee1c4319f8L);
        validateHashOfKeyLength(key, 151590, 0x5fea0956eddf5ce9L);
        validateHashOfKeyLength(key, 151591, 0x9e6d36ea0c9c1abcL);
        validateHashOfKeyLength(key, 151592, 0x3650de9cd26636f5L);
        validateHashOfKeyLength(key, 151593, 0xebb6983b53111496L);
        validateHashOfKeyLength(key, 151594, 0xab73f5ed311ea1b8L);
        validateHashOfKeyLength(key, 151595, 0xe8173a0751bbe37L);
        validateHashOfKeyLength(key, 151596, 0xd17bf4635a617eb2L);
        validateHashOfKeyLength(key, 151597, 0x8a91902cbdb1fb09L);
        validateHashOfKeyLength(key, 151598, 0xb57f59a95f47e3c6L);
        validateHashOfKeyLength(key, 151599, 0xd8502c82297eaff8L);

        validate128BitHashOfKeyLength(key, 151550, 0x47fd9691b08529dbL, 0xef3a5af37c454794L);
        validate128BitHashOfKeyLength(key, 151551, 0x9ab6c561dc200ed5L, 0xb4f13722b0628a8aL);
        validate128BitHashOfKeyLength(key, 151552, 0xbb2d5e62ceb2c886L, 0x82b57a74eeac9437L);
        validate128BitHashOfKeyLength(key, 151553, 0x6f6b70fe21cb32b0L, 0x415977705700d40L);
        validate128BitHashOfKeyLength(key, 151554, 0x8b559eaa6b4e58cdL, 0x5db3e6679d4f45afL);
        validate128BitHashOfKeyLength(key, 151555, 0x6616315df32162c3L, 0x906563da7643969eL);
        validate128BitHashOfKeyLength(key, 151556, 0xae0074163ce5cdb6L, 0xb31afa5f42f5c3efL);
        validate128BitHashOfKeyLength(key, 151557, 0x1b57c15e64f28802L, 0x78e2b2a46c942ccfL);
        validate128BitHashOfKeyLength(key, 151558, 0x2c75e3eb71f6eab7L, 0x177983d537bcea67L);
        validate128BitHashOfKeyLength(key, 151559, 0xe78b4965786df2a3L, 0x98309f106e6a4e3fL);
        validate128BitHashOfKeyLength(key, 151560, 0xe8c3044bdfb7f3c6L, 0xc57e1a75132397bL);
        validate128BitHashOfKeyLength(key, 151561, 0x403777270c98cc1dL, 0x955727ef12085c2L);
        validate128BitHashOfKeyLength(key, 151562, 0xe4c01aeb373ce5d5L, 0x816d0c9d0896e122L);
        validate128BitHashOfKeyLength(key, 151563, 0x845a4da9610067d4L, 0x7dc136b9ca20262dL);
        validate128BitHashOfKeyLength(key, 151564, 0x670723f710c242dbL, 0x52a24bab6e2bd34L);
        validate128BitHashOfKeyLength(key, 151565, 0xc4fde4ad3efd5d8fL, 0x398215b4c7e76347L);
        validate128BitHashOfKeyLength(key, 151566, 0x260ecc65d480d690L, 0xea140c43488a7714L);
        validate128BitHashOfKeyLength(key, 151567, 0x5591c9673da1dcddL, 0xf1d0eaafc4120bcdL);
        validate128BitHashOfKeyLength(key, 151568, 0x35c0dcf6430fe2efL, 0x6697cfb98681bcb5L);
        validate128BitHashOfKeyLength(key, 151569, 0x9abec808ce0a9be7L, 0xc7d3b47fb464eae3L);
        validate128BitHashOfKeyLength(key, 151570, 0xef1473ff61c6d6c8L, 0x1daa92f42e962ef9L);
        validate128BitHashOfKeyLength(key, 151571, 0xfe4eb5cfc8c8b029L, 0x75ce0daaede438aaL);
        validate128BitHashOfKeyLength(key, 151572, 0xe8677de47a8d8cacL, 0x60a3a7fa140bc880L);
        validate128BitHashOfKeyLength(key, 151573, 0xf6101f04c3cbcde2L, 0x3e6b3f4924285c9aL);
        validate128BitHashOfKeyLength(key, 151574, 0x8056f6b5dcbed321L, 0xaa02de346d48d999L);
        validate128BitHashOfKeyLength(key, 151575, 0x2e066809d705c659L, 0x245578a2ec564963L);
        validate128BitHashOfKeyLength(key, 151576, 0x56e39534f54d7aa7L, 0xc97a6f278d3e8b7fL);
        validate128BitHashOfKeyLength(key, 151577, 0x478003681237e050L, 0x43477133b25633faL);
        validate128BitHashOfKeyLength(key, 151578, 0xd98b04ddc48c1f22L, 0x908f14eee2a2a4e1L);
        validate128BitHashOfKeyLength(key, 151579, 0x46d4b91f76395026L, 0x473e2ef4c5b1d683L);
        validate128BitHashOfKeyLength(key, 151580, 0x533494edca2c161cL, 0xc9c5bb1a579bcbecL);
        validate128BitHashOfKeyLength(key, 151581, 0xafb863c1cae88eccL, 0x3fe79fd47e1c60f7L);
        validate128BitHashOfKeyLength(key, 151582, 0x50bee14f363dabebL, 0xc40da5a4d5047f04L);
        validate128BitHashOfKeyLength(key, 151583, 0x991d0b11e8564ad0L, 0x4354eaaaa9a5960dL);
        validate128BitHashOfKeyLength(key, 151584, 0x5d297b4379aa7212L, 0xaaf640b9b29fa56aL);
        validate128BitHashOfKeyLength(key, 151585, 0xc085b992b2673fc8L, 0xe23f9547cb608e5aL);
        validate128BitHashOfKeyLength(key, 151586, 0x2781ab112a3aba0dL, 0x89246d514c752f51L);
        validate128BitHashOfKeyLength(key, 151587, 0x9d0d415a84e0dc83L, 0xed52e055e1ba8c0cL);
        validate128BitHashOfKeyLength(key, 151588, 0x6f349794ae4366feL, 0x46eceb40de0d5b70L);
        validate128BitHashOfKeyLength(key, 151589, 0x7d1629ee1c4319f8L, 0x8d910a6af71c3662L);
        validate128BitHashOfKeyLength(key, 151590, 0x5fea0956eddf5ce9L, 0x8a40ba24bfcf841bL);
        validate128BitHashOfKeyLength(key, 151591, 0x9e6d36ea0c9c1abcL, 0x457f679d0d942265L);
        validate128BitHashOfKeyLength(key, 151592, 0x3650de9cd26636f5L, 0x3ab52232221e7742L);
        validate128BitHashOfKeyLength(key, 151593, 0xebb6983b53111496L, 0xc9a3cd9301d8c948L);
        validate128BitHashOfKeyLength(key, 151594, 0xab73f5ed311ea1b8L, 0x4a8fa589a23ae399L);
        validate128BitHashOfKeyLength(key, 151595, 0xe8173a0751bbe37L, 0xaf22cc9ef960a2cL);
        validate128BitHashOfKeyLength(key, 151596, 0xd17bf4635a617eb2L, 0x3aa65aacfbb3f24eL);
        validate128BitHashOfKeyLength(key, 151597, 0x8a91902cbdb1fb09L, 0xa2bf86d8ef74dc00L);
        validate128BitHashOfKeyLength(key, 151598, 0xb57f59a95f47e3c6L, 0x42f3edeef4eee913L);
        validate128BitHashOfKeyLength(key, 151599, 0xd8502c82297eaff8L, 0xd32cf643bb6db793L);
    }

    void validateHashOfKeyLengthWithSeed(byte[] keyBuffer, int length, long expectedHash) {
        for (int i = 0; i < length; i++)
            keyBuffer[i] = (byte) (i * 3);

        MurmurHash3 murmurHash3 = new MurmurHash3(length);
        murmurHash3.addStream(new ByteArrayInputStream(keyBuffer, 0, length));
        assertEquals(expectedHash, murmurHash3.getHash64());
    }


    void validateHashOfKeyLength(byte[] keyBuffer, int length, long expectedHash) {
        for (int i = 0; i < length; i++)
            keyBuffer[i] = (byte) (i * 3);

        MurmurHash3 murmurHash3;

        murmurHash3 = new MurmurHash3();
        murmurHash3.addStream(new ByteArrayInputStream(keyBuffer, 0, length));
        assertEquals(expectedHash, murmurHash3.getHash64());

        murmurHash3 = new MurmurHash3();
        murmurHash3.addBytes(keyBuffer, 0, length);
        assertEquals(expectedHash, murmurHash3.getHash64());

        if (length % 2 == 0 || (length / 4) + 1 == length) {
            murmurHash3 = new MurmurHash3();

            StringBuilder stringBuilder = new StringBuilder();
            int evenLength = (length / 2) * 2;
            for (int i = 0; i < evenLength; ) {
                char c = (char) ( (keyBuffer[i + 1] << 8) | (keyBuffer[i] & 0xFF) );
                stringBuilder.append(c);
                i += 2;
            }

            murmurHash3.addString(stringBuilder.toString());
            if (length > evenLength)
                murmurHash3.addByte(keyBuffer[length - 1]);
        }

        assertEquals(expectedHash, murmurHash3.getHash64());
    }

    void validate128BitHashOfKeyLength(byte[] keyBuffer, int length, long expectedHash1, long expectedHash2) {
        for (int i = 0; i < length; i++)
            keyBuffer[i] = (byte) (i * 3);

        MurmurHash3 murmurHash3 = new MurmurHash3();
        murmurHash3.addStream(new ByteArrayInputStream(keyBuffer, 0, length));

        byte[] hash = murmurHash3.getHash128();

        assertEquals(expectedHash1, getblock64(hash, 0));
        assertEquals(expectedHash2, getblock64(hash, 8));
    }

    private long getblock64(byte[] buffer, int offset) {
        return ((long) buffer[offset + 0] & 0xff) |
                (((long) buffer[offset + 1] & 0xff) << 8) |
                (((long) buffer[offset + 2] & 0xff) << 16) |
                (((long) buffer[offset + 3] & 0xff) << 24) |
                (((long) buffer[offset + 4] & 0xff) << 32) |
                (((long) buffer[offset + 5] & 0xff) << 40) |
                (((long) buffer[offset + 6] & 0xff) << 48) |
                (((long) buffer[offset + 7] & 0xff) << 56);
    }
}
