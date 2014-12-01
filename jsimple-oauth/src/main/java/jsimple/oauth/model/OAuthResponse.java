/*
 * Portions copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from the Scribe Java OAuth
 * library https://github.com/fernandezpablo85/scribe-java (collectively, “Third
 * Party Code”). Microsoft Mobile is not the original author of the Third Party
 * Code.
 *
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
 */

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