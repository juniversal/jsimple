package main.java.com.special_friend.local;

import jsimple.oauth.model.Token;
import jsimple.util.List;
import main.java.com.special_friend.azure.AzureMobileParser;
import main.java.com.special_friend.model.FacebookFriend;

public class LocalFileFacade {
    private LocalFileConnector connector;
    

    public LocalFileFacade() {
    	connector = new LocalFileConnector();
    }
    
    /** Upon file serialization a file named by the unique user's id is creaded. Search if the file exists , load it ,
     * and parse friends . Since the azure mobile jsons are the same as the local-file jsons , we can use an azure
     * parser to extract the model objects
     * @return list of close friends
     */
    public List<FacebookFriend> selectFriends(String appUserId) {
    	String friendsJson = connector.selectFriends(appUserId);
        //parses same as as azure field
        AzureMobileParser azureParser = new AzureMobileParser();
        List<FacebookFriend> friendList = azureParser.parserFriendList(friendsJson);
        return friendList;

    }

    public void saveFriends(List<FacebookFriend> specialFriends,String appUserId) {
        StringBuilder outputJson = new StringBuilder();
        outputJson.append("[");
        for (FacebookFriend facebookFriend : specialFriends) {
            outputJson.append(facebookFriend.toJson(appUserId));
            outputJson.append(",");
        }
        outputJson.deleteCharAt(outputJson.length() - 1);
        outputJson.append("]");

        String json = outputJson.toString();
        connector.saveFriends(json, appUserId);
    }

    public void saveToken(Token token){
    	connector.saveToken(token);
    }
    
    public Token loadTokenLocally(){
    	String token = connector.loadToken();
        Token ret  = new Token(token.toString(), "");
        return ret;
    }
}
