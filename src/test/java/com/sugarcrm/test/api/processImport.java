package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.ApiUtils;
import com.sugarcrm.sugar.api.RuntimeRecords;
import com.sugarcrm.sugar.api.UserTokens;
import com.sugarcrm.test.SugarTestApi;

/**
 * This is a test for Process Definition import
 * 
 */
public class processImport extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

	@Override
   public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		jsonObject = myUtils.getJson("loginAdmin.json");
		response = given().body(jsonObject).expect().statusCode(200).when()
				.post("oauth2/token");
		oauthToken = response.path("access_token");
		UserTokens userToken = new UserTokens("admin", oauthToken);
		UserTokensArray.add(userToken);
		// Create specific users for this test
		myUtils.standardTestDriver("rest/common/createUsers1.json",
				UserTokensArray, recordIDs);
		// Login as Sales Reps and Managers and get the Authentication Tokens
		oauthToken = myUtils.userLogin("rest/common/loginUser1.json",
				UserTokensArray);
		oauthToken = myUtils.userLogin("rest/common/loginUser2.json",
				UserTokensArray);
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver("rest/base/processImport.json",
				UserTokensArray, recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete all records that were created in this test
		JSONArray myModuleArray;
		jsonObject = myUtils.getJson("rest/base/processImport.json");
		myModuleArray = (JSONArray) jsonObject.get("modules_used");
		myUtils.deleteRecords(myModuleArray, UserTokensArray);
		// Processes are deleted via REST to clean up all related Tables
		myUtils.standardTestDriver("rest/common/deleteProcess.json",
				UserTokensArray, recordIDs);
		// Users and Teams are explicitly deleted
		myUtils.standardTestDriver("rest/common/deleteUsers1.json",
				UserTokensArray, recordIDs);
	}
}
