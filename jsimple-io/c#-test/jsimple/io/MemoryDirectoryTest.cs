namespace jsimple.io
{

	using UnitTest = jsimple.unit.UnitTest;
	using NUnit.Framework;

	/// <summary>
	/// @author Bret Johnson
	/// @since 3/23/13 10:09 PM
	/// </summary>

	public class MemoryDirectoryTest : UnitTest
	{
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: [Test] public void testCreateFile() throws Exception
		[Test] public virtual void testCreateFile()
		{
			MemoryDirectory rootDirectory = new MemoryDirectory("root");

			File file = rootDirectory.createFile("testfile.txt");

			createFileWithTestContents(file);
			validateTestContents(file);

			File regetFile = rootDirectory.getFile("testfile.txt");
			assertTrue(regetFile == file);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: [Test] public void testGetOrCreateDirectory() throws Exception
		[Test] public virtual void testGetOrCreateDirectory()
		{
			MemoryDirectory rootDirectory = new MemoryDirectory("root");

			Directory testDirectory = rootDirectory.getOrCreateDirectory("test-dir");

			Directory childDirectory = testDirectory.getOrCreateDirectory("test-child-dir");
			Directory regetChildDirectory = testDirectory.getDirectory("test-child-dir");
			assertTrue(childDirectory == regetChildDirectory);
		}

		private void createFileWithTestContents(File file)
		{
			Writer writer = new Utf8OutputStreamWriter(file.openForCreate());
			writer.write("hello there");
			writer.close();
		}

		private void validateTestContents(File file)
		{
			Reader reader = new Utf8InputStreamReader(file.openForRead());
			string contents = IOUtils.toStringFromReader(reader);
			assertEquals("hello there", contents);
		}
	}

}