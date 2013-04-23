package jsimple.io;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

    @Override public OutputStream openForCreate() {
        try {
            return new JSimpleOutputStreamOnJavaStream(Files.newOutputStream(javaPath,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE));
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public String getName() {
        return javaPath.getFileName().toString();
    }

    @Override public void delete(){
        try{
            Files.delete(javaPath);
        } catch (java.io.IOException e){
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

}
