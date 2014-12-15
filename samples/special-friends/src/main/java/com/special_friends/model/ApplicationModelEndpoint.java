package com.special_friends.model;

import jsimple.util.ArrayList;
import jsimple.util.List;

import com.special_friend.facebook.Constants;
import com.special_friend.parsers.AzureMobileParser;
import com.special_friend.parsers.FacebookParser;
import com.special_friend.persistance.AzureMobileConnector;
import com.special_friend.persistance.LocalFileConnector;
/**
 * Application facade.
 * Most of the app's functionality is exposed here
 */
public class ApplicationModelEndpoint {
	private final static ApplicationModelEndpoint instance = new ApplicationModelEndpoint();
	//current user id , so we can poll the data relevant only for this user
	private String myID;
	//current user's good friends
	private List<FacebookFriend> specialFriends;

	//delegates 
	private AzureMobileConnector azureConnector;
	private AzureMobileParser azureParser;
	private FacebookParser fbParser;
	private LocalFileConnector localFileConnector;
	

	public static ApplicationModelEndpoint getInstance() {
		return instance;
	}

	public ApplicationModelEndpoint() {
		specialFriends = new ArrayList<FacebookFriend>();
		azureConnector = new AzureMobileConnector();
		azureParser = new AzureMobileParser();
		fbParser = new FacebookParser();
		localFileConnector = new LocalFileConnector();
	}

	public void addSpecialFriend(FacebookFriend friend) {
		if (!isSpecialFriendAdded(friend)) {
			specialFriends.add(friend);
		}
	}

	public boolean isSpecialFriendAdded(FacebookFriend friend) {
		return specialFriends.contains(friend);
	}

	public void saveToFile(){
		localFileConnector.saveData(specialFriends);
	}
	
	public void loadFromFile(){
		specialFriends = localFileConnector.loadData();
	}
	
	public void loadFromCloud() {
		specialFriends = loadFriendsFromCloud();
	}

	public void saveToCloud() {
		cleadFriendsDB();
		for (FacebookFriend friend : specialFriends) {
			azureConnector.saveData(Constants.AZURE_FRIEND_TABLE_NAME, friend.toJson());
		}
	}

	private List<FacebookFriend> loadFriendsFromCloud() {
		String friendListJson = azureConnector.selectFriends(Constants.AZURE_FRIEND_TABLE_NAME);
		return azureParser.parserFriendList(friendListJson);
	}

	public void cleadFriendsDB() {
		String existingFriendJson = azureConnector.selectFriends(Constants.AZURE_FRIEND_TABLE_NAME);
		List<String> persistedFriends = azureParser.parserFriendListIDs(existingFriendJson);
		for (String id : persistedFriends) {
			azureConnector.deleteData(Constants.AZURE_FRIEND_TABLE_NAME, id);
		}
	}

	public List<FacebookFriend> getSpecialFriends() {
		return specialFriends;
	}

	public List<FacebookPost> getSpecialFriendPosts(String myWall) {
		List<FacebookPost> posts = fbParser.parseWall(myWall);
		List<FacebookPost> ret = new ArrayList<FacebookPost>();
		List<String> friendsNames = specialFriendsNames();

		for (FacebookPost facebookPost : posts) {
			if (friendsNames.contains(facebookPost.getFrom())) {
				ret.add(facebookPost);
			}
		}
		return ret;
	}

	private List<String> specialFriendsNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (FacebookFriend fr : specialFriends) {
			names.add(fr.getName());
		}
		return names;
	}
	
	public void setMyID(String myID){
		this.myID = myID;
	}

	public String getMyID() {
		return myID;
	}
	

	
}
