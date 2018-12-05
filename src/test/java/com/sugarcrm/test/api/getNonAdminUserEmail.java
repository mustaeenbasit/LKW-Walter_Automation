package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.response.Response;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.*;
import com.sugarcrm.test.SugarTestApi;
/**
 * This test verifies that the email address details can be retrieved for a non-admin user
 * 
 */
public class getNonAdminUserEmail extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	String userMaxID;
	String userSallyID;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		jsonObject = myUtils.getJson("loginAdmin.json");
		response = given().body(jsonObject).expect().statusCode(200).when()
				.post("oauth2/token");
		oauthToken = response.path("access_token");
		adminUserID = oauthToken;
		UserTokens userToken = new UserTokens("admin", oauthToken);
		UserTokensArray.add(userToken);

		// Calls a standard loop to process the common json test driven format
		// This version is passed record IDs and is used for Creating specific
		// Userss
		myUtils.standardTestDriver("getNonAdminUserEmailSetUp.json",
				UserTokensArray, recordIDs);

		// Login as Non-Admin bob and get the Authentication Token
		jsonObject = myUtils.getJson("loginBob.json");
		response = given().body(jsonObject).expect().statusCode(200).when()
				.post("oauth2/token");
		oauthToken = response.path("access_token");
		userToken = new UserTokens("bob", oauthToken);
		UserTokensArray.add(userToken);

	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver("getNonAdminUserEmail.json", UserTokensArray, recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete all records that were created in this test via record ids
		JSONArray myModuleArray;
		jsonObject = myUtils.getJson("getNonAdminUserEmail.json");
		myModuleArray = (JSONArray) jsonObject.get("modules_used");
		myUtils.deleteRecords(myModuleArray, UserTokensArray);

		// Users and Teams are explicitly deleted

		myUtils.standardTestDriver("getNonAdminUserEmailCleanUp.json",
				UserTokensArray, recordIDs);

		// Because the Teams have been created in the background they are
		// deleted via the default Team Name
		myUtils.deleteTeam("bob", adminUserID);

	}

}
