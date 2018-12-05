package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.response.Response;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.*;
import com.sugarcrm.test.SugarTestApi;

public class nonAdminAccessPart1 extends SugarTestApi {
	String nonAdminUserID;
	String adminUserID;
	JSONObject jsonObject;
	String oauthToken;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException {

		// Log in as Admin to get token to create Non Admin
		jsonObject = myUtils.getJson("loginAdmin.json");
		response = given().body(jsonObject).expect().statusCode(200).when()
				.post("oauth2/token");
		oauthToken = response.path("access_token");
		adminUserID = oauthToken;
		
		UserTokens userToken = new UserTokens("admin", oauthToken);
		UserTokensArray.add(userToken);

		// Create a non-admin user
		jsonObject = myUtils.getJson("rest/base/common/createNonAdminUser.json");
		response = given().header("OAuth-Token", oauthToken).body(jsonObject)
				.expect().statusCode(200).when().post("Users");
		nonAdminUserID = response.path("id");

		// Login as non-admin user
		jsonObject = myUtils.getJson("loginNonAdminUser.json");
		response = given().body(jsonObject).expect().statusCode(200).when()
				.post("oauth2/token");
		oauthToken = response.path("access_token");
		UserTokens userTokenQA = new UserTokens("nonadminuser10", oauthToken);
		UserTokensArray.add(userTokenQA);

	}
	@Test
	/**
	 * Test Case Non Admin access as documented in the json data file nonAdminAccessPart1
	 * 
	 * @throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException
	 */
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver("nonAdminAccessPart1.json", UserTokensArray);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete the Users and private Teams that has been created
		
		response = given().header("OAuth-Token", adminUserID).expect()
				.statusCode(200).when().delete("Users/" + nonAdminUserID);
		
		myUtils.deleteTeam("nonadminfirst", adminUserID);
	}

}
