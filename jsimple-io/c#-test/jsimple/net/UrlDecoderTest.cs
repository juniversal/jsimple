namespace jsimple.net {

    using UnitTest = jsimple.unit.UnitTest;
    using NUnit.Framework;

    /// <summary>
    /// @author Bret Johnson
    /// @since 11/25/12 11:16 PM
    /// </summary>
    public class UrlDecoderTest : UnitTest {
        [Test] public virtual void testDecode()
        {
            assertEquals(".-_*", UrlDecoder.decode(".-_*")); // None encoded
            assertEquals("$+!'(),", UrlDecoder.decode("%24%2B%21%27%28%29%2C")); // All encoded
            assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", UrlDecoder.decode("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")); // None encoded
            assertEquals("x\u00eb\u0106\u04b8abc\ufb9a", UrlDecoder.decode("x%C3%AB%C4%86%D2%B8abc%EF%AE%9A"));
            assertEquals("abc=def?sdf&hil sdlkfj l sdf slkdfj 123&def", UrlDecoder.decode("abc%3Ddef%3Fsdf%26hil+sdlkfj+l+sdf+slkdfj+123%26def"));
        }

        [Test] public virtual void testDecodePath()
        {
            assertEquals("/", UrlDecoder.decodePath("/").ToString());
            assertEquals("/", UrlDecoder.decodePath("").ToString());

            assertEquals("/abc", UrlDecoder.decodePath("abc").ToString());

            assertEquals("/abc/def", UrlDecoder.decodePath("abc/def").ToString());
            assertEquals("/abc/def", UrlDecoder.decodePath("abc/def/").ToString());

            assertEquals("/a!@#/def", UrlDecoder.decodePath("a!%40%23/def/").ToString());
            assertEquals("/ab/c/def", UrlDecoder.decodePath("/ab%2Fc/def").ToString());
            assertEquals("ab/c", UrlDecoder.decodePath("/ab%2Fc/def").FirstElement);
        }
    }

}