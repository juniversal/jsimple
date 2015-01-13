/*
 * Copyright (c) 2012-2015, Microsoft Mobile
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

package goodfriends.util;

import jsimple.io.BufferedReader;
import jsimple.io.Utf8InputStreamReader;
import jsimple.net.HttpRequest;
import jsimple.util.BasicException;

/**
 * Base class for Connectors
 * Implements methods for sending read REST requests for third party services
 */
public abstract class Connector {

    /**
     * Use this to send request to Facebook and Microsoft-Mobile platforms. Every class that's supposed to send requests
     * out should extend or have a reference to an implementation of this class. Will send the request and return it's
     * response body as String output. In case of a bad request a basic error containing the error message from the
     * server is thrown.
     *
     * @param req
     */
    protected String sendRequest(HttpRequest req) {
        //send the request
    	jsimple.net.HttpResponse response = req.send();
        //read the return data
    	//please note that jsimple reads the input stream of the response object , instead of using the outputstream
    	//of the connection
        BufferedReader in = new BufferedReader(new Utf8InputStreamReader(response.getBodyStream()));
        String inputLine;
        String ret = "";

        while ((inputLine = in.readLine()) != null) {
            ret += inputLine;
        }
        in.close();

        //if the response code is not success , throw an exception with the text == error message
        if (response.getStatusCode() == 400) {
            throw new BasicException(ret);
        }

		return ret;
	}
}