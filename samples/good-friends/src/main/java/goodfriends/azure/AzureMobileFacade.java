package goodfriends.azure;

import goodfriends.model.FacebookFriend;
import goodfriends.util.Constants;
import jsimple.util.ArrayList;
import jsimple.util.List;

public class AzureMobileFacade {
    private AzureMobileConnector connector;
    private AzureMobileParser parser;

    public AzureMobileFacade() {
        connector = new AzureMobileConnector();
        parser = new AzureMobileParser();
    }

    public void saveFriends(List<FacebookFriend> friends, String appUserID) {
        //delete existing friends for this user
        String existingFriends = connector.selectFriends(Constants.AZURE_FRIEND_TABLE_NAME, appUserID);
        List<String> friendIDs = parser.parserFriendListIDs(existingFriends);
        for (String id : friendIDs) {
            connector.deleteData(Constants.AZURE_FRIEND_TABLE_NAME, id);
        }

        //save new friend list
        for (FacebookFriend friend : friends) {
            connector.saveData(Constants.AZURE_FRIEND_TABLE_NAME, friend.toJson(appUserID));
        }
    }

    public ArrayList<FacebookFriend> loadFriends(String appUserID) {
        String friendsJson = connector.selectFriends(Constants.AZURE_FRIEND_TABLE_NAME, appUserID);
        return parser.parserFriendList(friendsJson);
    }
}
