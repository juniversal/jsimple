import jsimple.io.IOUtils;
import jsimple.net.HttpRequest;
import jsimple.net.HttpResponse;
import jsimple.util.HashMap;

/**
 * @author Dorin Suletea
 * @since 12/5/2014
 */
public class AzureMobileWriter {

	/**
	 * Inserts data into the specified table using appID (not the azure token)
	 * dataToInsert is a key-value mapping that represents what data to insert
	 * into what columns hashmap.key = column name hashmap.value = value to
	 * insert
	 * 
	 * Table1 structure is > ID: int (autoincrement)(primary key) A: varchar B:
	 * varchar Hence the dataToInsert will contain 2 pairs of data :
	 * (A,myAInsertData) and (B,myBinsertData)
	 * 
	 * @param table
	 *            the name of the table you want to insert into
	 * @param dataToInsert
	 *            field->value mapping
	 */
	public static boolean insertDataWithAppID(String table, HashMap<String, String> dataToInsert) {
		HttpRequest req = HttpRequest.create(Constants.DB_PATH + table);

		req.setMethod(HttpRequest.METHOD_POST);
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-APPLICATION", Constants.AZURE_APP_ID);// <--- the
																	// azure-mobile
																	// app id

		jsimple.io.Utf8OutputStreamWriter out = new jsimple.io.Utf8OutputStreamWriter(req.createRequestBodyStream());

		StringBuffer reqStr = new StringBuffer();

		// example reqStr = "{\"A\":\"5555\",\"B\":\"22222\"}";
		reqStr.append("{");
		for (String key : dataToInsert.keySet()) {
			reqStr.append("\"" + key + "\"");
			reqStr.append(":");
			reqStr.append("\"" + dataToInsert.get(key) + "\"");
			reqStr.append(",");
		}
		// remove the last comma
		reqStr.deleteCharAt(reqStr.length() - 1);

		reqStr.append("}");

		System.out.println("Insert request " + reqStr.toString());

		out.write(reqStr.toString());
		out.flush();
		out.close();

		HttpResponse resp = req.send();

		System.out.println("Insert resp code " + resp.getStatusCode());
		boolean sucess = false;
		if (resp.getStatusCode() == 201) {
			sucess = true;
		}
		return sucess;
	}
	
	/**
	 * Updates the row with the id specified,
	 * rows named with values from dataToInsert.keySet , will have their values set to dataToInsert.values
	 * 
	 * @param itemID the id of the item to update
	 * @param tableName 
	 * @param appId application id
	 * @param dataToInsert the column->value mappings for the values that should be updated
	 * @return
	 */
	public static boolean updateFieldFilteredWithAppID(Integer itemID , String tableName, String appId, HashMap<String, String> dataToInsert) {
		String requestString = Constants.DB_PATH + tableName + "/" + itemID ;
		HttpRequest req = HttpRequest.create(requestString);
		req.setMethod(HttpRequest.METHOD_POST);
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-APPLICATION", Constants.AZURE_APP_ID);
		//since java does not support PATCH, this will override the method verb 
		req.setHeader("x-http-method-override", "PATCH"); 

		jsimple.io.Utf8OutputStreamWriter out = new jsimple.io.Utf8OutputStreamWriter(req.createRequestBodyStream());
		StringBuffer reqStr = new StringBuffer();

		reqStr.append("{");
		for (String key : dataToInsert.keySet()) {
			reqStr.append("\"" + key + "\"");
			reqStr.append(":");
			reqStr.append("\"" + dataToInsert.get(key) + "\"");
			reqStr.append(",");
		}
		// remove the last comma
		reqStr.deleteCharAt(reqStr.length() - 1);
		reqStr.append("}");

		System.out.println("Insert request " + reqStr.toString());
		out.write(reqStr.toString());
		out.flush();
		out.close();

		HttpResponse resp = req.send();

		System.out.println("Update resp code " + resp.getStatusCode());
		boolean sucess = false;
		if (resp.getStatusCode() == 200) {
			sucess = true;
		}
		return sucess;
	}
}
