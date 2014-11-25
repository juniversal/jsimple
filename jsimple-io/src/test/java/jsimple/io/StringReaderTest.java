package jsimple.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
}
