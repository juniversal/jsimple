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
public class StringWriterTest extends UnitTest {
	@Test
	public void testWriteArray() {
		char[] data = new char[3];
		data[0] = '1';
		data[1] = '2';
		data[2] = '3';

		StringWriter sut = new StringWriter();
		sut.write(data);
		assertEquals(sut.getBuffer().charAt(0), data[0]);
		assertEquals(sut.getBuffer().charAt(1), data[1]);
		assertEquals(sut.getBuffer().charAt(2), data[2]);

		sut.close();
	}

	@Test
	public void testWriteChar() {
		char[] data = new char[3];
		data[0] = '1';
		data[1] = '2';
		data[2] = '3';

		StringWriter sut = new StringWriter();
		sut.write(data);
		sut.write('X');

		assertEquals(sut.getBuffer().charAt(0), data[0]);
		assertEquals(sut.getBuffer().charAt(1), data[1]);
		assertEquals(sut.getBuffer().charAt(2), data[2]);
		assertEquals(sut.getBuffer().charAt(3), 'X');

		sut.close();
	}

	@Test
	public void testLineSeparator() {
		char lineSeparatorChr = '[';
		String lineSeparator = new String(new char[] { lineSeparatorChr });

		StringWriter sut = new StringWriter();
		sut.setLineSeparator(lineSeparator);
		sut.writeln();

		assertEquals(sut.getBuffer().charAt(0), lineSeparatorChr);

		sut.close();
	}

	@Test
	public void testWriteString() {
		StringWriter sut = new StringWriter();
		sut.write("Test");

		assertEquals('T', sut.getBuffer().charAt(0));
		assertEquals('t', sut.getBuffer().charAt(3));
		sut.close();
	}

	@Test
	public void testClose() {
		StringWriter sut = new StringWriter();
		sut.close();
		sut.write('4');

		// No exception should get thrown here (at least in the current implementation)
	}
}
