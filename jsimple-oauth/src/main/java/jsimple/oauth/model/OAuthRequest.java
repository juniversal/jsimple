package jsimple.oauth.model;

import jsimple.io.IOUtils;
import jsimple.net.HttpRequest;
import jsimple.net.HttpRequestParams;
import jsimple.net.UnknownHostException;
import jsimple.net.Url;
import jsimple.oauth.exceptions.OAuthConnectionException;
import jsimple.oauth.exceptions.OAuthException;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * The representation of an OAuth HttpRequest.
 *
 * @author Pablo Fernandez
 */
public class OAuthRequest {
    private String url;
    private String verb;
    private ParameterList queryStringParams;
    private ParameterList bodyParams;
    private Map<String, String> headers;
    private @Nullable String payload = null;
    private @Nullable HttpRequest httpRequest = null;
    private byte/*@Nullable*/[] bytePayload = null;
    private @Nullable Integer timeout = null;
    private Map<String, String> oauthParameters;

    public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

    private static final String OAUTH_PREFIX = "oauth_";

    /**
     * Default constructor.
     *
     * @param verb Http verb/method
     * @param url  resource URL
     */
    public OAuthRequest(String verb, String url) {
        this.verb = verb;
        this.url = url;
        this.queryStringParams = new ParameterList();
        this.bodyParams = new ParameterList();
        this.headers = new HashMap<String, String>();

        this.oauthParameters = new HashMap<String, String>();
    }

    /**
     * Adds an OAuth parameter.
     *
     * @param key   name of the parameter
     * @param value value of the parameter
     * @throws IllegalArgumentException if the parameter is not an OAuth parameter
     */
    public void addOAuthParameter(String key, String value) {
        oauthParameters.put(checkKey(key), value);
    }

    private String checkKey(String key) {
        if (key.startsWith(OAUTH_PREFIX) || key.equals(OAuthConstants.SCOPE)) {
            return key;
        } else {
            throw new IllegalArgumentException(String.format("OAuth parameters must either be '%s' or start with '%s'",
                    OAuthConstants.SCOPE, OAUTH_PREFIX));
        }
    }

    /**
     * Returns the {@link Map} containing the key-value pair of parameters.
     *
     * @return parameters as map
     */
    public Map<String, String> getOauthParameters() {
        return oauthParameters;
    }

    @Override public String toString() {
        return String.format("@OAuthRequest(%s, %s)", getVerb(), getUrl());
    }

    /**
     * Execute the request and return a {@link jsimple.oauth.model.OAuthResponse}
     *
     * @return Http Response
     * @throws RuntimeException if the connection cannot be created.
     */
    public OAuthResponse send() {
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
            httpRequest = HttpRequest.create(completeUrl);
    }

    /**
     * Returns the complete url (host + resource + encoded querystring parameters).
     *
     * @return the complete url.
     */
    public String getCompleteUrl() {
        return queryStringParams.appendTo(url);
    }

    OAuthResponse doSend() {
        HttpRequest httpReq = httpRequest;
        assert httpReq != null : "nullness";

        httpReq.setMethod(this.verb);

        if (timeout != null)
            httpReq.setTimeout(timeout.intValue());

        addHeaders(httpReq);

        if (verb.equals("PUT") || verb.equals("POST")) {
            int[] length = new int[1];
            byte[] bodyBytes = getByteBodyContents(length);
            addBody(httpReq, bodyBytes, 0, length[0]);
        }

        try {
            return new OAuthResponse(httpReq.send());
        } catch (UnknownHostException e) {
            throw new OAuthException("The IP address of a host could not be determined.", e);
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

        httpRequest.createRequestBodyStream().write(content, offset, length);
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
     * Add a query string parameter
     *
     * @param key   the parameter name
     * @param value the parameter value
     */
    public void addQueryStringParameter(String key, String value) {
        this.queryStringParams.add(key, value);
    }

    /**
     * Add the passed parameters to the query string
     *
     * @param params parameters
     */
    public void addQueryStringParameters(HttpRequestParams params) {
        for (String name : params.getNames())
            addQueryStringParameter(name, params.getValue(name));
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
     * Get a {@link jsimple.oauth.model.ParameterList} with the query string parameters.
     *
     * @return a {@link jsimple.oauth.model.ParameterList} containing the query string parameters.
     * @throws jsimple.oauth.exceptions.OAuthException
     *          if the request URL is not valid.
     */
    public ParameterList getQueryStringParams() {
        ParameterList result = new ParameterList();
        @Nullable String queryString = new Url(url).getQuery();
        result.addQueryString(queryString);
        result.addAll(queryStringParams);
        return result;
    }

    /**
     * Obtains a {@link jsimple.oauth.model.ParameterList} of the body parameters.
     *
     * @return a {@link jsimple.oauth.model.ParameterList}containing the body parameters.
     */
    public ParameterList getBodyParams() {
        return bodyParams;
    }

    /**
     * Obtains the URL of the HTTP request.
     *
     * @return the original URL of the HTTP request
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
    public String getVerb() {
        return verb;
    }

    /**
     * Returns the connection headers as a {@link java.util.Map}
     *
     * @return map of headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets the timeout for the underlying {@link jsimple.net.HttpRequest}
     *
     * @param timeoutInMillis duration of the timeout
     */
    public void setTimeout(int timeoutInMillis) {
        this.timeout = timeoutInMillis;
    }

    /**
     * We need this in order to stub the connection object for test cases
     *
     * @param httpRequest
     */
    void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }
}
