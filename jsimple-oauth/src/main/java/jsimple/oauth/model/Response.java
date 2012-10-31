package jsimple.oauth.model;

import jsimple.net.HttpRequest;
import jsimple.io.IOUtils;
import jsimple.io.InputStream;
import jsimple.net.HttpResponse;
import jsimple.net.UnknownHostException;
import jsimple.oauth.exceptions.OAuthException;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an HTTP Response.
 *
 * @author Pablo Fernandez
 */
public class Response {
    private static final String EMPTY = "";

    private int code;
    private String body;
    private HttpResponse httpResponse;

    Response(HttpRequest httpRequest) {
        try {
            httpResponse = httpRequest.getResponse();
            code = httpResponse.getResponseCode();
        } catch (UnknownHostException e) {
            throw new OAuthException("The IP address of a host could not be determined.", e);
        }
    }

    /**
     * Obtains the HTTP Response body
     *
     * @return response body
     */
    public String getBody() {
        if (body == null)
            body = IOUtils.toStringFromUtf8Stream(getBodyStream());
        return body;
    }

    /**
     * Obtains the meaningful stream of the HttpUrlConnection, either inputStream or errorInputStream, depending on the
     * status code
     *
     * @return input stream / error stream
     */
    public InputStream getBodyStream() {
        return httpResponse.getBodyStream();
    }

    /**
     * Obtains the HTTP status code
     *
     * @return the status code
     */
    public int getCode() {
        return code;
    }

    /**
     * Obtains a single HTTP Header value, or null if not present.
     *
     * @param name the header name
     * @return header value or null
     */
    public @Nullable String getHeader(String name) {
        return httpResponse.getHeader(name);
    }
}