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
