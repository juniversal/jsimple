/*
 * Copyright (c) 2012-2014, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 * This code was adapted from the Scribe Java OAuth library
 * (https://github.com/fernandezpablo85/scribe-java), with modest changes made
 * for JUniversal.  Scribe itself is distributed under the MIT license.
 */

package jsimple.oauth.model;

import jsimple.io.File;
import jsimple.io.IOUtils;
import jsimple.io.InputStream;
import jsimple.io.OutputStream;
import jsimple.logging.Logger;
import jsimple.logging.LoggerFactory;
import jsimple.net.*;
import jsimple.oauth.exceptions.OAuthConnectionException;
import jsimple.oauth.exceptions.OAuthException;
import jsimple.util.ByteArrayRange;
import jsimple.util.PlatformUtils;
import jsimple.util.ProgrammerError;
import jsimple.util.TextualPath;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * The representation of an OAuth HttpRequest.
 *
 * @author Pablo Fernandez
 */
public class OAuthRequest {
    final static Logger logger = LoggerFactory.getLogger("OAuthRequest");

    private String url;
    private String verb;
    private ParameterList queryStringParams;
    private ParameterList bodyParams;
    private Map<String, String> headers;
    private @Nullable ByteArrayRange bytePayload = null;
    private @Nullable String stringPayload = null;
    private @Nullable File filePayload = null;
    private @Nullable HttpRequest httpRequest = null;
    private @Nullable Integer timeout = null;
    private Map<String, String> oauthParameters;

    public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

    private static final String OAUTH_PREFIX = "oauth_";

    /**
     * Construct an OAuthRequest to apply to specified verb against the specified resource URL.
     *
     * @param verb HTTP verb/method
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
     * Construct an OAuthRequest to apply to specified verb against the specified resource.
     *
     * @param verb         HTTP verb/method
     * @param baseUrl      base URL, including server/port and optionally start of path; this should NOT end with a /
     *                     character
     * @param resourcePath path to resource on server, which will be appended to baseUrl
     */
    public OAuthRequest(String verb, String baseUrl, TextualPath resourcePath) {
        this(verb, baseUrl + UrlEncoder.encode(resourcePath));
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
            throw new ProgrammerError("OAuth parameters must either be '{}' or start with '{}'", OAuthConstants.SCOPE,
                    OAUTH_PREFIX);
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
        return "@OAuthRequest(" + getVerb() + ", " + getUrl() + ")";
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

        for (String key : headers.keySet())
            httpReq.setHeader(key, headers.get(key));

        if (verb.equals("PUT") || verb.equals("POST"))
            addBody(httpReq);

        try {
            OAuthResponse response;

            if (logger.isTraceEnabled()) {
                long startTime = PlatformUtils.getCurrentTimeMillis();

                response = new OAuthResponse(httpReq.send());

                long duration = PlatformUtils.getCurrentTimeMillis() - startTime;
                logger.trace("{} {}; took {}ms", verb, getUrl(), duration);
            } else response = new OAuthResponse(httpReq.send());

            return response;
        } catch (UnknownHostException e) {
            throw new OAuthException("The IP address of a host could not be determined.", e);
        }
    }

    private void addBody(HttpRequest httpReq) {
        // Set default content type if none is set
        if (httpReq.getHeader(HttpRequest.HEADER_CONTENT_TYPE) == null)
            httpReq.setHeader(HttpRequest.HEADER_CONTENT_TYPE, DEFAULT_CONTENT_TYPE);

        if (filePayload != null) {
            Long filePayloadSize = filePayload.getSize();
            httpReq.setHeader(HttpRequest.HEADER_CONTENT_LENGTH, filePayloadSize.toString());

            try (InputStream fileStream = filePayload.openForRead()) {
                try (OutputStream bodyStream = httpReq.createRequestBodyStream()) {
                    fileStream.copyTo(bodyStream);
                }
            }
        } else {
            ByteArrayRange byteArrayRange = getByteBodyContents();
            Integer byteArrayRangeLength = byteArrayRange.getLength();
            httpReq.setHeader(HttpRequest.HEADER_CONTENT_LENGTH, byteArrayRangeLength.toString());
            try (OutputStream bodyStream = httpReq.createRequestBodyStream()) {
                bodyStream.write(byteArrayRange);
            }
        }
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
        this.stringPayload = payload;
    }

    /**
     * Overloaded version for byte arrays
     *
     * @param payload
     */
    public void addPayload(byte[] payload) {
        addPayload(new ByteArrayRange(payload));
    }

    /**
     * Overloaded version for byte arrays
     *
     * @param payload
     */
    public void addPayload(ByteArrayRange payload) {
        this.bytePayload = payload;
    }

    /**
     * Overloaded version for byte arrays
     *
     * @param filePayload
     */
    public void addPayload(File filePayload) {
        this.filePayload = filePayload;
    }

    /**
     * Get a {@link jsimple.oauth.model.ParameterList} with the query string parameters.
     *
     * @return a {@link jsimple.oauth.model.ParameterList} containing the query string parameters.
     * @throws jsimple.oauth.exceptions.OAuthException if the request URL is not valid.
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

    ByteArrayRange getByteBodyContents() {
        if (bytePayload != null)
            return bytePayload;

        String body = (stringPayload != null) ? stringPayload : bodyParams.asFormUrlEncodedString();
        return IOUtils.toUtf8BytesFromString(body);
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
