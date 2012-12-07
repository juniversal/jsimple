package jsimple.net;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Bret Johnson
 * @since 11/25/12 11:16 PM
 */
public class UrlDecoderTest {
    @Test public void testDecode() {
        assertEquals(".-_*", UrlDecoder.decode(".-_*"));   // None encoded
        assertEquals("$+!'(),", UrlDecoder.decode("%24%2B%21%27%28%29%2C"));   // All encoded
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
                UrlDecoder.decode("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));   // None encoded
        assertEquals("xëĆҸabcﮚ", UrlDecoder.decode("x%C3%AB%C4%86%D2%B8abc%EF%AE%9A"));
        assertEquals("abc=def?sdf&hil sdlkfj l sdf slkdfj 123&def",
                UrlDecoder.decode("abc%3Ddef%3Fsdf%26hil+sdlkfj+l+sdf+slkdfj+123%26def"));
    }
}
