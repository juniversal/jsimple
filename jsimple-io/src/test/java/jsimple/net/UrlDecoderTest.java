package jsimple.net;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 11/25/12 11:16 PM
 */
public class UrlDecoderTest extends UnitTest {
    @Test public void testDecode() {
        assertEquals(".-_*", UrlDecoder.decode(".-_*"));   // None encoded
        assertEquals("$+!'(),", UrlDecoder.decode("%24%2B%21%27%28%29%2C"));   // All encoded
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
                UrlDecoder.decode("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));   // None encoded
        assertEquals("xëĆҸabcﮚ", UrlDecoder.decode("x%C3%AB%C4%86%D2%B8abc%EF%AE%9A"));
        assertEquals("abc=def?sdf&hil sdlkfj l sdf slkdfj 123&def",
                UrlDecoder.decode("abc%3Ddef%3Fsdf%26hil+sdlkfj+l+sdf+slkdfj+123%26def"));
    }

    @Test public void testDecodePath() {
        assertEquals("/", UrlDecoder.decodePath("/").toString());
        assertEquals("/", UrlDecoder.decodePath("").toString());

        assertEquals("/abc", UrlDecoder.decodePath("abc").toString());

        assertEquals("/abc/def", UrlDecoder.decodePath("abc/def").toString());
        assertEquals("/abc/def", UrlDecoder.decodePath("abc/def/").toString());

        assertEquals("/a!@#/def", UrlDecoder.decodePath("a!%40%23/def/").toString());
        assertEquals("/ab/c/def", UrlDecoder.decodePath("/ab%2Fc/def").toString());
        assertEquals("ab/c", UrlDecoder.decodePath("/ab%2Fc/def").getPathElements().get(0));
    }
}
