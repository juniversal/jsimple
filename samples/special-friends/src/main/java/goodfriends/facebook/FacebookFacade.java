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
	
	public List<FacebookFriend> getAllFbFriends(){
		String friendsJson = connector.getFriendList();
		return parser.parserFriendList(friendsJson);
	}
}
