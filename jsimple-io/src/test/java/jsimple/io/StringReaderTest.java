package jsimple.io;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Dorin Suletea
 * @since 12/1/2014
 */
public class StringReaderTest {
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
