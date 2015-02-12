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

import goodfriends.util.Constants;
import jsimple.io.BufferedReader;
import jsimple.io.Directory;
import jsimple.io.File;
import jsimple.io.InputStream;
import jsimple.io.OutputStream;
import jsimple.io.Paths;
import jsimple.io.Utf8InputStreamReader;
import jsimple.oauth.model.Token;
import jsimple.util.BasicException;

public class LocalFileConnector {
  private Directory dataDir;

  public LocalFileConnector() {
    dataDir = Paths.getInstance().getApplicationDataDirectory();
  }

  public String selectFriends(String appUserID) {
    File dataFile = dataDir.getFile(appUserID);
    if (!dataFile.exists()) {
      System.out.println("Local file does not exist");
      return "";
    }

    InputStream is = dataFile.openForRead();
    BufferedReader br = new BufferedReader(new Utf8InputStreamReader(is));
    StringBuilder jsonBuilder = new StringBuilder();
    String line = "";
    while ((line = br.readLine()) != null) {
      jsonBuilder.append(line);
    }
    br.close();
    return jsonBuilder.toString();
  }

  public void saveFriends(String friendsJson,String appUserID){
    File dataFile = dataDir.getFile(appUserID);
    if (dataFile != null) {
      dataFile.delete();
    }
    OutputStream fileOS = dataFile.openForCreate();
    fileOS.writeLatin1EncodedString(friendsJson);
    fileOS.flush();
    fileOS.close();
  }

  public void saveToken(Token token){
    File dataFile = dataDir.getFile(Constants.LOCAL_TOKEN_STORAGE);

    if (dataFile != null) {
      dataFile.delete();
    }

    OutputStream fileOS = dataFile.openForCreate();
    fileOS.writeLatin1EncodedString(token.getTokenString());
    fileOS.flush();
    fileOS.close();
  }

  public String loadToken(){
    File dataFile = dataDir.getFile(Constants.LOCAL_TOKEN_STORAGE);
    if (!dataFile.exists()) {
      throw new BasicException("Can't load acess token from file , file does not exist , use option 2 to auth for the first time");
    }

    StringBuilder rawData = new StringBuilder();
    String line = "";
    InputStream is = dataFile.openForRead();
    BufferedReader br = new BufferedReader(new Utf8InputStreamReader(is)); 
    while ((line = br.readLine()) != null) {
      rawData.append(line);
    }
    br.close();

    return rawData.toString();
  }
}
