package jsimple.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class SequenceInputStreamTest {

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
		sut.read(output);
		// for (int i = 0; i < 6; i++) {
		// System.out.println(output[i]);
		// }
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
		
		
		//test size
		assertEquals(output.length, data1.length+data2.length);
		//test first and last elements
		assertEquals(output[0],data1[0]);
		assertEquals(output[5],data2[2]);	
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
		// for (int i = 0; i < 6; i++) {
		// System.out.println(output[i]);
		// }
		sut.close();

		assertEquals(data1[0], output[0]);
		assertEquals(data2[2], output[5]);
	}
}
