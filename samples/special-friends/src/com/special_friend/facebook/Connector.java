package com.special_friend.facebook;

import jsimple.io.Utf8InputStreamReader;
import jsimple.net.HttpRequest;
import jsimple.util.BasicException;
/**
 * Base class for Connectors
 */
public abstract class Connector {

	/**
	 * Use this to send request to Facebook and Microsoft-Mobile platforms
	 * Every class thats suposed to send requests out should extend or have a reference to an implementation of this class
	 * Will send the request and return it's response body as String output
	 * In case of a bad request a basic error containing the error message from the server is thrown.
	 * @param req
	 */
	protected String sendRequest(HttpRequest req) {
		jsimple.net.HttpResponse response = req.send();

		jsimple.io.BufferedReader in = new jsimple.io.BufferedReader(new Utf8InputStreamReader(response.getBodyStream()));
		String inputLine;
		String ret = new String();

		while ((inputLine = in.readLine()) != null) {
			ret += inputLine;
		}
		in.close();
		
		if (response.getStatusCode()==400){
			throw new BasicException(ret);
		}
		
		return ret;
	}
}
