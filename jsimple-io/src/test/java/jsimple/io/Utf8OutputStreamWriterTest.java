package jsimple.io;

import jsimple.unit.UnitTest;
import jsimple.util.StringUtils;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 10/15/12 8:17 PM
 */
public class Utf8OutputStreamWriterTest extends UnitTest {
    @Test public void testWrite() {
        testRoundTripping("a");
        testRoundTripping("abcdef");

        // Test all valid Unicode characters in the basic code plane (that is, 16 bit characters)
        StringBuilder uni = new StringBuilder();
        for (char c = '\u0000'; c <= '\ud7ff'; ++c)
            uni.append(c);
        for (char c = '\ue000'; c < '\uffff'; ++c)
            uni.append(c);
        uni.append('\uffff');

        // Write most of the characters again, backwards this time
        for (char c = '\ud7ff'; c > 0; --c)
            uni.append(c);
        uni.append('\0');

        // Test a sampling of valid Unicode characters in the other 16 code plans (all of which need to be represented
        // with a surrogate pair)
        for (int codePlane = 1; codePlane <= 16; ++codePlane)
            appendCodePoints(codePlane, uni);

        testRoundTripping(uni.toString());
    }

    private void appendCodePoints(int codePlane, StringBuilder uni) {
        int highWord = codePlane << 16;

        StringUtils.appendCodePoint(uni, highWord | 0x0000);
        StringUtils.appendCodePoint(uni, highWord | 0x0001);
        StringUtils.appendCodePoint(uni, highWord | 0x000F);
        StringUtils.appendCodePoint(uni, highWord | 0xFFFE);
        StringUtils.appendCodePoint(uni, highWord | 0xFFFF);
    }


    private void testRoundTripping(String input) {
        int[] length = new int[1];
            byte[] utf8Bytes = IOUtils.toUtf8BytesFromString(input, length);

        String output = IOUtils.toStringFromUtf8Bytes(utf8Bytes, 0, length[0]);

        assertEquals(input, output);
    }

    @Test public void testErrors() {
        testExpectingError("\uDC00", "surrogate pair must start with a lead surrogate");   // Single trail surrogate, bottom bound
        testExpectingError("\uDFFF", "surrogate pair must start with a lead surrogate");   // Single trail surrogate, top bound

        testExpectingError("\uD800\uDBFF", "only a valid trail surrogate should come after a lead surrogate");   // Two lead surrogates in a row

        testExpectingError("\uD800a", "only a valid trail surrogate should come after a lead surrogate");   // No trail surrogate

        testExpectingError("\uD800\uE000", "only a valid trail surrogate should come after a lead surrogate");   // No trail surrogate
        testExpectingError("\uD800\uFFFF", "only a valid trail surrogate should come after a lead surrogate");   // No trail surrogate
        testExpectingError("\uD800\u0800", "only a valid trail surrogate should come after a lead surrogate");   // No trail surrogate
    }

    public void testExpectingError(String input, String expectedMessageSnippet) {
        boolean encounteredError = false;

        try {
            int[] length = new int[1];
            byte[] utf8Bytes = IOUtils.toUtf8BytesFromString(input, length);
        } catch (CharConversionException e) {
            String message = e.getMessage();
            if (message == null)
                message = "null";

            assertTrue("Different error message than expected: " + message, message.contains(expectedMessageSnippet));
            return;
        }

        assertTrue("Converting invalid input: '" + input + "' should have failed", encounteredError);
    }

    @Test public void testSingleLeadSurrogate() {
        // If there's only a lead surrogate at the end of the string, then it's skipped (without generating an error) &
        // nothing is written to the output bytes for it
        int[] length = new int[1];
        byte[] utf8Bytes = IOUtils.toUtf8BytesFromString("\uD800", length);

        assertEquals(0, length[0]);
    }

    @Test public void testBreakInMiddleOfSurrogate() {
        String s = "\uDBFF\uDC00";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Utf8OutputStreamWriter utf8OutputStreamWriter = new Utf8OutputStreamWriter(byteArrayOutputStream);
        utf8OutputStreamWriter.write(s.charAt(0));
        utf8OutputStreamWriter.write(s.charAt(1));
        utf8OutputStreamWriter.flush();

        int[] length = new int[1];
        byte[] bytes = byteArrayOutputStream.closeAndGetByteArray(length);

        assertEquals(s, IOUtils.toStringFromUtf8Bytes(bytes, 0, length[0]));
    }
}
