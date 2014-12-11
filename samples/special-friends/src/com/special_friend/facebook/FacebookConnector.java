package com.special_friend.facebook;

import jsimple.net.HttpRequest;

public class FacebookConnector extends Connector {
	private String authToken;

	public FacebookConnector(String authToken) {
		this.authToken = authToken;
	}

	public String getMyTimeline() {
		String request = "https://graph.facebook.com/me/home?access_token=" + authToken;
		HttpRequest req = HttpRequest.create(request);

		String response = sendRequest(req);
		return response;
	}

	public String getFriendList() {
		String request = "https://graph.facebook.com/me/taggable_friends?access_token=" + authToken;
		HttpRequest req = HttpRequest.create(request);

		String response = sendRequest(req);
		return response;
	}
	
	public String getMyInfo(){
		String request = "https://graph.facebook.com/me?access_token=" + authToken;
		HttpRequest req = HttpRequest.create(request);

		String response = sendRequest(req);
		return response;
	}
	

	
}
