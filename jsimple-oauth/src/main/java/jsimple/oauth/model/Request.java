package jsimple.oauth.model;

import jsimple.io.IOUtils;
import jsimple.net.HttpRequest;
import jsimple.net.Url;
import jsimple.oauth.exceptions.OAuthConnectionException;
import jsimple.oauth.exceptions.OAuthException;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP Request object
 *
 * @author Pablo Fernandez
 */
public class Request {
    public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

    private String url;
    private Verb verb;
    private ParameterList querystringParams;
    private ParameterList bodyParams;
    private Map<String, String> headers;
    private @Nullable String payload = null;
    private @Nullable HttpRequest httpRequest = null;
    private byte/*@Nullable*/[] bytePayload = null;
    private @Nullable Integer timeout = null;

    /**
     * Creates a new Http Request
     *
     * @param verb Http Verb (GET, POST, etc)
     * @param url  url with optional querystring parameters.
     */
    public Request(Verb verb, String url) {
        this.verb = verb;
        this.url = url;
        this.querystringParams = new ParameterList();
        this.bodyParams = new ParameterList();
        this.headers = new HashMap<String, String>();
    }

    /**
     * Execute the request and return a {@link Response}
     *
     * @return Http Response
     * @throws RuntimeException if the connection cannot be created.
     */
    public Response send() {
        try {
            createConnection();
            return doSend();
        } catch (Exception e) {
            throw new OAuthConnectionException(e);
        }
    }

    private void createConnection() {
        String completeUrl = getCompleteUrl();
        if (httpRequest == null)
            httpRequest = new HttpRequest(completeUrl);
    }

    /**
     * Returns the complete url (host + resource + encoded querystring parameters).
     *
     * @return the complete url.
     */
    public String getCompleteUrl() {
        return querystringParams.appendTo(url);
    }

    Response doSend() {
        HttpRequest httpReq = httpRequest;
        assert httpReq != null : "nullness";

        httpReq.setMethod(getMethodForVerb(this.verb));

        if (timeout != null)
            httpReq.setTimeout(timeout.intValue());

        addHeaders(httpReq);

        if (verb.equals(Verb.PUT) || verb.equals(Verb.POST)) {
            int[] length = new int[1];
            byte[] bodyBytes = getByteBodyContents(length);
            addBody(httpReq, bodyBytes, 0, length[0]);
        }

        return new Response(httpReq);
    }

    public static String getMethodForVerb(Verb verb) {
        switch (verb) {
            case POST:
                return HttpRequest.METHOD_POST;
            case DELETE:
                return HttpRequest.METHOD_DELETE;
            case GET:
                return HttpRequest.METHOD_GET;
            case PUT:
                return HttpRequest.METHOD_PUT;
            default:
                throw new RuntimeException("Unknown verb: " + verb);
        }
    }

    void addHeaders(HttpRequest httpRequest) {
        for (String key : headers.keySet())
            httpRequest.setHeader(key, headers.get(key));
    }

    void addBody(HttpRequest httpRequest, byte[] content, int offset, int length) {
        httpRequest.setHeader(HttpRequest.HEADER_CONTENT_LENGTH, String.valueOf(content.length));

        // Set default content type if none is set
        if (httpRequest.getHeader(HttpRequest.HEADER_CONTENT_TYPE) == null)
            httpRequest.setHeader(HttpRequest.HEADER_CONTENT_TYPE, DEFAULT_CONTENT_TYPE);

        httpRequest.getRequestBodyStream().write(content, offset, length);
    }

    /**
     * Add an HTTP Header to the Request
     *
     * @param key   the header name
     * @param value the header value
     */
    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    /**
     * Add a body Parameter (for POST/ PUT Requests)
     *
     * @param key   the parameter name
     * @param value the parameter value
     */
    public void addBodyParameter(String key, String value) {
        this.bodyParams.add(key, value);
    }

    /**
     * Add a QueryString parameter
     *
     * @param key   the parameter name
     * @param value the parameter value
     */
    public void addQuerystringParameter(String key, String value) {
        this.querystringParams.add(key, value);
    }

    /**
     * Add body payload.
     * <p/>
     * This method is used when the HTTP body is not a form-url-encoded string, but another thing. Like for example
     * XML.
     * <p/>
     * Note: The contents are not part of the OAuth signature
     *
     * @param payload the body of the request
     */
    public void addPayload(String payload) {
        this.payload = payload;
    }

    /**
     * Overloaded version for byte arrays
     *
     * @param payload
     */
    public void addPayload(byte[] payload) {
        this.bytePayload = payload;
    }

    /**
     * Get a {@link ParameterList} with the query string parameters.
     *
     * @return a {@link ParameterList} containing the query string parameters.
     * @throws OAuthException if the request URL is not valid.
     */
    public ParameterList getQueryStringParams() {
        ParameterList result = new ParameterList();
        @Nullable String queryString = new Url(url).getQuery();
        result.addQueryString(queryString);
        result.addAll(querystringParams);
        return result;
    }

    /**
     * Obtains a {@link ParameterList} of the body parameters.
     *
     * @return a {@link ParameterList}containing the body parameters.
     */
    public ParameterList getBodyParams() {
        return bodyParams;
    }

    /**
     * Obtains the URL of the HTTP Request.
     *
     * @return the original URL of the HTTP Request
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the URL without the port and the query string part.
     *
     * @return the OAuth-sanitized URL
     */
    public String getSanitizedUrl() {
        // Old code: return url.replaceAll("\\?.*", "").replace("\\:\\d{4}", "");

        String sanitizedUrl = url;

        int queryStringStart = url.indexOf('?');
        if (queryStringStart != -1)
            sanitizedUrl = sanitizedUrl.substring(0, queryStringStart);
        int colonStart = url.indexOf(':');
        if (colonStart != -1)
            sanitizedUrl = sanitizedUrl.substring(0, colonStart);

        return sanitizedUrl;
    }

    byte[] getByteBodyContents(int[] length) {
        if (bytePayload != null) {
            length[0] = bytePayload.length;
            return bytePayload;
        }

        String body = (payload != null) ? payload : bodyParams.asFormUrlEncodedString();
        return IOUtils.toUtf8BytesFromString(body, length);
    }

    /**
     * Returns the HTTP Verb
     *
     * @return the verb
     */
    public Verb getVerb() {
        return verb;
    }

    /**
     * Returns the connection headers as a {@link Map}
     *
     * @return map of headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets the timeout for the underlying {@link HttpRequest}
     *
     * @param timeoutInMillis duration of the timeout
     */
    public void setTimeout(int timeoutInMillis) {
        this.timeout = timeoutInMillis;
    }

    /*
    * We need this in order to stub the connection object for test cases
    */
    void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override public String toString() {
        return String.format("@Request(%s %s)", getVerb(), getUrl());
    }
}
