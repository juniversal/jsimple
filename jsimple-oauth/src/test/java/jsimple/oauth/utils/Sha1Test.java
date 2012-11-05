package jsimple.oauth.utils;

import jsimple.io.IOUtils;
import jsimple.util.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Bret Johnson
 * @since 10/23/12 12:04 AM
 */
public class Sha1Test {

    // These test cases are from http://tools.ietf.org/html/rfc2202
    @Test public void testMac() {
        testMac("0x0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b", "Hi There", "0xb617318655057264e28bc0b6fb378c8ef146be00");

        testMac("Jefe", "what do ya want for nothing?", "0xeffcdf6ae5eb2fa2d27416d5f184df9c259a7c79");

        testMac("0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "0xdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd",
                "0x125d7342b9ac11cd91a39af48aa17b4f63f175d3");

        testMac("0x0102030405060708090a0b0c0d0e0f10111213141516171819",
                "0xcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd",
                "0x4c9007f4026250c6bc8414f9bf50c86c2d7235da");

        testMac("0x0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c", "Test With Truncation", "0x4c1a03424b55e07fe7f27be1d58bb9324a9a5a04");

        testMac("0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "Test Using Larger Than Block-Size Key - Hash Key First",
                "0xaa4ae5e15272d00e95705637ce8a3b55ed402112");

        testMac("0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "Test Using Larger Than Block-Size Key and Larger Than One Block-Size Data",
                "0xe8e99d0f45237d786d6bbaa7965c7808bbff1a91");

        testMac("0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "Test Using Larger Than Block-Size Key - Hash Key First",
                "0xaa4ae5e15272d00e95705637ce8a3b55ed402112");

        testMac("0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "Test Using Larger Than Block-Size Key and Larger Than One Block-Size Data",
                "0xe8e99d0f45237d786d6bbaa7965c7808bbff1a91");
    }

    private void testMac(String key, String data, String expectedDigest) {
        byte[] digest = Sha1.mac(toBytesFromString(key), toBytesFromString(data));
        assertArrayEquals(toBytesFromString(expectedDigest), digest);
    }

    private byte[] toBytesFromString(String string) {
        if (string.startsWith("0x"))
            return StringUtils.toBytesFromHexString(string.substring(2));
        else return IOUtils.toLatin1BytesFromString(string);
    }
}
