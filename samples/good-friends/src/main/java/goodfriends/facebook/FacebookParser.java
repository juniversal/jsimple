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
import jsimple.io.StringReader;
import jsimple.json.objectmodel.JsonArray;
import jsimple.json.objectmodel.JsonObject;
import jsimple.json.objectmodel.JsonObjectOrArray;
import jsimple.json.objectmodel.ObjectModelParser;
import jsimple.util.*;

/**
 * Parses jsons received from facebook and constructs model objects
 */
public class FacebookParser {

    /**
     * Create friends data object from the friend json received from facebook
     */
    public ArrayList<FacebookFriend> parserFriendList(String json) {
        ArrayList<FacebookFriend> ret = new ArrayList<FacebookFriend>();
        ObjectModelParser parser = new ObjectModelParser(new StringReader(json));
        JsonObjectOrArray root = parser.parseRoot();
        //when receiving data from facebook the all the information is in a json 
        //list called data 
        JsonObject rootObj = (JsonObject) root;
        JsonArray friendList = rootObj.getJsonArray("data");
        //itterate over the data list and extract relevant values for each item
        for (int i = 0; i < friendList.size(); i++) {
            JsonObject strFriend = (JsonObject) friendList.get(i);
            // the picture is a json object that contains a list of properties, the property that interests us is the picture url
            String profilePic = strFriend.getJsonObject("picture").getJsonObject("data").getString("url");
            //extract friend's name , this will be used as a primary key
            String name = strFriend.getString("name");
            String newName = replaceNonUTF(name);
            FacebookFriend f = new FacebookFriend(newName, profilePic);
            ret.add(f);
        }
        return ret;
    }

    /**
     * Creates a list of facebook posts from the json of the user's wall Post's attributes are nullable , except for the
     * poster's name
     */
    public List<FacebookPost> parseWall(String wallJson) {
        List<FacebookPost> ret = new ArrayList<FacebookPost>();
        ObjectModelParser parser = new ObjectModelParser(new StringReader(wallJson));
        JsonObjectOrArray root = parser.parseRoot();
        JsonObject rootObj = (JsonObject) root;
        JsonArray feeds = rootObj.getJsonArray("data");

        for (int i = 0; i < feeds.size(); i++) {
            JsonObject post = (JsonObject) feeds.get(i);
            String from = post.getJsonObject("from").getString("name");
            String fromUTF = replaceNonUTF(from);

            System.out.println(from);

            String story = null;
            String picture = null;
            String link = null;
            String description = null;
            String name = null;
            String message = null;

            if (post.containsKey("story")) {
                story = post.getString("story");
            }
            if (post.containsKey("message")) {
                message = post.getString("message");
            }
            if (post.containsKey("picture")) {
                picture = post.getString("picture");
            }
            if (post.containsKey("link")) {
                picture = post.getString("link");
            }
            if (post.containsKey("name")) {
                name = post.getString("name");
            }
            if (post.containsKey("description")) {
                description = post.getString("description");
            }

            FacebookPost postPOJO = new FacebookPost(fromUTF, story, picture, link, name, description, message);
            ret.add(postPOJO);

        }
        return ret;
    }

    /**
     * Parses current user  information(json) and extracts the user's unique facebook ID
     */
    public String extractMyId(String myDataJson) {
        ObjectModelParser parser = new ObjectModelParser(new StringReader(myDataJson));
        JsonObjectOrArray root = parser.parseRoot();
        JsonObject rootObj = (JsonObject) root;
        String myID = rootObj.getString("id");
        return myID;
    }

    /**
     * Parses current user  information(json) and extracts the user's name
     */
    public String extractMyName(String myDataJson) {
        ObjectModelParser parser = new ObjectModelParser(new StringReader(myDataJson));
        JsonObjectOrArray root = parser.parseRoot();
        JsonObject rootObj = (JsonObject) root;
        String myFirstName = rootObj.getString("first_name");
        String myLastName = rootObj.getString("last_name");
        return myFirstName + " " + myLastName;
    }

    /**
     * Replace character that can not be read (special characters , etc with _)
     */
    public String replaceNonUTF(String original) {
        return StringUtils.replaceAll(original, new MatchPattern() {
            @Override public boolean match(MatchBuilder mb) {
                return ! (mb.matchAsciiLetter() || mb.matchDigit() || mb.match('(', ')', '[', ']'));
            }
        }, "_");

        //return original.replaceAll("[^A-Za-z0-9()\\[\\]]", "_");
    }
    
    public String getNextPostPage(String wallJSon){
        ObjectModelParser parser = new ObjectModelParser(new StringReader(wallJSon));
        JsonObjectOrArray root = parser.parseRoot();
        JsonObject rootObj = (JsonObject) root;
        String nextPage = "";
        if (rootObj.containsKey("paging")){
	        JsonObject paging = rootObj.getJsonObject("paging");
	        nextPage = paging.getString("next");
        }else{
        	return "";
        }
        return nextPage;
    }
}
