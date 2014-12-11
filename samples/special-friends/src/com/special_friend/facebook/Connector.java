package com.special_friend.facebook;

import jsimple.io.Utf8InputStreamReader;
import jsimple.net.HttpRequest;
import jsimple.util.BasicException;

public abstract class Connector {

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
