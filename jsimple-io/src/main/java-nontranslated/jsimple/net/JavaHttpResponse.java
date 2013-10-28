package jsimple.net;

import jsimple.io.InputStream;
import jsimple.io.JSimpleInputStreamOnJavaStream;
import jsimple.io.JavaIOUtils;
import org.jetbrains.annotations.Nullable;

import java.net.HttpURLConnection;

/**
 * Java platform implementation of platform independent HttpResponse.  This implementation is basically a wrapper around
 * the response part of HttpURLConnection.
 *
 * @author Bret Johnson
 * @since 10/6/12 12:58 AM
 */
public class JavaHttpResponse extends HttpResponse {
    private HttpURLConnection httpUrlConnection;
    private @Nullable InputStream bodyStream = null;

    JavaHttpResponse(HttpURLConnection httpUrlConnection) {
        this.httpUrlConnection = httpUrlConnection;
    }

    @Override public int getStatusCode() {
        try {
            return httpUrlConnection.getResponseCode();
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public String getStatusMessage() {
        try {
            String message = httpUrlConnection.getResponseMessage();

            // On a connection error, an exception is thrown.  But if null is returned, according to the docs that means
            // the response wasn't valid HTTP, so we turn that into a descriptive string.
            if (message == null)
                message = "<error parsing status line message>";

            return message;
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public String getHeader(String headerName) {
        return httpUrlConnection.getHeaderField(headerName);
    }

    @Override public InputStream getBodyStream() {
        if (bodyStream == null) {
            int statusCode = getStatusCode();
            try {
                // TODO: Check status status code is exactly right (check Java source)

                if (statusCode < 200 || statusCode >= 300) {
                    java.io.InputStream errorStream = httpUrlConnection.getErrorStream();
                    if (errorStream == null)
                        bodyStream = new EmptyInputStream();
                    else bodyStream = new JSimpleInputStreamOnJavaStream(errorStream);
                } else bodyStream = new JSimpleInputStreamOnJavaStream(httpUrlConnection.getInputStream());
            } catch (java.io.IOException e) {
                throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
            }
        }

        return bodyStream;
    }

    @Override public void close() {
        httpUrlConnection.disconnect();
    }

    public static class EmptyInputStream extends InputStream {
        @Override public int read() {
            return -1;
        }

        @Override public int read(byte[] buffer, int offset, int length) {
            return length == 0 ? 0 : -1;
        }

        @Override public void close() {
        }
    }
}