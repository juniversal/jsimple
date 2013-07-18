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
		public DirectoryTest()
		{
			JSimpleIO.init();
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: [Test] public void testCreateFile() throws Exception
		[Test] public virtual void testCreateFile()
		{
			Directory testOutputDirectory = Paths.Instance.getTestOutputDirectory("testCreateFile");

			File file = testOutputDirectory.getFile("testfile.txt");

			createFileWithTestContents(file);
			validateTestContents(file);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: [Test] public void testGetOrCreateDirectory() throws Exception
		[Test] public virtual void testGetOrCreateDirectory()
		{
			Directory testOutputDirectory = Paths.Instance.getTestOutputDirectory("testGetOrCreateDirectory");

			Directory dir = testOutputDirectory.createDirectory("dir");
			Directory childDirectory = testOutputDirectory.createDirectory("child-dir");

			Directory regetChildDirectory = testOutputDirectory.getDirectory("child-dir");
		}

		[Test] public virtual void testDeleteContents()
		{
			Directory testOutputDirectory = Paths.Instance.getTestOutputDirectory("testDeleteContents");

			Directory directory = testOutputDirectory.createDirectory("dir");
			Directory childDirectory = testOutputDirectory.createDirectory("child-dir");

			testOutputDirectory.deleteContents();

			assertTrue(testOutputDirectory.Empty);
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