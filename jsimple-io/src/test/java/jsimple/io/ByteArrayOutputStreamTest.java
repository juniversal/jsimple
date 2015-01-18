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
import jsimple.util.ByteArrayRange;
import jsimple.util.StringUtils;

import org.junit.Test;

/**
 * @author Dorin Suletea
 * @since 12/1/2014
 */
public class ByteArrayOutputStreamTest extends UnitTest {

	@Test
	public void testWriteReadBytes() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		baos.closeAndGetByteArray()
//		baos.getLength()
		byte[] data = new byte[3];
		data[0] = 1;
		data[1] = 2;
		data[2] = 3;
		baos.write(data);

		byte[] sut = baos.getByteArray().toByteArray();

		assertArrayEquals(data, sut);
		baos.close();
	}

	@Test
	public void testReset() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] data = new byte[] { 1, 2, 3};
		baos.write(data);

		// reset the stream , replace first byte with 5
		baos.reset();
		byte newFirst = 5;
		baos.write(newFirst);
		byte[] sut = baos.getByteArray().toByteArray();
		baos.close();

		assertArrayEquals(sut, new byte[] { newFirst });
	}

	// the stream is closed multiple times , the trigger should be activated
	// only once
	private int closedListenerTriggeredCount = 0;

	@Test
	public void testCloseListenerTriggerCount() {
		closedListenerTriggeredCount = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.setClosedListener(new ClosedListener() {
			@Override
			public void onClosed() {
				closedListenerTriggeredCount++;
			}
		});

		baos.close();
		baos.close();
		assertEquals(1, closedListenerTriggeredCount);
	}

	@Test
	public void testWriteLatin() {
		String text = "test text";
		ByteArrayOutputStream sut = new ByteArrayOutputStream();
		sut.writeLatin1EncodedString(text);

		String output = StringUtils.toStringFromLatin1Bytes(sut.getByteArray().toByteArray());
		sut.close();

		assertEquals(text, output);
	}

	@Test
	public void testWriteUTF8() {
		String text = "test text UTF8";
		ByteArrayOutputStream sut = new ByteArrayOutputStream();
		sut.writeUtf8EncodedString(text);

		byte[] expected = new byte[text.length()];
		for (int i = 0; i < text.length(); ++i)
			expected[i] = (byte) text.charAt(i);

		sut.close();

		assertArrayEquals(expected, sut.getByteArray().toByteArray());
	}
	
	@Test
	public void testCloseAndGetByteArray(){
		closedListenerTriggeredCount = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] data = new byte[3];
		data[0] = 1;
		data[1] = 2;
		data[2] = 3;
		baos.write(data);
		
		baos.setClosedListener(new ClosedListener() {
			@Override
			public void onClosed() {
				closedListenerTriggeredCount++;
			}
		});
		ByteArrayRange byteRange = baos.closeAndGetByteArray();
		byte [] bytes = byteRange.toByteArray();
		baos.close();
		
		assertEquals(bytes[0], data[0]);
		assertEquals(bytes[1], data[1]);
		assertEquals(1, closedListenerTriggeredCount);
	}
}
