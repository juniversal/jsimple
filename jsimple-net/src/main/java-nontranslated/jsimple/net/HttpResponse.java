package jsimple.net;

import jsimple.io.InputStream;
import jsimple.io.JSimpleInputStreamOnJavaStream;
import jsimple.io.JavaIOUtils;
import org.jetbrains.annotations.Nullable;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * This class handles http connectivity.  It's based on a subset of the standard Java java.net.HttpURLConnection class,
 * with some changes (to improve names, get rid of checked exceptions, etc.).
 * <p/>
 * From the doc for java.net.HttpURLConnection:  Each instance of this class is used to make a single request but the
 * underlying network connection to the HTTP server may be transparently shared by other instances. Calling the close()
 * methods on the InputStream or OutputStream of an HttpURLConnection after a request may free network resources
 * associated with this instance but has no effect on any shared persistent connection. Calling the disconnect() method
 * may close the underlying socket if a persistent connection is otherwise idle at that time.
 * <p/>
 * This class is platform independent.  Platforms (Java, C# for .Net, etc.) should define a subclass called
 * "HttpRequest" which is what all the code will instantiate and use.  That subclass should implement the abstract
 * methods below and can implement other, platform specific, methods.  Handling platform specific classes this way has a
 * few advantages:  There are no factory classes to define/implement nor a need to set a factory at app startup. And
 * platform specific code can freely use platform specific methods implemented on the subclass with no casting
 * required.
 *
 * @author Bret Johnson
 * @since 10/6/12 12:58 AM
 */
public class HttpResponse extends HttpResponseBase {
    private HttpURLConnection httpUrlConnection;
    private @Nullable InputStream bodyStream = null;

    HttpResponse(HttpURLConnection httpUrlConnection) {
        this.httpUrlConnection = httpUrlConnection;
    }

    @Override public int getResponseCode() {
        try {
            return httpUrlConnection.getResponseCode();
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public String getHeader(String headerName) {
        return httpUrlConnection.getHeaderField(headerName);
    }

    @Override public InputStream getBodyStream() {
        if (bodyStream == null) {
            try {
                bodyStream = new JSimpleInputStreamOnJavaStream(httpUrlConnection.getInputStream());
            } catch (java.io.IOException e) {
                throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
            }
        }

        return bodyStream;
    }
}