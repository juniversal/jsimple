namespace jsimple.io
{

	using UnitTest = jsimple.unit.UnitTest;
	using NUnit.Framework;

	/// <summary>
	/// @author Bret Johnson
	/// @since 3/16/13 12:20 AM
	/// </summary>
	public class DirectoryTest : UnitTest
	{
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: [Test] public void testCreateFile() throws Exception
		[Test] public virtual void testCreateFile()
		{
			Directory applicationDataDirectory = Paths.ApplicationDataDirectory;

			File file = applicationDataDirectory.createFile("testfile.txt");

			createFileWithTestContents(file);
			validateTestContents(file);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: [Test] public void testGetOrCreateDirectory() throws Exception
		[Test] public virtual void testGetOrCreateDirectory()
		{
			Directory applicationDataDirectory = Paths.ApplicationDataDirectory;

			Directory testDirectory = applicationDataDirectory.getOrCreateDirectory("test-dir");
			Directory childDirectory = testDirectory.getOrCreateDirectory("test-child-dir");

			Directory regetChildDirectory = testDirectory.getDirectory("test-dir");

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