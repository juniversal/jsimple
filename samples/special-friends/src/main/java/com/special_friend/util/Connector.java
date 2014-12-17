package main.java.com.special_friend.util;

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

        //if the response code is not sucess , throw an exeption with the text == error message
        if (response.getStatusCode() == 400) {
            throw new BasicException(ret);
        }

		return ret;
	}
}