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
