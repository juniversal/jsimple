package jsimple.io;

import jsimple.unit.UnitTest;

import org.junit.Test;

/**
 * @author Dorin Suletea
 * @since 12/1/2014
 */
public class BufferedReaderTest extends UnitTest {
	private String resourcesDirectoryPath = getJavaProjectDirectory()
			+ "/src/test/resources";

	public BufferedReaderTest() {
		JSimpleIO.init();
	}

	@Test
	public void testReadChar() {
		BufferedReader br = getBufferedReader();
		char first = (char) br.read();
		char second = (char) br.read();
		char third = (char) br.read();

		assertEquals('L', first);
		assertEquals('o', second);
		assertEquals('r', third);
		br.close();
	}

	@Test
	public void testReadEnd() {
		BufferedReader br = getBufferedReader();
		// forward the reader
		while (-1 != br.read()) {
		}
		br.read();

		assertEquals(-1, br.read());
	}

	@Test
	public void testReadCharOutOfBounds() {
		BufferedReader br = getBufferedReader();
		char[] chars = new char[3];
		// forward the reader
		while (-1 != br.read()) {
		}
		// chech return value
		int ret = br.read(chars);
		assertEquals(-1, ret);

		// check buffer values not overwritten
		assertEquals(0, chars[0]);
		assertEquals(0, chars[1]);
		assertEquals(0, chars[2]);
	}

	@Test
	public void testReadCharArrayOffset() {
		BufferedReader br = getBufferedReader();
		char[] chars = new char[3];
		chars[0] = 'x';

		br.read(chars, 1, 2);
		assertEquals('x', chars[0]);
		assertEquals('L', chars[1]);
		assertEquals('o', chars[2]);
	}
	
	

	@Test
	public void testMark() {
		BufferedReader br = getBufferedReader();
		// br.hashCode()

		br.read();
		br.mark(2);
		int second = br.read();
		br.read();
		br.reset();
		int sut = br.read();
		// mark reset sucessful
		assertEquals(sut, second);
		br.close();
	}
	
	@Test
	public void testHashCode() {
		BufferedReader br = getBufferedReader();
		int hc1 = br.hashCode();
		br.read();
		int hc2 = br.hashCode();
		br.skip(2);
		br.close();
		int hc3 = br.hashCode();
		
		br.close();

		assertTrue(hc1 == hc2 && hc2 == hc3);
	}

	@Test
	public void testSkip() {
		BufferedReader br = getBufferedReader();
		// skip 2 chars by reading , store third
		br.read();
		br.read();
		int third = br.read();
		br.close();

		// reopen same the file , skip first 2 characters
		BufferedReader br2 = getBufferedReader();
		br2.skip(2);
		int sut = br2.read();
		br2.close();

		assertEquals(third, sut);
	}

	@Test
	public void testReadArray() {
		BufferedReader br = getBufferedReader();
		int first = br.read();
		int second = br.read();
		int third = br.read();

		br.close();

		BufferedReader br2 = getBufferedReader();
		char[] sut = new char[3];
		br2.read(sut);

		assertEquals(sut[0], first);
		assertEquals(sut[1], second);
		assertEquals(sut[2], third);

	}

	@Test
	public void testReadLine() {
		BufferedReader br = getBufferedReader();
		char currentChar;
		char[] line = new char[8192];
		int index = -1;
		br.mark(8192);
		while ((currentChar = (char) br.read()) != '\n') {
			line[++index] = currentChar;
		}

		// drop the unused characters in line buffer
		char[] trimedLine = new char[index];
		for (int i = 0; i < index; i++) {
			trimedLine[i] = line[i];
		}

		String stringLine = new String(trimedLine);
		br.reset();
		String sut = br.readLine();

		assertEquals(sut, stringLine);

		br.close();

	}

	@Test
	public void readUTF8() {
		// try {
		// java.io.BufferedReader br;
		// try {
		// br = new java.io.BufferedReader(new
		// java.io.FileReader(resourcesDirectoryPath + "/UTF-8-test.utf8"));
		// StringBuilder sb = new StringBuilder();
		// String line = br.readLine();
		// System.out.println(line);
		// } catch (java.io.IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		FileSystemDirectory resourcesDirectory = new FileSystemDirectory(
				resourcesDirectoryPath);
		InputStream plainTextFile = new FileSystemFile(resourcesDirectory,
				resourcesDirectoryPath + "/UTF-8-test.utf8").openForRead();
		BufferedReader br = new BufferedReader(new Utf8InputStreamReader(
				plainTextFile));
		br.read();

		br.close();
	}

	private BufferedReader getBufferedReader() {
		FileSystemDirectory resourcesDirectory = new FileSystemDirectory(
				resourcesDirectoryPath);
		InputStream plainTextFile = new FileSystemFile(resourcesDirectory,
				resourcesDirectoryPath + "/plain-text.txt").openForRead();
		BufferedReader br = new BufferedReader(new Utf8InputStreamReader(
				plainTextFile));
		return br;
	}

}