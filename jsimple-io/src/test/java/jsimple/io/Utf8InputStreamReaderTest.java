package jsimple.io;

import org.junit.Test;

import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * @author Bret Johnson
 * @since 10/8/12 8:17 PM
 */
public class Utf8InputStreamReaderTest {

    @Test public void testRead() throws Exception {
        // Test UTF-8 decoding by reading the UTF-8 stress test file, along with a Unicode encoded version of it,
        // and test the UTF-8 decoder on each line, ensuring the decoded text matches the Unicode version

        FileInputStream unicodeFile = new FileInputStream("UTF-8-test.unicode");
        int bomByte1 = unicodeFile.read();
        int bomByte2 = unicodeFile.read();
        char bomChar = (char) (bomByte1 | (bomByte2 << 8));
        assertEquals(0xfeff, bomChar);

        FileInputStream utf8File = new FileInputStream("UTF-8-test.utf8");
        bomByte1 = utf8File.read();
        assertEquals(0xef, bomByte1);
        bomByte2 = utf8File.read();
        assertEquals(0xbb, bomByte2);
        int bomByte3 = utf8File.read();
        assertEquals(0xbf, bomByte3);

        byte[] utf8Line = new byte[1000];

        int lineNumber = 1;
        while (true) {
            boolean atEof = false;

            int i = 0;
            int b;
            while (true) {
                b = utf8File.read();
                if (b == '\r')
                    continue;
                else if (b == '\n')
                    break;
                else if (b == -1) {
                    atEof = true;
                    break;
                } else utf8Line[i++] = (byte) b;
            }

            if (atEof) {
                assertEquals(-1, unicodeFile.read());   // Both files should reach the end at the same time
                break;
            }

            StringBuilder unicodeLine = new StringBuilder();
            char c;
            while (true) {
                int byte1 = unicodeFile.read();
                int byte2 = unicodeFile.read();

                c = (char) (byte1 | (byte2 << 8));
                if (c == '\r')
                    continue;
                else if (c == '\n')
                    break;
                else unicodeLine.append(c);
            }

            while (c != '\n') ;

            int charsRead = 0;
            char[] buffer = new char[i + 1];
            Utf8InputStreamReader reader = new Utf8InputStreamReader(new jsimple.io.ByteArrayInputStream(
                    utf8Line, 0, i));
            try {
                charsRead = reader.read(buffer);
                if (charsRead == -1)
                    charsRead = 0;

                assertFalse(unicodeLine.toString().startsWith("MALFORMED"));

                String decoded = new String(buffer, 0, charsRead);

                String prefix = "Line " + lineNumber + ": ";
                assertEquals(prefix + unicodeLine, prefix + decoded);

                prefix = "Line " + lineNumber + " bytes read at end: ";
                assertEquals(prefix + -1, prefix + reader.read(buffer));   // The reader should be at end of stream
            } catch (CharConversionException e) {
                assertTrue(unicodeLine.toString().startsWith("MALFORMED"));
            }

            ++lineNumber;
        }
    }

    @Test public void testReadSurrogate() throws Exception {
        byte[] utf8Input = {(byte) 0xf0, (byte) 0x9f, (byte) 0xBF, (byte) 0xBD};
        char[] expected = {0xd83f, 0xdffd};
        char[] buffer = new char[2];
        Utf8InputStreamReader reader;

        reader = new Utf8InputStreamReader(new jsimple.io.ByteArrayInputStream(utf8Input, 0, 4));
        assertEquals(2, reader.read(buffer));
        assertArrayEquals(expected, buffer);

        reader = new Utf8InputStreamReader(new jsimple.io.ByteArrayInputStream(utf8Input, 0, 4));
        assertEquals(0, reader.read(buffer, 0, 1));
        assertEquals(2, reader.read(buffer, 0, 2));
        assertArrayEquals(expected, buffer);

        char[] utf8SurrogateWithFollowingCharBuffer = new char[3];
        byte[] utf8SurrogateWithFollowingChar = {(byte) 0xf0, (byte) 0x9f, (byte) 0xBF, (byte) 0xBD, (byte) 0x22};
        char[] utf8SurrogateWithFollowingCharExpected = {0xd83f, 0xdffd, 0x22};
        reader = new Utf8InputStreamReader(new jsimple.io.ByteArrayInputStream(utf8SurrogateWithFollowingChar));
        assertEquals(0, reader.read(utf8SurrogateWithFollowingCharBuffer, 0, 1));
        assertEquals(3, reader.read(utf8SurrogateWithFollowingCharBuffer, 0, 3));
        assertArrayEquals(utf8SurrogateWithFollowingCharExpected, utf8SurrogateWithFollowingCharBuffer);

        byte[] incompleteSurrogate1 = {(byte) 0xf0, (byte) 0x9f, (byte) 0xBF};
        testReadExpectError(incompleteSurrogate1,
                "Invalid UTF-8 encoding--stream ends with a partially defined UTF-8 character");

        byte[] incompleteSurrogate2 = {(byte) 0xf0, (byte) 0x9f };
        testReadExpectError(incompleteSurrogate2,
                "Invalid UTF-8 encoding--stream ends with a partially defined UTF-8 character");

        byte[] incompleteSurrogate3 = {(byte) 0xf0, (byte) 0x9f };
        testReadExpectError(incompleteSurrogate3,
                "Invalid UTF-8 encoding--stream ends with a partially defined UTF-8 character");
    }

    private void testReadExpectError(byte[] utf8Data, String expectedMessage) {
        Utf8InputStreamReader reader;
        reader = new Utf8InputStreamReader(new ByteArrayInputStream(utf8Data));

        String message = "";
        char[] buffer = new char[500];
        try {
            reader.read(buffer);
        } catch (CharConversionException e) {
            message = e.getMessage();
        }
        assertEquals(expectedMessage, message);
    }
}
