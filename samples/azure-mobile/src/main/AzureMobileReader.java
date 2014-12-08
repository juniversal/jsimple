import jsimple.io.IOUtils;
import jsimple.net.HttpRequest;
import jsimple.net.HttpResponse;

/**
 * @author Dorin Suletea
 * @since 12/5/2014
 */
public class AzureMobileReader {

	/**
	 * select * from tableName authenticate using appID
	 * 
	 * @param tableName
	 *            what table to read
	 * @param appId
	 *            usses application id to authenticate
	 * @return json response data
	 */
	public static String selectAllAuthWithAppID(String tableName, String appId) {
		String requestString = Constants.DB_PATH + tableName;
		HttpRequest req = HttpRequest.create(requestString);
		req.setMethod(HttpRequest.METHOD_GET);
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-APPLICATION", appId);

		HttpResponse response = req.send();
		String tableData = IOUtils.toStringFromUtf8Stream(response.getBodyStream());
		return tableData;
	}

	/**
	 * select * from tableName authenticate using acesstoken
	 * 
	 * @param tableName
	 *            what table to read
	 * @param appId
	 *            usses application id to authenticate
	 * @return json response data
	 */
	public static String selectAllAuthWithToken(String tableName, String azureAcessToken) {
		jsimple.net.HttpRequest req = jsimple.net.HttpRequest.create(Constants.DB_PATH + tableName);

		req.setMethod(HttpRequest.METHOD_GET);
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-AUTH", azureAcessToken);

		jsimple.net.HttpResponse response = req.send();
		String tableData = IOUtils.toStringFromUtf8Stream(response.getBodyStream());
		return tableData;
	}

	/**
	 * select * from tableName where column = value
	 * authenticate using acesstoken
	 * 
	 * @param tableName
	 *            what table to read
	 * @param appId
	 *            usses application id to authenticate
	 * @return json response data
	 */
	public static String selectFilteredAuthWithAppID(String tableName, String appId, String column, String value) {
		String requestString = Constants.DB_PATH + tableName;
		String filterString = "?$filter=("   + column  + "%20eq%20" + "'" + value + "')";
		requestString += filterString;

		HttpRequest req = HttpRequest.create(requestString);
		req.setMethod(HttpRequest.METHOD_GET);
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-APPLICATION", appId);

		HttpResponse response = req.send();
		String tableData = IOUtils.toStringFromUtf8Stream(response.getBodyStream());
		return tableData;
	}

	// ?$filter=LastName%20eq%20'Smith'%20and%20FirstName%20eq%20'John'

}
