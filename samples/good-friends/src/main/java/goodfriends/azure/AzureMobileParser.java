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
    public ArrayList<FacebookFriend> parserFriendList(String json) {
        ArrayList<FacebookFriend> ret = new ArrayList<FacebookFriend>();
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
