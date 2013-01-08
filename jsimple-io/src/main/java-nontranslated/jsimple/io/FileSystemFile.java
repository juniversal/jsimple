package jsimple.io;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Bret Johnson
 * @since 11/22/12 12:29 AM
 */
public class FileSystemFile extends File {
    private java.nio.file.Path javaPath;

    public FileSystemFile(String filePath) {
        this.javaPath = Paths.get(filePath);
    }

    public FileSystemFile(java.nio.file.Path javaPath) {
        this.javaPath = javaPath;
    }

    @Override public InputStream openForRead() {
        try {
            return new JSimpleInputStreamOnJavaStream(Files.newInputStream(javaPath));
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public String getName() {
        return javaPath.getFileName().toString();
    }

    @Override public Directory getParent() {
        java.nio.file.Path javaParentPath = javaPath.getParent();
        if (javaParentPath == null)
            throw new RuntimeException("File " + javaPath.toString() + " unexpectedly has no parent directory");

        return new FileSystemDirectory(javaParentPath);
    }
}
