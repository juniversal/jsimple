package main.java.com.special_friend.facebook;

import jsimple.oauth.model.Token;
import jsimple.util.List;
import jsimple.util.ArrayList;
import main.java.com.special_friend.model.FacebookFriend;
import main.java.com.special_friend.model.FacebookPost;
import main.java.com.special_friend.util.Constants;

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
	
	public List<FacebookPost> getMyFilteredWall(int postLimit,List<FacebookFriend> specialFriends){
		List<FacebookPost> ret = new ArrayList<FacebookPost>();
		String wallJson = connector.getMyTimeline(postLimit);
		
		List<String> friendNames = new ArrayList<String>();
		for (FacebookFriend friend : specialFriends){
			friendNames.add(friend.getName());
		}
		List<FacebookPost> posts = parser.parseWall(wallJson);
		for (FacebookPost facebookPost : posts) {
			if (friendNames.contains(facebookPost.getFrom())) {
				ret.add(facebookPost);
			}
		}
		
		return ret;
	}
	
	public List<FacebookFriend> getAllFbFriends(){
		String friendsJson = connector.getFriendList();
		return parser.parserFriendList(friendsJson);
	}
}
