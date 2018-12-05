package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.response.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.*;
import com.sugarcrm.test.SugarTestApi;

public class globalSearchPart3 extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Some debug code to verify the state of the local Elastic Search set
		// up
		// Automatically follows redirects with the returned location being
		// processed
		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setRedirectStrategy(new LaxRedirectStrategy()).build();

		HttpGet getRequest = new HttpGet(
				"http://fts-test2.sjc.sugarcrm.pvt:9200/_cluster/stats?human&pretty");
		// Un-comment for debugging via proxy
		// postRequest.setConfig(config);
		HttpResponse myResponse = httpClient.execute(getRequest);
		if (myResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ myResponse.getStatusLine().getStatusCode());
		}

		HttpEntity entity = myResponse.getEntity();
		String content = EntityUtils.toString(entity);

		httpClient.close();

		VoodooUtils.voodoo.log.info("ELASTIC SEARCH STATS BEGIN " + content);

		// End of Debug code

		// Login as Admin and get the Authentication Token
		jsonObject = myUtils.getJson("loginAdmin.json");
		response = given().body(jsonObject).expect().statusCode(200).when()
				.post("oauth2/token");
		oauthToken = response.path("access_token");
		UserTokens userToken = new UserTokens("admin", oauthToken);
		UserTokensArray.add(userToken);
		//Create 24 Accounts to perform searches on
		int x = 1;
		while (x < 25) {
			// Calls a standard loop to process the common json test driven
			// format
			myUtils.standardTestDriver("rest/base/createAccount.json",
					UserTokensArray);
			x++;
		}
	}
	@Test
	/**
	 * Verifies the global search with max_num filter and offset
	 * 
	 * @throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException
	 */
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException,
			InterruptedException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to Create records to search
		//myUtils.standardTestDriver("createGlobalSearchPart2.json",
				//UserTokensArray);
		// The Elastic Search needs time to populate before validation
		Thread.sleep(15000);
		// Some debug code to verify the state of the local Elastic Search set
		// up
		// Automatically follows redirects with the returned location being
		// processed
		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setRedirectStrategy(new LaxRedirectStrategy()).build();

		HttpGet getRequest = new HttpGet(
				"http://fts-test2.sjc.sugarcrm.pvt:9200/_cluster/stats?human&pretty");
		// Un-comment for debugging via proxy
		// postRequest.setConfig(config);
		HttpResponse myResponse = httpClient.execute(getRequest);
		if (myResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ myResponse.getStatusLine().getStatusCode());
		}

		HttpEntity entity = myResponse.getEntity();
		String content = EntityUtils.toString(entity);

		httpClient.close();

		VoodooUtils.voodoo.log.info("ELASTIC SEARCH STATS DURING " + content);

		// End of Debug code
		// Calls a standard loop to Verify search terms on the created records
		myUtils.standardTestDriver("globalSearchPart3.json", UserTokensArray);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException, InterruptedException {
		// Delete all records that were created in this test
		JSONArray myModuleArray;
		jsonObject = myUtils.getJson("globalSearchPart3.json");
		myModuleArray = (JSONArray) jsonObject.get("modules_used");
		myUtils.deleteRecords(myModuleArray, UserTokensArray);

		Thread.sleep(15000);

		// Some debug code to verify the state of the local Elastic Search set
		// up
		// Automatically follows redirects with the returned location being
		// processed
		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setRedirectStrategy(new LaxRedirectStrategy()).build();

		HttpGet getRequest = new HttpGet(
				"http://fts-test2.sjc.sugarcrm.pvt:9200/_cluster/stats?human&pretty");
		// Un-comment for debugging via proxy
		// postRequest.setConfig(config);
		HttpResponse myResponse = httpClient.execute(getRequest);
		if (myResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ myResponse.getStatusLine().getStatusCode());
		}

		HttpEntity entity = myResponse.getEntity();
		String content = EntityUtils.toString(entity);

		httpClient.close();

		VoodooUtils.voodoo.log.info("ELASTIC SEARCH STATS END " + content);

		// End of Debug code
	}

}
