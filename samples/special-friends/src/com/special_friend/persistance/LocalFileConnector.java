package com.special_friend.persistance;

import jsimple.io.BufferedReader;
import jsimple.io.Directory;
import jsimple.io.File;
import jsimple.io.InputStream;
import jsimple.io.OutputStream;
import jsimple.io.Paths;
import jsimple.io.Utf8InputStreamReader;
import jsimple.util.ArrayList;
import jsimple.util.List;

import com.special_friend.parsers.AzureMobileParser;
import com.special_friends.model.ApplicationModelEndpoint;
import com.special_friends.model.FacebookFriend;

public class LocalFileConnector {
	private Directory dataDir;
	
	public LocalFileConnector() {
		dataDir = Paths.getInstance().getApplicationDataDirectory();
	}
	
	public List<FacebookFriend> loadData(){
		File dataFile = dataDir.getFile(ApplicationModelEndpoint.getInstance().getMyID());
		if (!dataFile.exists()){
			System.out.println("Local file does not exist");
			return new ArrayList<FacebookFriend>();
		}
		
		InputStream is = dataFile.openForRead();
		BufferedReader br = new BufferedReader(new Utf8InputStreamReader(is));
		StringBuilder jsonBuilder = new StringBuilder();
		String line ="";
		while ((line=br.readLine())!=null){
			jsonBuilder.append(line);
		}
		br.close();
		
		//parses same as as azure field
		AzureMobileParser fbParser = new AzureMobileParser();
		List<FacebookFriend> friendList = fbParser.parserFriendList(jsonBuilder.toString());
		return friendList;
		
	}
	
	public void saveData(List<FacebookFriend> specialFriends) {
		File dataFile = dataDir.getFile(ApplicationModelEndpoint.getInstance().getMyID());
		
		if (dataFile!=null){
			dataFile.delete();
		}
		
		StringBuilder outputJson = new StringBuilder();
		outputJson.append("[");
		for (FacebookFriend facebookFriend : specialFriends) {
			outputJson.append(facebookFriend.toJson());
			outputJson.append(",");
		}
		outputJson.deleteCharAt(outputJson.length() - 1);
		outputJson.append("]");
		
		String json = outputJson.toString();
		
		OutputStream fileOS = dataFile.openForCreate();
		fileOS.writeLatin1EncodedString(json);
		fileOS.close();
	}
	
	
}
