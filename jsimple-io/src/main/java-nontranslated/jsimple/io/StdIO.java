package jsimple.io;

/**
 * The StdIO class can be used to write to stdout & stderr.
 *
 * @author Bret Johnson
 * @since 12/2/12 12:50 AM
 */
public class StdIO {
    public static final OutputStream stdoutStream = new JSimpleOutputStreamOnJavaStream(System.out);
    public static final OutputStream stderrStream = new JSimpleOutputStreamOnJavaStream(System.err);

    public static final Writer stdout = new Utf8OutputStreamWriter(stdoutStream);
    public static final Writer stderr = new Utf8OutputStreamWriter(stderrStream);
}
