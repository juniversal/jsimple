import jsimple.io.IOUtils;
import jsimple.io.StringReader;
import jsimple.json.objectmodel.JsonObject;
import jsimple.json.objectmodel.JsonObjectOrArray;
import jsimple.json.objectmodel.ObjectModelParser;
import jsimple.net.HttpRequest;

/**
 * @author Dorin Suletea
 * @since 12/5/2014
 */
public class AzureAuthenticator {
	/**
	 * Sends to Azure-mobile server the Facebook acess_token as a json object,
	 * extracts from the response the azure access token.
	 * This token can be used as authentication for all azure-mobile related services
	 * Compatible with Facebook ; Google ; Azure Active Directory
	 * 
	 * @param facebookAcessToken = facebook acess_token
	 */
	public static String obtainAzureToken(String facebookAcessToken) {
        HttpRequest req = jsimple.net.HttpRequest.create(Constants.FB_AUTH_URL);

        req.setMethod(HttpRequest.METHOD_POST);
        req.setContentTypeAndAcceptHeaders("application/json", "application/json");
        jsimple.io.Utf8OutputStreamWriter out = new jsimple.io.Utf8OutputStreamWriter(req.createRequestBodyStream());
        
        String reqStr = "{\"access_token\":\"" + facebookAcessToken + "\"}";
        out.write(reqStr);
        out.flush();
        out.close();

        jsimple.net.HttpResponse resp = req.send();
        String azureAuthData = IOUtils.toStringFromUtf8Stream(resp.getBodyStream());

        
        //parse the response
        ObjectModelParser parser = new jsimple.json.objectmodel.ObjectModelParser(new StringReader(azureAuthData));
		JsonObjectOrArray root = parser.parseRoot();
		JsonObject rootObj = (JsonObject) root;
		
		String azureAuthToken = rootObj.getString(Constants.JSON_NAME_AZURE_MOBILE_TOKEN);
		return azureAuthToken;
    }
}
