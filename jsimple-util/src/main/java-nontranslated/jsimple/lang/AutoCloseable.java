package jsimple.lang;

/**
 * Subclasses of AutoCloseable automatically have their close() method called when used in the Java7 try-with-resources
 * statement.  The C# version of this class implements the IDisposable interface and maps calls to the Dispose method
 * to close().  Essentially this class maps to the target language autoclosable-like interface (assuming it has that).
 *
 * @author Bret Johnson
 * @since 4/26/13 11:58 PM
 */
public abstract class AutoCloseable implements java.lang.AutoCloseable {
    public abstract void close();
}
