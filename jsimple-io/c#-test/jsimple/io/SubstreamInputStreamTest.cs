namespace jsimple.io {

    using UnitTest = jsimple.unit.UnitTest;
    using NUnit.Framework;

    /// <summary>
    /// @author Bret Johnson
    /// @since 8/22/13 4:58 PM
    /// </summary>
    public class SubstreamInputStreamTest : UnitTest {
        [Test] public virtual void testReadByte()
        {
            sbyte[] testData = new sbyte[100];
            for (int i = 0; i < testData.Length; i++)
                testData[i] = (sbyte) i;

            sbyte[] buffer = new sbyte[4];
            int byteRead;

            // Test reading up to end

            ByteArrayInputStream outerStream = new ByteArrayInputStream(testData);
            SubstreamInputStream substreamInputStream = new SubstreamInputStream(outerStream, 2);

            byteRead = substreamInputStream.read();
            assertEquals(0, byteRead);

            byteRead = substreamInputStream.read();
            assertEquals(1, byteRead);

            byteRead = substreamInputStream.read();
            assertEquals(-1, byteRead);

            // Test premature end of stream

            outerStream = new ByteArrayInputStream(testData);
            substreamInputStream = new SubstreamInputStream(outerStream, 200);

            for (int i = 0; i < 100; i++) {
                byteRead = substreamInputStream.read();
                assertEquals(i, byteRead);
            }

            bool exceptionThrown = false;
            try {
                substreamInputStream.read();
            }
            catch (IOException) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }

        [Test] public virtual void testRead()
        {
            sbyte[] testData = new sbyte[100];
            for (int i = 0; i < testData.Length; i++)
                testData[i] = (sbyte) i;

            sbyte[] buffer = new sbyte[4];
            int amountRead;

            // Test reading up to end

            ByteArrayInputStream outerStream = new ByteArrayInputStream(testData);
            SubstreamInputStream substreamInputStream = new SubstreamInputStream(outerStream, 4);

            amountRead = substreamInputStream.read(buffer);
            assertEquals(4, amountRead);
            assertEquals(3, buffer[3]);

            amountRead = substreamInputStream.read(buffer);
            assertEquals(-1, amountRead);

            amountRead = substreamInputStream.read(buffer);
            assertEquals(-1, amountRead);

            // Should always return 0 if asked to read 0 bytes, even at end of stream
            amountRead = substreamInputStream.read(buffer, 2, 0);
            assertEquals(0, amountRead);

            // Test reading past end of substream

            outerStream = new ByteArrayInputStream(testData);
            substreamInputStream = new SubstreamInputStream(outerStream, 4);

            buffer = new sbyte[10];
            amountRead = substreamInputStream.read(buffer);
            assertEquals(4, amountRead);
            assertEquals(3, buffer[3]);

            amountRead = substreamInputStream.read(buffer);
            assertEquals(-1, amountRead);

            // Test premature end of stream

            outerStream = new ByteArrayInputStream(testData);
            substreamInputStream = new SubstreamInputStream(outerStream, 200);

            buffer = new sbyte[150];
            amountRead = substreamInputStream.read(buffer);
            assertEquals(100, amountRead);

            bool exceptionThrown = false;
            try {
                substreamInputStream.read(buffer);
            }
            catch (IOException) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);
        }
    }

}