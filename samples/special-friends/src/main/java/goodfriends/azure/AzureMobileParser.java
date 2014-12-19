package goodfriends.azure;

import goodfriends.model.FacebookFriend;
import jsimple.io.StringReader;
import jsimple.json.objectmodel.JsonArray;
import jsimple.json.objectmodel.JsonObject;
import jsimple.json.objectmodel.JsonObjectOrArray;
import jsimple.json.objectmodel.ObjectModelParser;
import jsimple.util.ArrayList;
import jsimple.util.List;

/**
 * Parses jsons received from azure mobile server and constructs model objects
 */
public class AzureMobileParser {

    /**
     * Json to Model object parser
     */
    public List<FacebookFriend> parserFriendList(String json) {
        List<FacebookFriend> ret = new ArrayList<FacebookFriend>();
        //create a new json parser and extract the root object
        ObjectModelParser parser = new ObjectModelParser(new StringReader(json));
        JsonObjectOrArray root = parser.parseRoot();
        //our root object is a list , so iterate over it and initialize the model of each friend
        JsonArray friendList = (JsonArray) root;
        for (int i = 0; i < friendList.size(); i++) {
            JsonObject strFriend = (JsonObject) friendList.get(i);
            //we are interested only in name and picURL columns of the friend table , the insertion date and other metadata are not relevant for us
            ret.add(new FacebookFriend(strFriend.getString("name"), strFriend.getString("picURL")));
        }

        return ret;
    }

    /**
     * Json to azure-mobile id parser Azure mobile handles delete operations by record id In order to delete users
     * friend list a string with all the ids of user's friends records must be provided. This extracts only the id's
     * from the input json
     */
    public List<String> parserFriendListIDs(String json) {
        List<String> ret = new ArrayList<String>();
        ObjectModelParser parser = new ObjectModelParser(new StringReader(json));
        JsonObjectOrArray root = parser.parseRoot();
        JsonArray friendList = (JsonArray) root;

        for (int i = 0; i < friendList.size(); i++) {
            JsonObject strFriend = (JsonObject) friendList.get(i);
            //we only need the id's of the records
            ret.add(strFriend.getString("id"));
        }

        return ret;
    }
}
