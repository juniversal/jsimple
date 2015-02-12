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

package goodfriends.facebook;

import goodfriends.util.Connector;
import jsimple.net.HttpRequest;

/**
 * Facebook raw-data extractor Specialized class that composes parameters and request specific strings in order to form
 * a valid facebook request, after the request is sent data is extracted and returned as raw jsons
 */
public class FacebookConnector extends Connector {
  private String authToken;

  public FacebookConnector() {
  }

  /**Extracts the posts from the user's time-line
   * @param postLimit how many posts to extract from the timeline , as the number increases the slower the posts will be returned
   * @return raw posts as json */
  public String getMyTimeline(Integer postLimit) {
    String request = "https://graph.facebook.com/me/home?limit=" + postLimit + "&access_token=" + authToken;
    HttpRequest req = HttpRequest.create(request);

    String response = sendRequest(req);
    return response;
  }

  public String getMyTimeline(String request){
    HttpRequest req = HttpRequest.create(request);
    String response = sendRequest(req);
    return response;
  }


  /**Extracts the user's taggable friends friends
   * @return raw friends as json */
  public String getFriendList() {
    String request = "https://graph.facebook.com/me/taggable_friends?access_token=" + authToken;
    HttpRequest req = HttpRequest.create(request);

    String response = sendRequest(req);
    return response;
  }

  /** Extracts the id of the person using the application
   * This id is used in order to persist the good friends list.
   * When extracting the list from the database , only the list belonging to this specific user will be returned.
   * @return user's info json*/
  public String getMyInfo() {
    String request = "https://graph.facebook.com/me?access_token=" + authToken;
    HttpRequest req = HttpRequest.create(request);

    String response = sendRequest(req);
    return response;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

}
