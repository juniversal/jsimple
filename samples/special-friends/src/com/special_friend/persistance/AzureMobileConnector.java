package com.special_friend.persistance;

import com.special_friend.facebook.Constants;
import com.special_friends.model.ApplicationModelEndpoint;

import jsimple.io.IOUtils;
import jsimple.net.HttpRequest;
import jsimple.net.HttpResponse;
import jsimple.util.BasicException;
import jsimple.util.HashMap;

public class AzureMobileConnector {

	public void saveData(String table, HashMap<String, String> dataToInsert) {
		HttpRequest req = HttpRequest.create(Constants.AZURE_DB_PATH + table);
		req.setMethod(HttpRequest.METHOD_POST);
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-APPLICATION", Constants.AZURE_APP_ID);

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
		System.out.println(reqStr.toString());
		out.write(reqStr.toString());
		out.flush();
		out.close();

		HttpResponse resp = req.send();

		if (resp.getStatusCode() != 201) {
			throw new BasicException(resp.getStatusMessage());
		}
	}

	public void saveData(String table, String json) {
		HttpRequest req = HttpRequest.create(Constants.AZURE_DB_PATH + table);
		req.setMethod(HttpRequest.METHOD_POST);
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-APPLICATION", Constants.AZURE_APP_ID);

		jsimple.io.Utf8OutputStreamWriter out = new jsimple.io.Utf8OutputStreamWriter(req.createRequestBodyStream());

		out.write(json);
		out.flush();
		out.close();

		HttpResponse resp = req.send();
		if (resp.getStatusCode() != 201) {
			throw new BasicException(resp.getStatusMessage());
		}
	}
	
	
	public void deleteData(String table, String id) {
		HttpRequest req = HttpRequest.create(Constants.AZURE_DB_PATH + table+"/"+id);
		req.setMethod(HttpRequest.METHOD_DELETE);
		
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-APPLICATION", Constants.AZURE_APP_ID);

		jsimple.io.Utf8OutputStreamWriter out = new jsimple.io.Utf8OutputStreamWriter(req.createRequestBodyStream());


		out.flush();
		out.close();
//		Pascu
		HttpResponse resp = req.send();
		if (resp.getStatusCode() != 204) {
			throw new BasicException(resp.getStatusMessage());
		}
	}
	
	public  String selectFriends(String tableName) {
		String requestString = Constants.AZURE_DB_PATH + tableName;
		String filterString = "?$filter=("   + Constants.MY_ID_COLUMN  + "%20eq%20" + "'" + ApplicationModelEndpoint.getInstance().getMyID() + "')";
		requestString += filterString;
		
		HttpRequest req = HttpRequest.create(requestString);
		req.setMethod(HttpRequest.METHOD_GET);
		req.setContentTypeAndAcceptHeaders("application/json", "application/json");
		req.setHeader("X-ZUMO-APPLICATION", Constants.AZURE_APP_ID);

		HttpResponse response = req.send();
		String tableData = IOUtils.toStringFromUtf8Stream(response.getBodyStream());
		return tableData;
	}
	
}
