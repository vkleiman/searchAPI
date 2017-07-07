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

	/*
	 * Make the request in SearchAPIRequest class
	 * in this class we just implement the tests
	 */
	
	//check resultCount is as expected, specify type of comparison, eq, gt, lt
	private static void checkCount(int expCount, int resCount, String type) throws JSONException {
		
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
		SearchAPIRequest req = new SearchAPIRequest();
		req.runGetRequest("https://itunes.apple.com/search?term=jack+johnson");
		assertEquals("wrong status code", 200, req.getStatusCode());
		checkCount(50, req.getCountItem(), "eq");
	}
	
	@Test
    public void testLimit() throws ClientProtocolException, IOException, JSONException {
		SearchAPIRequest req = new SearchAPIRequest();
		req.runGetRequest("https://itunes.apple.com/search?term=jack+johnson&limit=25");
		assertEquals("wrong status code", 200, req.getStatusCode());
		checkCount(25, req.getCountItem(), "eq");
	}
	
	@Test
    public void testBadCountry() throws ClientProtocolException, IOException {
		SearchAPIRequest req = new SearchAPIRequest();
		req.runGetRequest("https://itunes.apple.com/search?term=jack+johnson&country=ZZ");
		assertEquals("wrong status code", 400, req.getStatusCode());
	}
	
	//other tests can be easily added as well
}
