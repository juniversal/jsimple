package jsimple.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringWriterTest {
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
		String lineSeparator = new String(new char[]{lineSeparatorChr});

		StringWriter sut = new StringWriter();
		sut.setLineSeparator(lineSeparator);
		sut.writeln();

		assertEquals(sut.getBuffer().charAt(0), lineSeparatorChr);

		sut.close();
	}

}
