package com.special_friend.facebook;

import jsimple.net.HttpRequest;

/**
 * Facebook raw-data extractor Specialized class that composes parameters and request specific strings in order to form
 * a valid facebook request, after the request is sent data is extracted and returned as raw jsons
 */
public class FacebookConnector extends Connector {
    private String authToken;

    public FacebookConnector(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Extracts the posts from the user's timeline posts
     *
     * @param postLimit how many posts to extract from the timeline , as the number increases the slower the posts will
     *                  be returned
     * @return raw posts as json
     */
    public String getMyTimeline(Integer postLimit) {
        String request = "https://graph.facebook.com/me/home?limit=" + postLimit + "&access_token=" + authToken;
        HttpRequest req = HttpRequest.create(request);

        String response = sendRequest(req);
        return response;
    }

    /**
     * Extracts the user's taggable friends friends
     *
     * @return raw friends as json
     */
    public String getFriendList() {
        String request = "https://graph.facebook.com/me/taggable_friends?access_token=" + authToken;
        HttpRequest req = HttpRequest.create(request);

        String response = sendRequest(req);
        return response;
    }

    /**
     * Extracts the id of the person using the application
     *
     * @return user's info json
     */
    public String getMyInfo() {
        String request = "https://graph.facebook.com/me?access_token=" + authToken;
        HttpRequest req = HttpRequest.create(request);

        String response = sendRequest(req);
        return response;
    }


}
