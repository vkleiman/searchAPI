package com.sky;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.*;

import java.io.*;

public class SearchAPIRequest {

	private String body;
	private int statusCode;
	
	//run get request, check status code, return body
	public void runGetRequest(String url) throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		
		try {
		    statusCode = response1.getStatusLine().getStatusCode();
		    System.out.println("code =" + statusCode);
		    HttpEntity entity1 = response1.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    InputStream is = entity1.getContent();
		    String body = convertStreamToString(is);
		    System.out.println("body =" + body);
		    EntityUtils.consume(entity1);
		    
		    this.body = body;
		} finally {
		    response1.close();
		}

	}
	
	//copied from stack overflow
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}

	public int getCountItem() {
		if(body==null)
			return 0;
		
		try {
			JSONObject jo = new JSONObject(body);
			return jo.getInt("resultCount");
		} catch (JSONException e) {
			return 0;
		}
	}
	
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	


}
