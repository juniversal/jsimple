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
import org.junit.Test;

/**
 * @author Dorin Suletea
 * @since 12/1/2014
 */
public class StringReaderTest extends UnitTest {
    @Test
    public void testRead() {
        String initialData = "Test String";
        StringReader sut = new StringReader(initialData);
        int currentChar;
        char[] line = new char[11];
        int index = -1;
        while ((currentChar = sut.read()) != -1) {
            line[++index] = (char) currentChar;
        }
        String output = new String(line);
        assertEquals(initialData, output);
        sut.close();
    }

    @Test
    public void testReadBuffer() {
        String initialData = "Test String";
        StringReader sut = new StringReader(initialData);
        char[] buffer = new char[11];
        sut.read(buffer);
        sut.close();

        assertEquals(initialData.charAt(0), buffer[0]);
        assertEquals(initialData.charAt(10), buffer[10]);
    }

    @Test
    public void testReadOffsetBuffer() {
        String initialData = "Test String";
        StringReader sut = new StringReader(initialData);
        char[] buffer = new char[5];
        sut.read(buffer, 1, 4);
        sut.close();

        assertEquals(0, buffer[0]);
        assertEquals(initialData.charAt(0), buffer[1]);
    }

    @Test
    public void testClose() {
        String initialData = "Test String";
        StringReader sut = new StringReader(initialData);
        sut.close();
        try {
            sut.read();
        } catch (IOException e) {
            return;
        }
        fail("IO exception should be thrown");
    }
}
