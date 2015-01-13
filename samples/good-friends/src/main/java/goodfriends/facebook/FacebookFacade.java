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

import goodfriends.model.FacebookFriend;
import goodfriends.model.FacebookPost;
import goodfriends.util.Constants;
import jsimple.oauth.model.Token;
import jsimple.util.List;
import jsimple.util.ArrayList;

public class FacebookFacade {
	private FacebookAuthenticator authenticator;
	private FacebookConnector connector;
	private FacebookParser parser;
	
	
	public FacebookFacade() {
		authenticator = new FacebookAuthenticator(Constants.FACEBOOK_SCOPE);
		connector = new FacebookConnector();
		parser = new FacebookParser();
	}
	
	public Token refreshToken(Token oldToken){
		return authenticator.getRefreshedToken(oldToken);
	}
	
	public String getAuthorizationUrl(){
		return authenticator.getAuthorizationUrl();
	}
	
	public Token getAcessToken(String code){
		return authenticator.getAccessToken(code);
	}
	
	public void setConnectorAuthToken(String token){
		connector.setAuthToken(token);
	}
	
	public String getMyFbId(){
		String idJson = connector.getMyInfo();
		return parser.extractMyId(idJson);
	}
	
	public String getMyFbName(){
		String idJson = connector.getMyInfo();
		return parser.extractMyName(idJson);
	}
	
	public List<FacebookPost> getMyFilteredWall(int postLimit,int pageLimit, List<FacebookFriend> specialFriends){
		List<FacebookPost> ret = new ArrayList<FacebookPost>();
		//facebook will apply the filters after it extracts the feeds,
		//so it will grab postLimit number of posts , filter what posts we can see ,
		//and return them.
		//https://developers.facebook.com/blog/post/478/
		String wallJson = connector.getMyTimeline(postLimit);
		String nextPage = "";
		List<String> friendNames = new ArrayList<String>();
		for (FacebookFriend friend : specialFriends){
			friendNames.add(friend.getName());
		}
		
		List<FacebookPost> allPosts = new ArrayList<FacebookPost>();
		for (int i = 0; i < pageLimit; i++) {
			List<FacebookPost> posts = parser.parseWall(wallJson);
			//in case there is no next page , break
			//in the case none of the posts are visible to us an empty json will be returned
			if (posts.size()==0){
				break;
			}
			allPosts.addAll(posts);
			nextPage = parser.getNextPostPage(wallJson);
			wallJson = connector.getMyTimeline(nextPage);
		}
		for (FacebookPost facebookPost :allPosts) {
			if (friendNames.contains(facebookPost.getFrom())) {
				ret.add(facebookPost);
			}
		}
		
		return ret;
	}
	
	public ArrayList<FacebookFriend> getAllFbFriends(){
		String friendsJson = connector.getFriendList();
		return parser.parserFriendList(friendsJson);
	}
}
