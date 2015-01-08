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
