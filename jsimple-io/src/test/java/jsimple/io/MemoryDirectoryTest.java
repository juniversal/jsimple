package jsimple.io;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 3/23/13 10:09 PM
 */

public class MemoryDirectoryTest extends UnitTest {
    @Test public void testCreateFile() throws Exception {
        MemoryDirectory rootDirectory = new MemoryDirectory("root");

        File file = rootDirectory.createFile("testfile.txt");

        createFileWithTestContents(file);
        validateTestContents(file);

        File regetFile = rootDirectory.getFile("testfile.txt");
        assertTrue(regetFile == file);
    }

    @Test public void testGetOrCreateDirectory() throws Exception {
        MemoryDirectory rootDirectory = new MemoryDirectory("root");

        Directory testDirectory = rootDirectory.getOrCreateDirectory("test-dir");

        Directory childDirectory = testDirectory.getOrCreateDirectory("test-child-dir");
        Directory regetChildDirectory = testDirectory.getDirectory("test-child-dir");
        assertTrue(childDirectory == regetChildDirectory);
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
