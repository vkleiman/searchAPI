package com.sky;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.*;

import java.io.*;

public class SearchAPITest {

	//run get request, check status code, return body
	public String runGetRequest(String url, int expStatus) throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		
		try {
		    int statusCode = response1.getStatusLine().getStatusCode();
		    System.out.println("code =" + statusCode);
		    HttpEntity entity1 = response1.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    InputStream is = entity1.getContent();
		    String body = convertStreamToString(is);
		    System.out.println("body =" + body);
		    EntityUtils.consume(entity1);
		    
		    //check status code is as expected
		    assertTrue(statusCode == expStatus);
		    return body;
		} finally {
		    response1.close();
		}

	}
	
	//copied from stack overflow
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	//check resultCount is as expected, specify type of comparison, eq, gt, lt
	private static void checkCount(int expCount, String body, String type) throws JSONException {
		JSONObject jo = new JSONObject(body);
		int resCount = jo.getInt("resultCount");
		System.out.println("count=" + resCount);
		
		//do various types of comparisons
		if(type.equals("eq"))
			assertTrue(resCount == expCount);
		else if(type.equals("gt"))
			assertTrue(resCount > expCount);
		else if(type.equals("lt"))
			assertTrue(resCount < expCount);
	}
	
	@Test
    public void testHappyPath() throws ClientProtocolException, IOException, JSONException {
		String body = runGetRequest("https://itunes.apple.com/search?term=jack+johnson", 200);
		checkCount(50, body, "eq");
	}
	
	@Test
    public void testLimit() throws ClientProtocolException, IOException, JSONException {
		String body = runGetRequest("https://itunes.apple.com/search?term=jack+johnson&limit=25", 200);
		checkCount(25, body, "eq");
	}
	@Test
    public void testBadCountry() throws ClientProtocolException, IOException {
		String body = runGetRequest("https://itunes.apple.com/search?term=jack+johnson&country=ZZ", 400);
	}
	
	//other tests can be easily added as well
}
