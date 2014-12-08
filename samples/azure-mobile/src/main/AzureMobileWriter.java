import jsimple.net.HttpRequest;
import jsimple.net.HttpResponse;

/**
 * @author Dorin Suletea
 * @since 12/5/2014
 */
public class AzureMobileWriter {

	/*uses the app id to instert data into Table1*/
	public static void insertDataWithAppID() throws Exception {
		HttpRequest req = HttpRequest.create(Constants.DB_PATH);
		
		req.setMethod(HttpRequest.METHOD_POST);
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-APPLICATION", Constants.AZURE_APP_ID);// <--- the azure-mobile app id
		
		//request body contains the json for the new data to be inserted
		//currently hardcoded json is "{"A":"5555","B":"22222"}"
		// Table1 structure is 
		//
		// ID: int (autoincrement)(primary key)
		// A: varchar
		// B: varchar
		jsimple.io.Utf8OutputStreamWriter out = new jsimple.io.Utf8OutputStreamWriter(req.createRequestBodyStream());
		String reqStr = "{\"A\":\"5555\",\"B\":\"22222\"}";

		out.write(reqStr);
		out.flush();
		out.close();
		
		HttpResponse resp = req.send();
		
		System.out.println(resp.getStatusCode());
	}
}
