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

package goodfriends.local;

import goodfriends.model.FacebookFriend;
import jsimple.oauth.model.Token;
import jsimple.util.List;
import goodfriends.azure.AzureMobileParser;

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
