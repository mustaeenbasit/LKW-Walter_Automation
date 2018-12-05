package com.sugarcrm.sugar.api.modules;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.api.*;
import static org.junit.Assert.*;

public class RenameModules {
	JSONObject jsonObjectValidate;
	JSONObject jsonObjectValues;
	JSONObject jsonObjectDriver;
	Object obj1 = null;
	ApiUtils myUtils = new ApiUtils();
	Response response;

	// Validates the contents of the returned Module Rename HTML in Admin Rename
	// Modules
	// This is used to validate the expected starting default Module Names as
	// well as validate the Changed Module Names
	public void ValidateModuleNames(String adminUserID, String testData)
			throws FileNotFoundException, IOException, ParseException {

		// Un-comment for debugging via proxy
		// HttpHost proxy = new HttpHost("localhost", 8080);
		// RequestConfig config =
		// RequestConfig.custom().setProxy(proxy).build();

		// The Values passed in the json file are validated
		jsonObjectValidate = myUtils.getJson(testData);
		// Need to read global variables for URL
		String baseURI = "";
		String basePath = "";
		try {
			baseURI = new SugarUrl().getBaseUrl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			basePath = new SugarUrl().getRestRelativeUrl();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Automatically follows redirects with the returned location being
		// processed
		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setRedirectStrategy(new LaxRedirectStrategy()).build();

		// This request will set the PHP Session ID in the Cookie, needed for
		// BWC PHP Forms requests
		HttpPost postRequest = new HttpPost(baseURI + basePath
				+ "/oauth2/bwc/login");
		// Un-comment for debugging via proxy
		// postRequest.setConfig(config);

		postRequest.addHeader("OAuth-Token", adminUserID);
		HttpResponse myResponse = httpClient.execute(postRequest);

		if (myResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ myResponse.getStatusLine().getStatusCode());
		}

		postRequest.releaseConnection();

		// This request will get the Rename Modules HTML
		HttpGet getRequest = new HttpGet(
				baseURI
						+ "index.php?action=wizard&module=Studio&wizard=StudioWizard&option=RenameTabs");
		// Un-comment for debugging via proxy
		// postRequest.setConfig(config);

		myResponse = httpClient.execute(getRequest);
		if (myResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ myResponse.getStatusLine().getStatusCode());
		}

		HttpEntity entity = myResponse.getEntity();

		JSONArray myJsonArraySingular = (JSONArray) jsonObjectValidate
				.get("validateModuleNames");
		Iterator<JSONObject> iteratorParms = myJsonArraySingular.iterator();
		//
		// Read the Singular Values and Validate via a regular Expression on the
		// returned HTML
		//
		String content = EntityUtils.toString(entity);
		String findSingular = "rename-label-singular\" id='.*' value='(.*)' onchange.*";
		Pattern myPattern = Pattern.compile(findSingular);
		Matcher matcher = myPattern.matcher(content);
		boolean found = false;

		while (matcher.find()) {
			found = true;
			jsonObjectValues = (JSONObject) iteratorParms.next();
			String mySingular = (String) jsonObjectValues.get("singular")
					.toString();
			assertEquals(mySingular, matcher.group(1));
		}

		if (!found) {
			System.out.print("No match found");
		}

		JSONArray myJsonArrayPlural = (JSONArray) jsonObjectValidate
				.get("validateModuleNames");
		iteratorParms = myJsonArrayPlural.iterator();
		//
		// Read the Plural Values and Validate vi a regular Expression on the
		// HTML
		//
		String findPlural = "rename-label-plural\" id='.*' value='(.*)' type='.*";
		myPattern = Pattern.compile(findPlural);
		matcher = myPattern.matcher(content);

		while (matcher.find()) {
			found = true;
			jsonObjectValues = (JSONObject) iteratorParms.next();
			String myPlural = (String) jsonObjectValues.get("plural")
					.toString();
			assertEquals(myPlural, matcher.group(1));
		}

		if (!found) {
			System.out.print("No match found");
		}

		getRequest.releaseConnection();
		httpClient.close();
	}

}
