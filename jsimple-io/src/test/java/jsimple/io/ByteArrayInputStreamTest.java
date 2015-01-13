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
public class ByteArrayInputStreamTest extends UnitTest {
	@Test public void testRead() {
		byte[] data = new byte[3];
		data[0] = 1;
		data[1] = 2;
		data[2] = 3;

		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		byte[] readData = new byte[3];
		bais.read(readData);
		bais.close();

		assertArrayEquals(data, readData);
	}

	@Test public void testReadFully() {
		byte[] data = new byte[3];
		data[0] = 1;
		data[1] = 2;
		data[2] = 3;

		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		byte[] readData = new byte[3];
		bais.readFully(readData);
		bais.close();
		
		assertArrayEquals(data, readData);
	}

	@Test public void testCoppy() {
		byte[] data = new byte[3];
		data[0] = 1;
		data[1] = 2;
		data[2] = 3;

		ByteArrayInputStream sut = new ByteArrayInputStream(data);
		ByteArrayOutputStream coppy = new ByteArrayOutputStream();

		sut.copyTo(coppy);
		byte[] coppiedData = coppy.getByteArray().toByteArray();
		sut.close();

		assertEquals(data.length, coppiedData.length);
		assertArrayEquals(data, coppiedData);
	}

	@Test public void testOffsetRead() {
		byte[] data = new byte[3];
		data[0] = 1;
		data[1] = 2;
		data[2] = 3;

		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		byte[] readData = new byte[4];
		bais.readFully(readData, 1, 4);
		bais.close();
	
		assertEquals(0, readData[0]);
		assertEquals(data[0], readData[1]);
	}
	
	@Test public void testCoppyToByteArray(){
		byte[] data = new byte[3];
		data[0] = 1;
		data[1] = 2;
		data[2] = 3;

		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		byte[] readData = bais.copyToByteArray().toByteArray();
		bais.close();
	
		assertEquals(data[0], readData[0]);
		assertEquals(data[1], readData[1]);
	}
}
