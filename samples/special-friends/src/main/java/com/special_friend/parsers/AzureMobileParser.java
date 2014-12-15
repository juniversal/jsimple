package com.special_friend.parsers;

import jsimple.io.StringReader;
import jsimple.json.objectmodel.JsonArray;
import jsimple.json.objectmodel.JsonObject;
import jsimple.json.objectmodel.JsonObjectOrArray;
import jsimple.json.objectmodel.ObjectModelParser;
import jsimple.util.ArrayList;
import jsimple.util.List;

import com.special_friends.model.FacebookFriend;
/**
 * Parses jsons received from azure mobile server and constructs model objects
 */
public class AzureMobileParser {
	
	public List<FacebookFriend> parserFriendList(String json) {
		List<FacebookFriend> ret = new ArrayList<FacebookFriend>();
		ObjectModelParser parser = new ObjectModelParser(new StringReader(json));
		JsonObjectOrArray root = parser.parseRoot();
		JsonArray friendList = (JsonArray)root;
		for (int i = 0; i < friendList.size(); i++) {
			JsonObject strFriend = (JsonObject) friendList.get(i);
			String name = strFriend.getString("name");
			String pictureUrl = strFriend.getString("picURL");
			FacebookFriend f = new FacebookFriend(name, pictureUrl);
			ret.add(f);
		}

		return ret;
	}
	
	public List<String> parserFriendListIDs(String json) {
		List<String> ret = new ArrayList<String>();
		ObjectModelParser parser = new ObjectModelParser(new StringReader(json));
		JsonObjectOrArray root = parser.parseRoot();
		JsonArray friendList = (JsonArray)root;

		for (int i = 0; i < friendList.size(); i++) {
			JsonObject strFriend = (JsonObject) friendList.get(i);
			String id = strFriend.getString("id");
			ret.add(id);
		}

		return ret;
	}
}
