/*
 * Copyright (c) 2012-2014, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

using System.IO;
using NUnit.Framework;

namespace jsimple.unit
{
    [TestFixture]
    public class UnitTest : UnitTestBase
    {
        private static string projectDirectory;

        public override string getJavaProjectDirectory()
        {
            if (projectDirectory == null)
            {
                // Assume that the project structure is c#-test/bin/Debug|Release.  So go three levels up to get the
                // platform independent project directory, rooting the Java source code
                var directory = Directory.GetCurrentDirectory();
                directory = Path.GetDirectoryName(directory);
                directory = Path.GetDirectoryName(directory);
                projectDirectory = Path.GetDirectoryName(directory);
            }

            return projectDirectory;
        }

        public override void assertEquals(string message, object expected, object actual)
        {
            Assert.AreEqual(expected, actual, message);
        }

        /// <summary>
        ///     Asserts that two object arrays are equal. If they are not, a test framework assertion error is thrown with the
        ///     given message. If <code>expecteds</code> and <code>actuals</code> are <code>null</code>, they are considered
        ///     equal.
        /// </summary>
        /// <param name="message">   the identifying message for the test framework assertion error (<code>null</code> okay) </param>
        /// <param name="expecteds"> Object array or array of arrays (multi-dimensional array) with expected values. </param>
        /// <param name="actuals">   Object array or array of arrays (multi-dimensional array) with actual values </param>
        public override void assertArrayEquals(string message, object[] expecteds, object[] actuals)
        {
            Assert.AreEqual(expecteds, actuals, message);
        }

        public override void assertArrayEquals(string message, sbyte[] expecteds, sbyte[] actuals)
        {
            Assert.AreEqual(expecteds, actuals, message);
        }

        public override void assertArrayEquals(string message, char[] expecteds, char[] actuals)
        {
            Assert.AreEqual(expecteds, actuals, message);
        }

        public override void assertArrayEquals(string message, short[] expecteds, short[] actuals)
        {
            Assert.AreEqual(expecteds, actuals, message);
        }

        public override void assertArrayEquals(string message, int[] expecteds, int[] actuals)
        {
            Assert.AreEqual(expecteds, actuals, message);
        }

        public override void assertArrayEquals(string message, long[] expecteds, long[] actuals)
        {
            Assert.AreEqual(expecteds, actuals, message);
        }

        public override void assertSame(string message, object expected, object actual)
        {
            Assert.AreSame(expected, actual, message);
        }

        public override void fail(string message)
        {
            Assert.Fail(message);
        }
    }
}