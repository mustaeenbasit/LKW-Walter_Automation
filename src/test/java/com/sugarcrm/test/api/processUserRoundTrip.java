package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.response.Response;

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

/**
 * This is a test for Process create and execution, then One Way Assign and
 * Approval of a Static User to an Account Creation event is verified
 * 
 */
public class processUserRoundTrip extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
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
		UserTokens userToken = new UserTokens("admin", oauthToken);
		UserTokensArray.add(userToken);
		// Create specific users for this test
		myUtils.standardTestDriver("rest/common/createUsers5Level3.json",
				UserTokensArray, recordIDs);
		// Login as Sales Rep 1 to get the Authentication Token
		oauthToken = myUtils.userLogin("rest/common/loginsalesRep1.json",
				UserTokensArray);
		// Login as Sales Rep 2 to get the Authentication Token
		oauthToken = myUtils.userLogin("rest/common/loginsalesRep2.json",
				UserTokensArray);
		// Generate unique Client UIds for creating graphic objects
		for (int x = 0; x < 5; x = x + 1) {
			long millisecs = System.currentTimeMillis();
			String myStrmillisecs = "UIID" + x + millisecs;
			RuntimeRecords moduleID = new RuntimeRecords("ID", "UIID/", "",
					myStrmillisecs);
			recordIDs.add(moduleID);
			VoodooUtils.voodoo.log.info(moduleID + " ADDED");
		}
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to set up the basic template process for accept
		// reject
		myUtils.standardTestDriver(
				"rest/base/common/processUserApproveSetUp.json",
				UserTokensArray, recordIDs);
		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver("rest/base/processUserRoundTrip.json",
				UserTokensArray, recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete all records that were created in this test
		JSONArray myModuleArray;
		jsonObject = myUtils.getJson("rest/base/processUserRoundTrip.json");
		myModuleArray = (JSONArray) jsonObject.get("modules_used");
		myUtils.deleteRecords(myModuleArray, UserTokensArray);
		// Processes are deleted via REST to clean up all related Tables
		myUtils.standardTestDriver("rest/common/deleteProcess.json",
				UserTokensArray, recordIDs);
		// Users and Teams are explicitly deleted
		myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
				UserTokensArray, recordIDs);
	}

}
