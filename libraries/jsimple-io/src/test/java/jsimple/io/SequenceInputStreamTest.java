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
import org.junit.Test;

/**
 * @author Dorin Suletea
 * @since 12/1/2014
 */
public class SequenceInputStreamTest extends UnitTest {

    @Test
    public void testRead() {
        byte[] data1 = new byte[3];
        data1[0] = 1;
        data1[1] = 2;
        data1[2] = 3;
        ByteArrayInputStream bais1 = new ByteArrayInputStream(data1);

        byte[] data2 = new byte[3];
        data2[0] = 4;
        data2[1] = 5;
        data2[2] = 6;
        ByteArrayInputStream bais2 = new ByteArrayInputStream(data2);

        SequenceInputStream sut = new SequenceInputStream(bais1, bais2);

        byte[] output = new byte[6];
        sut.readFully(output);
        sut.read(output);
        sut.close();

        assertEquals(data1[0], output[0]);
        assertEquals(data2[2], output[5]);
    }

    @Test
    public void testCoppy() {
        byte[] data1 = new byte[3];
        data1[0] = 1;
        data1[1] = 2;
        data1[2] = 3;
        ByteArrayInputStream bais1 = new ByteArrayInputStream(data1);

        byte[] data2 = new byte[3];
        data2[0] = 4;
        data2[1] = 5;
        data2[2] = 6;
        ByteArrayInputStream bais2 = new ByteArrayInputStream(data2);

        SequenceInputStream sut = new SequenceInputStream(bais1, bais2);
        ByteArrayOutputStream coppy = new ByteArrayOutputStream();
        sut.copyTo(coppy);
        sut.close();
        coppy.close();
        byte[] output = coppy.getByteArray().toByteArray();

        // test size
        assertEquals(output.length, data1.length + data2.length);
        // test first and last elements
        assertEquals(output[0], data1[0]);
        assertEquals(output[5], data2[2]);
    }

    @Test
    public void testReadFully() {
        byte[] data1 = new byte[3];
        data1[0] = 1;
        data1[1] = 2;
        data1[2] = 3;
        ByteArrayInputStream bais1 = new ByteArrayInputStream(data1);

        byte[] data2 = new byte[3];
        data2[0] = 4;
        data2[1] = 5;
        data2[2] = 6;
        ByteArrayInputStream bais2 = new ByteArrayInputStream(data2);

        SequenceInputStream sut = new SequenceInputStream(bais1, bais2);
        byte[] output = new byte[6];
        sut.readFully(output);
        sut.close();

        assertEquals(data1[0], output[0]);
        assertEquals(data2[2], output[5]);
    }

    @Test
    public void testCoppyToByteArray() {
        byte[] data1 = new byte[3];
        data1[0] = 1;
        data1[1] = 2;
        data1[2] = 3;
        ByteArrayInputStream bais1 = new ByteArrayInputStream(data1);

        byte[] data2 = new byte[3];
        data2[0] = 4;
        data2[1] = 5;
        data2[2] = 6;
        ByteArrayInputStream bais2 = new ByteArrayInputStream(data2);

        SequenceInputStream sut = new SequenceInputStream(bais1, bais2);
        ByteArrayRange outRange = sut.copyToByteArray();
        byte[] output = outRange.toByteArray();
        sut.close();

        assertEquals(data1[0], output[0]);
        assertEquals(data2[2], output[5]);
    }

    @Test
    public void testReadBuffer() {
        byte[] data1 = new byte[3];
        data1[0] = 1;
        data1[1] = 2;
        data1[2] = 3;
        ByteArrayInputStream bais1 = new ByteArrayInputStream(data1);

        byte[] data2 = new byte[3];
        data2[0] = 4;
        data2[1] = 5;
        data2[2] = 6;
        ByteArrayInputStream bais2 = new ByteArrayInputStream(data2);

        SequenceInputStream sut = new SequenceInputStream(bais1, bais2);
        byte[] outData = new byte[5];
        sut.readFully(outData);
        sut.close();

        assertEquals(data1[0], outData[0]);
        assertEquals(data2[1], outData[4]);
    }

    @Test
    public void testReadOffsetBuffer() {
        byte[] data1 = new byte[3];
        data1[0] = 1;
        data1[1] = 2;
        data1[2] = 3;
        ByteArrayInputStream bais1 = new ByteArrayInputStream(data1);

        byte[] data2 = new byte[3];
        data2[0] = 4;
        data2[1] = 5;
        data2[2] = 6;
        ByteArrayInputStream bais2 = new ByteArrayInputStream(data2);

        SequenceInputStream sut = new SequenceInputStream(bais1, bais2);
        byte[] outData = new byte[5];
        sut.readFully(outData, 1, 4);
        sut.close();

        assertEquals(0, outData[0]);
        assertEquals(data1[0], outData[1]);
        assertEquals(data2[0], outData[4]);
    }
}
