package com.sugarcrm.test.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.junit.*;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.skyscreamer.jsonassert.JSONAssert;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.ApiUtils;
import com.sugarcrm.sugar.api.RuntimeRecords;
import com.sugarcrm.sugar.api.UserTokens;
import com.sugarcrm.test.SugarTestApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ian Fleming A collection of tests that verify the use of utilized
 *         libs
 */
public class verifyFramework extends SugarTestApi {

	public void setup() throws FileNotFoundException, IOException,
			ParseException {
	}

	@Test
	/**
	 * Test Case testJson - Verifies the json data files and tests the json Libs being used
	 * 
	 * @throws FileNotFoundException, IOException,
			JSONException 
	 */
	public void testJsonFormat() throws FileNotFoundException, IOException,
			JSONException, ParseException {
		File folder = new File("src/test/resources/apidata");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().endsWith(".json")) {
				try {
					new JSONParser().parse(new FileReader(file));
				} catch (ParseException e) {
					fail("No Json " + file);
					e.printStackTrace();
				}

			}
		}

		// TimePeriods?filter[0][end_date][$gte]:=2014-09-11&filter[1][start_date][$lte]:=2014-09-11&filter[0][type][$contains]=Quarter

		// Basic JSONAssert commands being used in validation
		String result = "{id:1,name:\"Juergen\"}";
		JSONAssert.assertEquals("{id:1}", result, false); // Pass
		JSONAssert.assertNotEquals("{id:2}", result, false); // Pass

		// Set the Rest Assured API global environment variables
		// RestAssured.basePath = "";
		// Response response = null;
		// To use the Burp proxy
		// System.setProperty("http.proxyHost", "");
		// System.setProperty("http.proxyPort", "");

		// System.setProperty("http.proxyHost", "localhost");
		// System.setProperty("http.proxyPort", "8080");

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");

		SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");

		String myDateOnly = formatterDate.format(currentDate.getTime());
		VoodooUtils.voodoo.log.info("My Date ONLY = " + myDateOnly);

		String myDate = formatter.format(currentDate.getTime());
		VoodooUtils.voodoo.log.info("My Date = " + myDate);

		currentDate.add(Calendar.DATE, 1);
		myDate = formatter.format(currentDate.getTime());
		int offset = Integer.parseInt("1");
		VoodooUtils.voodoo.log.info("My Date + offset = " + myDate);

		currentDate = Calendar.getInstance();
		offset = Integer.parseInt("-1");
		currentDate.add(Calendar.DATE, offset);
		myDate = formatter.format(currentDate.getTime());
		VoodooUtils.voodoo.log.info("My Date - 1 = " + myDate);

		VoodooUtils.voodoo.log.info("BEFORE ");
		Pattern p = Pattern
				.compile("<input type=\\\"hidden\\\" name=\\\"record\\\" value=\\\"(.*)\\\".*");
		Matcher m = p
				.matcher("xxxx <input type=\"hidden\" name=\"record\" value=\"fc780bcc-8f2a-5f26-9a73-54b80b1a1b3f\">");
		if (m.find()) {
			VoodooUtils.voodoo.log.info("AFTER " + m.group(1));
		}

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
		// BWC PHP Forms
		HttpPost postRequest = new HttpPost(baseURI + basePath
				+ "/oauth2/bwc/login");
		// Un-comment for debugging via proxy
		// postRequest.setConfig(config);
		ApiUtils myUtils = new ApiUtils();
		ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
		ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

		// Login as Admin and get the Authentication Token
		String oauthToken = myUtils.userLogin("loginAdmin.json",
				UserTokensArray);

		postRequest.addHeader("OAuth-Token", oauthToken);

		HttpResponse myResponse = httpClient.execute(postRequest);

		HttpEntity entity = myResponse.getEntity();

		String responseString = EntityUtils.toString(entity, "UTF-8");
		System.out.println("Here is response - " + responseString);

		if (myResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ myResponse.getStatusLine().getStatusCode());
		}

		postRequest = new HttpPost(baseURI + "rest/v10/oauth2/token");

		JSONObject jsonObject = myUtils.getJson("loginAdmin.json");

		StringEntity myEntity = new StringEntity(jsonObject.toString(), "UTF-8");

		postRequest.setEntity(myEntity);

		myResponse = httpClient.execute(postRequest);

		entity = myResponse.getEntity();

		responseString = EntityUtils.toString(entity, "UTF-8");
		System.out.println("Here is response for Login as Admin- " + responseString);

	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
	}
}
