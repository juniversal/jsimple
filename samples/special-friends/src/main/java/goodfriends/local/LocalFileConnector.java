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
