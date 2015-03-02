/*
 * Copyright (c) 2012-2015, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jsimple.io;

import jsimple.unit.UnitTest;
import jsimple.util.InvalidFormatException;
import jsimple.util.Utils;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 10/8/12 8:17 PM
 */
public class Utf8InputStreamReaderTest extends UnitTest {

    @Test public void testRead() {
        // Test UTF-8 decoding by reading the UTF-8 stress test file, along with a Unicode encoded version of it,
        // and test the UTF-8 decoder on each line, ensuring the decoded text matches the Unicode version

        String resourcesDirectoryPath = getProjectDirectory() + "/src/test/resources";

        FileSystemDirectory resourcesDirectory = new FileSystemDirectory(resourcesDirectoryPath);

        InputStream unicodeFile = new FileSystemFile(resourcesDirectory,
                resourcesDirectoryPath + "/UTF-8-test.unicode").openForRead();
        int bomByte1 = unicodeFile.read();
        int bomByte2 = unicodeFile.read();
        char bomChar = (char) (bomByte1 | (bomByte2 << 8));
        assertEquals(0xfeff, bomChar);

        InputStream utf8File = new FileSystemFile(resourcesDirectory, resourcesDirectoryPath +
                "/UTF-8-test.utf8").openForRead();
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
            } catch (InvalidFormatException e) {
                assertTrue(unicodeLine.toString().startsWith("MALFORMED"));
            }

            ++lineNumber;
        }
    }

    @Test public void testReadSurrogate() {
        byte[] utf8Input = Utils.byteArrayFromBytes(0xf0, 0x9f, 0xBF, 0xBD);
        char[] expected = {(char) 0xd83f, (char) 0xdffd};
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
        byte[] utf8SurrogateWithFollowingChar = Utils.byteArrayFromBytes(0xf0, 0x9f, 0xBF, 0xBD, 0x22);
        char[] utf8SurrogateWithFollowingCharExpected = {(char) 0xd83f, (char) 0xdffd, (char) 0x22};
        reader = new Utf8InputStreamReader(new jsimple.io.ByteArrayInputStream(utf8SurrogateWithFollowingChar));
        assertEquals(0, reader.read(utf8SurrogateWithFollowingCharBuffer, 0, 1));
        assertEquals(3, reader.read(utf8SurrogateWithFollowingCharBuffer, 0, 3));
        assertArrayEquals(utf8SurrogateWithFollowingCharExpected, utf8SurrogateWithFollowingCharBuffer);

        byte[] incompleteSurrogate1 = Utils.byteArrayFromBytes(0xf0, 0x9f, 0xBF);
        testReadExpectError(incompleteSurrogate1,
                "Invalid UTF-8 encoding--stream ends with a partially defined UTF-8 character");

        byte[] incompleteSurrogate2 = Utils.byteArrayFromBytes(0xf0, 0x9f);
        testReadExpectError(incompleteSurrogate2,
                "Invalid UTF-8 encoding--stream ends with a partially defined UTF-8 character");

        byte[] incompleteSurrogate3 = Utils.byteArrayFromBytes(0xf0, 0x9f);
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
        } catch (InvalidFormatException e) {
            message = e.getMessage();
        }
        assertEquals(expectedMessage, message);
    }
}
