package com.sugarcrm.test.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.ApiUtils;
import com.sugarcrm.sugar.api.RuntimeRecords;
import com.sugarcrm.sugar.api.UserTokens;
import com.sugarcrm.test.SugarTestApi;

/**
 * This is a basic smoke test for the reading Activities for Create and Update
 * Accounts, Contacts and Opportunities
 * 
 */
public class activitiesPart1 extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

	@Override
   public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
		// Create a one off specific users for the Activities
		myUtils.standardTestDriver("rest/common/createActivityUser.json",
				UserTokensArray, recordIDs);
		// Login as the Activity User
		oauthToken = myUtils.userLogin("rest/common/loginActivityUser.json",
				UserTokensArray);
	}

	@Test
	@Ignore
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the prerequisites then the part 1 tests
		myUtils.standardTestDriver("rest/base/activitiesSmokeTest.json",
				UserTokensArray);
		myUtils.standardTestDriver("rest/base/activitiesPart1.json",
				UserTokensArray);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Users and Teams are explicitly deleted
		myUtils.standardTestDriver("rest/common/deleteActivityUser.json",
				UserTokensArray, recordIDs);
		// Delete all records that were created in this test
		myUtils.deleteModules("rest/base/activitiesPart1.json", UserTokensArray);
	}

}
