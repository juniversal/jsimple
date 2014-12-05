package jsimple.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Dorin Suletea
 * @since 12/1/2014
 */
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
		try {
			sut.write(4);
		} catch (Exception e) {
			fail("No exceptions should be thrown");
		}
	}
}
