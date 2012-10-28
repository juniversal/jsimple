package jsimple.net;

import jsimple.io.*;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
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
public class HttpRequest extends HttpRequestBase {
    private HttpURLConnection httpUrlConnection;

    public HttpRequest(String url) {
        // System.getProperty("http.keepAlive") defaults to true.  We don't change it here as the doc is a bit unclear
        // exactly when that property is checked for HttpURLConnection.  The app can set it to something else if they
        // want different behavior.
        //System.setProperty("http.keepAlive", keepAlive ? "true" : "false");

        try {
            httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
        } catch (java.io.IOException e) {
            throw new jsimple.io.IOException(e);
        }
    }

    @Override public void setMethod(String method) {
        try {
            httpUrlConnection.setRequestMethod(method);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void setConnectTimeout(int timeoutInMillis) {
        httpUrlConnection.setConnectTimeout(timeoutInMillis);

    }

    @Override public void setReadTimeout(int timeoutInMillis) {
        httpUrlConnection.setReadTimeout(timeoutInMillis);
    }

    @Override public void setHeader(String key, String value) {
        httpUrlConnection.setRequestProperty(key, value);
    }

    @Override public String getHeader(String key) {
        return httpUrlConnection.getRequestProperty(key);
    }

    @Override public void setDoOutput(boolean doOutput) {
        httpUrlConnection.setDoOutput(doOutput);

    }

    @Override public OutputStream getOutputStream() {
        try {
            return new JSimpleOutputStreamOnJavaStream(httpUrlConnection.getOutputStream());
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public void connect() {
        try {
            httpUrlConnection.connect();
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public int getResponseCode() {
        try {
            return httpUrlConnection.getResponseCode();
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public Map<String, List<String>> getHeaderFields() {
        return httpUrlConnection.getHeaderFields();
    }

    @Override public InputStream getInputStream() {
        try {
            return new JSimpleInputStreamOnJavaStream(httpUrlConnection.getInputStream());
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public InputStream getErrorStream() {
        return new JSimpleInputStreamOnJavaStream(httpUrlConnection.getErrorStream());
    }
}