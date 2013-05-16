using System.Text;

namespace jsimple.io
{

	using UnitTest = jsimple.unit.UnitTest;
	using Utils = jsimple.util.Utils;
	using NUnit.Framework;

	/// <summary>
	/// @author Bret Johnson
	/// @since 10/8/12 8:17 PM
	/// </summary>
	public class Utf8InputStreamReaderTest : UnitTest
	{

		[Test] public virtual void testRead()
		{
			// Test UTF-8 decoding by reading the UTF-8 stress test file, along with a Unicode encoded version of it,
			// and test the UTF-8 decoder on each line, ensuring the decoded text matches the Unicode version

			string resourcesDirectoryPath = JavaProjectDirectory + "/src/test/resources";

			FileSystemDirectory resourcesDirectory = new FileSystemDirectory(resourcesDirectoryPath);

			InputStream unicodeFile = (new FileSystemFile(resourcesDirectory, resourcesDirectoryPath + "/UTF-8-test.unicode")).openForRead();
			int bomByte1 = unicodeFile.read();
			int bomByte2 = unicodeFile.read();
			char bomChar = (char)(bomByte1 | (bomByte2 << 8));
			assertEquals(0xfeff, bomChar);

			InputStream utf8File = (new FileSystemFile(resourcesDirectory, resourcesDirectoryPath + "/UTF-8-test.utf8")).openForRead();
			bomByte1 = utf8File.read();
			assertEquals(0xef, bomByte1);
			bomByte2 = utf8File.read();
			assertEquals(0xbb, bomByte2);
			int bomByte3 = utf8File.read();
			assertEquals(0xbf, bomByte3);

			sbyte[] utf8Line = new sbyte[1000];

			int lineNumber = 1;
			while (true)
			{
				bool atEof = false;

				int i = 0;
				int b;
				while (true)
				{
					b = utf8File.read();
					if (b == '\r')
						continue;
					else if (b == '\n')
						break;
					else if (b == -1)
					{
						atEof = true;
						break;
					}
					else
						utf8Line[i++] = (sbyte) b;
				}

				if (atEof)
				{
					assertEquals(-1, unicodeFile.read()); // Both files should reach the end at the same time
					break;
				}

				StringBuilder unicodeLine = new StringBuilder();
				char c;
				while (true)
				{
					int byte1 = unicodeFile.read();
					int byte2 = unicodeFile.read();

					c = (char)(byte1 | (byte2 << 8));
					if (c == '\r')
						continue;
					else if (c == '\n')
						break;
					else
						unicodeLine.Append(c);
				}

				while (c != '\n');

				int charsRead = 0;
				char[] buffer = new char[i + 1];
				Utf8InputStreamReader reader = new Utf8InputStreamReader(new jsimple.io.ByteArrayInputStream(utf8Line, 0, i));
				try
				{
					charsRead = reader.read(buffer);
					if (charsRead == -1)
						charsRead = 0;

					assertFalse(unicodeLine.ToString().StartsWith("MALFORMED"));

					string decoded = new string(buffer, 0, charsRead);

					string prefix = "Line " + lineNumber + ": ";
					assertEquals(prefix + unicodeLine, prefix + decoded);

					prefix = "Line " + lineNumber + " bytes read at end: ";
					assertEquals(prefix + -1, prefix + reader.read(buffer)); // The reader should be at end of stream
				}
				catch (CharConversionException)
				{
					assertTrue(unicodeLine.ToString().StartsWith("MALFORMED"));
				}

				++lineNumber;
			}
		}

		[Test] public virtual void testReadSurrogate()
		{
			sbyte[] utf8Input = Utils.byteArrayFromBytes(0xf0, 0x9f, 0xBF, 0xBD);
			char[] expected = new char[] {(char) 0xd83f, (char) 0xdffd};
			char[] buffer = new char[2];
			Utf8InputStreamReader reader;

			reader = new Utf8InputStreamReader(new jsimple.io.ByteArrayInputStream(utf8Input, 0, 4));
			assertEquals(2, reader.read(buffer));
			assertArrayEquals(expected, buffer);

			reader = new Utf8InputStreamReader(new jsimple.io.ByteArrayInputStream(utf8Input, 0, 4));
			assertEquals(0, reader.read(buffer, 0, 1));
			assertEquals(2, reader.read(buffer, 0, 2));
			assertArrayEquals(expected, buffer);

			char[] utf8SurrogateWithFollowingCharBuffer = new char[3];
			sbyte[] utf8SurrogateWithFollowingChar = Utils.byteArrayFromBytes(0xf0, 0x9f, 0xBF, 0xBD, 0x22);
			char[] utf8SurrogateWithFollowingCharExpected = new char[] {(char) 0xd83f, (char) 0xdffd, (char) 0x22};
			reader = new Utf8InputStreamReader(new jsimple.io.ByteArrayInputStream(utf8SurrogateWithFollowingChar));
			assertEquals(0, reader.read(utf8SurrogateWithFollowingCharBuffer, 0, 1));
			assertEquals(3, reader.read(utf8SurrogateWithFollowingCharBuffer, 0, 3));
			assertArrayEquals(utf8SurrogateWithFollowingCharExpected, utf8SurrogateWithFollowingCharBuffer);

			sbyte[] incompleteSurrogate1 = Utils.byteArrayFromBytes(0xf0, 0x9f, 0xBF);
			testReadExpectError(incompleteSurrogate1, "Invalid UTF-8 encoding--stream ends with a partially defined UTF-8 character");

			sbyte[] incompleteSurrogate2 = Utils.byteArrayFromBytes(0xf0, 0x9f);
			testReadExpectError(incompleteSurrogate2, "Invalid UTF-8 encoding--stream ends with a partially defined UTF-8 character");

			sbyte[] incompleteSurrogate3 = Utils.byteArrayFromBytes(0xf0, 0x9f);
			testReadExpectError(incompleteSurrogate3, "Invalid UTF-8 encoding--stream ends with a partially defined UTF-8 character");
		}

		private void testReadExpectError(sbyte[] utf8Data, string expectedMessage)
		{
			Utf8InputStreamReader reader;
			reader = new Utf8InputStreamReader(new ByteArrayInputStream(utf8Data));

			string message = "";
			char[] buffer = new char[500];
			try
			{
				reader.read(buffer);
			}
			catch (CharConversionException e)
			{
				message = e.Message;
			}
			assertEquals(expectedMessage, message);
		}
	}

}