using System.IO;
using NUnit.Framework;

namespace jsimple.unit
{
	[TestFixture] public class UnitTest : UnitTestBase
	{
        private static string projectDirectory = null;
        public override string JavaProjectDirectory
        {
            get
            {
                if (projectDirectory == null)
                {
                    // Assume that the project structure is c#-test/bin/Debug|Release.  So go three levels up to get the
                    // platform independent project directory, rooting the Java source code
                    string directory = System.IO.Directory.GetCurrentDirectory();
                    directory = System.IO.Path.GetDirectoryName(directory);
                    directory = System.IO.Path.GetDirectoryName(directory);
                    projectDirectory = System.IO.Path.GetDirectoryName(directory);
                }

                return projectDirectory;
            }
        }

		override public void assertEquals(string message, object expected, object actual)
		{
            Assert.AreEqual(expected, actual, message);
		}

	    /// <summary>
	    /// Asserts that two object arrays are equal. If they are not, a test framework assertion error is thrown with the
	    /// given message. If <code>expecteds</code> and <code>actuals</code> are <code>null</code>, they are considered
	    /// equal.
	    /// </summary>
	    /// <param name="message">   the identifying message for the test framework assertion error (<code>null</code> okay) </param>
	    /// <param name="expecteds"> Object array or array of arrays (multi-dimensional array) with expected values. </param>
	    /// <param name="actuals">   Object array or array of arrays (multi-dimensional array) with actual values </param>
	    override public void assertArrayEquals(string message, object[] expecteds, object[] actuals)
	    {
            Assert.AreEqual(expecteds, actuals, message);
	    }

        override public void assertArrayEquals(string message, sbyte[] expecteds, sbyte[] actuals)
		{
            Assert.AreEqual(expecteds, actuals, message);
		}

        override public void assertArrayEquals(string message, char[] expecteds, char[] actuals)
		{
            Assert.AreEqual(expecteds, actuals, message);
		}

		override public void assertArrayEquals(string message, short[] expecteds, short[] actuals)
		{
            Assert.AreEqual(expecteds, actuals, message);
		}

		override public void assertArrayEquals(string message, int[] expecteds, int[] actuals)
		{
            Assert.AreEqual(expecteds, actuals, message);
		}

		override public void assertArrayEquals(string message, long[] expecteds, long[] actuals)
		{
            Assert.AreEqual(expecteds, actuals, message);
		}

	    public override void fail(string message)
	    {
	        Assert.Fail(message);
	    }
	}

}