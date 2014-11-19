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

package jsimple.io;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 3/16/13 12:20 AM
 */
public class DirectoryTest extends UnitTest {
    public DirectoryTest() {
        JSimpleIO.init();
    }

    @Test public void testCreateFile() throws Exception {
        Directory testOutputDirectory = Paths.getInstance().getTestOutputDirectory("testCreateFile");

        File file = testOutputDirectory.getFile("testfile.txt");

        createFileWithTestContents(file);
        validateTestContents(file);
    }

    @Test public void testGetOrCreateDirectory() throws Exception {
        Directory testOutputDirectory = Paths.getInstance().getTestOutputDirectory("testGetOrCreateDirectory");

        Directory dir = testOutputDirectory.createDirectory("dir");
        Directory childDirectory = testOutputDirectory.createDirectory("child-dir");

        Directory regetChildDirectory = testOutputDirectory.getDirectory("child-dir");
    }

    @Test public void testDeleteContents() {
        Directory testOutputDirectory = Paths.getInstance().getTestOutputDirectory("testDeleteContents");

        Directory directory = testOutputDirectory.createDirectory("dir");
        Directory childDirectory = testOutputDirectory.createDirectory("child-dir");

        testOutputDirectory.deleteContents();

        assertTrue(testOutputDirectory.isEmpty());
    }

    private void createFileWithTestContents(File file) {
        Writer writer = new Utf8OutputStreamWriter(file.openForCreate());
        writer.write("hello there");
        writer.close();
    }

    private void validateTestContents(File file) {
        Reader reader = new Utf8InputStreamReader(file.openForRead());
        String contents = IOUtils.toStringFromReader(reader);
        assertEquals("hello there", contents);
    }
}
