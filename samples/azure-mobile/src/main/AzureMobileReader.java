import jsimple.io.IOUtils;
import jsimple.net.HttpRequest;
import jsimple.net.HttpResponse;

/**
 * @author Dorin Suletea
 * @since 12/5/2014
 */
public class AzureMobileReader {

	/* select * from Table1 ; authenticates with the azure-application id (X-ZUMO-APPLICATION)*/
	public static String queryAzureWithAppID() {
		HttpRequest req = HttpRequest.create(Constants.DB_PATH);
		
		//assemble request 
		req.setMethod(HttpRequest.METHOD_GET);
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-APPLICATION", Constants.AZURE_APP_ID);//<--- the azure-mobile app id

		HttpResponse response = req.send();
		String tableData = IOUtils.toStringFromUtf8Stream(response.getBodyStream());

		System.out.println("Table1 data using azure-app-id " + tableData);
		return tableData;
	}

	/* select * from Table1 authenticates with microsoft-azure token, this token is obtained from
	 * facebook oauth token (see AzureAuthenticator)*/
	public static String queryAzureWithFbToken(String azureOauthToken) {
		jsimple.net.HttpRequest req = jsimple.net.HttpRequest.create(Constants.DB_PATH);
		
		//assemble request 
		req.setMethod(HttpRequest.METHOD_GET);
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-AUTH", azureOauthToken);//<--- the azure-mobile oauth TOKEN

		jsimple.net.HttpResponse response = req.send();

		String tableData = IOUtils.toStringFromUtf8Stream(response.getBodyStream());
		System.out.println("Table1 data using azure-token "+tableData);
		return tableData;
	}

}
