package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.ApiUtils;
import com.sugarcrm.sugar.api.UserTokens;
import com.sugarcrm.test.SugarTestApi;

public class globalSearchPart2 extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();

	@Override
   public void setup() throws FileNotFoundException, IOException,
			ParseException {
		// Login as Admin and get the Authentication Token
		jsonObject = myUtils.getJson("loginAdmin.json");
		response = given().body(jsonObject).expect().statusCode(200).when()
				.post("oauth2/token");
		oauthToken = response.path("access_token");
		UserTokens userToken = new UserTokens("admin", oauthToken);
		UserTokensArray.add(userToken);
	}

	@Test
	@Ignore
	/**
	 * Verifies the global search end point
	 *  with the request pay load in the Body not the URL- part 2
	 * 
	 * @throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException
	 */
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException,
			InterruptedException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to Create records to search
		myUtils.standardTestDriver("createGlobalSearchPart2.json",
				UserTokensArray);
		// Calls a standard loop to Verify search terms on the created records
		myUtils.standardTestDriver("globalSearchPart2.json", UserTokensArray);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException, InterruptedException {
		// Delete all records that were created in this test
		JSONArray myModuleArray;
		jsonObject = myUtils.getJson("globalSearchPart2.json");
		myModuleArray = (JSONArray) jsonObject.get("modules_used");
		myUtils.deleteRecords(myModuleArray, UserTokensArray);
	}

}
