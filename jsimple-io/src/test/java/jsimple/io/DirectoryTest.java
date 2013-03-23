package jsimple.io;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 3/16/13 12:20 AM
 */
public class DirectoryTest extends UnitTest {
    @Test public void testCreateFile() throws Exception {
        Directory applicationDataDirectory = Paths.getApplicationDataDirectory();

        File file = applicationDataDirectory.createFile("testfile.txt");

        createFileWithTestContents(file);
        validateTestContents(file);
    }

    @Test public void testGetOrCreateDirectory() throws Exception {
        Directory applicationDataDirectory = Paths.getApplicationDataDirectory();

        Directory testDirectory = applicationDataDirectory.getOrCreateDirectory("test-dir");
        Directory childDirectory = testDirectory.getOrCreateDirectory("test-child-dir");

        Directory regetChildDirectory = testDirectory.getDirectory("test-dir");

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
