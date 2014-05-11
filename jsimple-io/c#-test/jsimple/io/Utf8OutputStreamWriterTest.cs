using System.Text;

namespace jsimple.io {

    using UnitTest = jsimple.unit.UnitTest;
    using ByteArrayRange = jsimple.util.ByteArrayRange;
    using StringUtils = jsimple.util.StringUtils;
    using NUnit.Framework;

    /// <summary>
    /// @author Bret Johnson
    /// @since 10/15/12 8:17 PM
    /// </summary>
    public class Utf8OutputStreamWriterTest : UnitTest {
        [Test] public virtual void testWrite()
        {
            testRoundTripping("a");
            testRoundTripping("abcdef");

            // Test all valid Unicode characters in the basic code plane (that is, 16 bit characters)
            StringBuilder uni = new StringBuilder();
            for (char c = '\u0000'; c <= '\ud7ff'; ++c)
                uni.Append(c);
            for (char c = '\ue000'; c < '\uffff'; ++c)
                uni.Append(c);
            uni.Append('\uffff');

            // Write most of the characters again, backwards this time
            for (char c = '\ud7ff'; c > 0; --c)
                uni.Append(c);
            uni.Append('\0');

            // Test a sampling of valid Unicode characters in the other 16 code plans (all of which need to be represented
            // with a surrogate pair)
            for (int codePlane = 1; codePlane <= 16; ++codePlane)
                appendCodePoints(codePlane, uni);

            testRoundTripping(uni.ToString());
        }

        private void appendCodePoints(int codePlane, StringBuilder uni) {
            int highWord = codePlane << 16;

            StringUtils.appendCodePoint(uni, highWord | 0x0000);
            StringUtils.appendCodePoint(uni, highWord | 0x0001);
            StringUtils.appendCodePoint(uni, highWord | 0x000F);
            StringUtils.appendCodePoint(uni, highWord | 0xFFFE);
            StringUtils.appendCodePoint(uni, highWord | 0xFFFF);
        }

        private void testRoundTripping(string input) {
            ByteArrayRange utf8Bytes = IOUtils.toUtf8BytesFromString(input);
            string output = IOUtils.toStringFromUtf8Bytes(utf8Bytes);
            assertEquals(input, output);
        }

        [Test] public virtual void testErrors()
        {
            testExpectingError("\uDC00", "surrogate pair must start with a lead surrogate"); // Single trail surrogate, bottom bound
            testExpectingError("\uDFFF", "surrogate pair must start with a lead surrogate"); // Single trail surrogate, top bound

            testExpectingError("\uD800\uDBFF", "only a valid trail surrogate should come after a lead surrogate"); // Two lead surrogates in a row

            testExpectingError("\uD800a", "only a valid trail surrogate should come after a lead surrogate"); // No trail surrogate

            testExpectingError("\uD800\uE000", "only a valid trail surrogate should come after a lead surrogate"); // No trail surrogate
            testExpectingError("\uD800\uFFFF", "only a valid trail surrogate should come after a lead surrogate"); // No trail surrogate
            testExpectingError("\uD800\u0800", "only a valid trail surrogate should come after a lead surrogate"); // No trail surrogate
        }

        public virtual void testExpectingError(string input, string expectedMessageSnippet) {
            bool encounteredError = false;

            try {
                IOUtils.toUtf8BytesFromString(input);
            }
            catch (CharConversionException e) {
                string message = e.Message;
                if (message == null)
                    message = "null";

                assertTrue("Message:\n" + message + "\ndoesn't contain expected snippet:\n" + expectedMessageSnippet, message.Contains(expectedMessageSnippet));
                return;
            }

            assertTrue("Converting invalid input: '" + input + "' should have failed", encounteredError);
        }

        [Test] public virtual void testSingleLeadSurrogate()
        {
            // If there's only a lead surrogate at the end of the string, then it's skipped (without generating an error) &
            // nothing is written to the output bytes for it
            ByteArrayRange utf8Bytes = IOUtils.toUtf8BytesFromString("\uD800");

            assertEquals(0, utf8Bytes.Length);
        }

        [Test] public virtual void testBreakInMiddleOfSurrogate()
        {
            string s = "\uDBFF\uDC00";

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Utf8OutputStreamWriter utf8OutputStreamWriter = new Utf8OutputStreamWriter(byteArrayOutputStream);
            utf8OutputStreamWriter.write(s[0]);
            utf8OutputStreamWriter.write(s[1]);
            utf8OutputStreamWriter.flush();

            ByteArrayRange byteArrayRange = byteArrayOutputStream.closeAndGetByteArray();
            assertEquals(s, IOUtils.toStringFromUtf8Bytes(byteArrayRange));
        }
    }

}