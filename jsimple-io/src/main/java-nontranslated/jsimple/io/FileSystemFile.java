package jsimple.io;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import jsimple.io.OutputStream;

/**
 *
 * @author Bret Johnson
 * @since 11/22/12 12:29 AM
 */
public class FileSystemFile extends File {
    private java.nio.file.Path javaPath;
    private String originFilePath;

    public FileSystemFile(String filePath) {
        this.javaPath = Paths.get(filePath);
        this.originFilePath = filePath;
    }

    public FileSystemFile(java.nio.file.Path javaPath) {
        this.javaPath = javaPath;
        this.originFilePath = javaPath.toFile().getAbsolutePath();
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

    @Override public OutputStream openForCreateAtomic() {
        System.out.println(javaPath.toFile().getAbsolutePath());
        String originPath = javaPath.toFile().getAbsolutePath();

        final String newPath = originPath + "_temp";
        FileSystemFile tempSystemFile = new FileSystemFile(newPath);

        OutputStream stream = tempSystemFile.openForCreate();

        stream.setClosedListener(new ClosedListener() {
            @Override public void onClosed() {

                delete();
                rename(newPath);
            }
        });

        return stream;
    }

    @Override public String getName() {
        return javaPath.getFileName().toString();
    }

    @Override public void delete(){
        try{
            if(javaPath.toFile().exists())
                Files.delete(javaPath);
        } catch (java.io.IOException e){
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public void rename(String newName){
            java.io.File tempFile = new java.io.File(newName);
            java.io.File originFile = new java.io.File(originFilePath);
            tempFile.renameTo(originFile);
    }

}
