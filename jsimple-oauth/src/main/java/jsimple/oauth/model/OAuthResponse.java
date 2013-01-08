package jsimple.oauth.model;

import jsimple.io.IOUtils;
import jsimple.io.InputStream;
import jsimple.net.HttpResponse;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an HTTP Response to an OAuth request.  Note that this method was named Response in the original Scribe;
 * for JSimple it's called OAuthResponse so that API client use the two parallel named classes of OAuthRequest and
 * OAuthResponse.
 *
 * @author Pablo Fernandez
 */
public class OAuthResponse {
    private static final String EMPTY = "";

    private int code;
    private @Nullable String body = null;
    private HttpResponse httpResponse;

    OAuthResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
        code = httpResponse.getStatusCode();
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

    public HttpResponse getHttpResponse() {
        return httpResponse;
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