package com.sugarcrm.test.api;

import com.jayway.restassured.response.Response;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.*;
import com.sugarcrm.sugar.api.modules.*;
import com.sugarcrm.test.SugarTestApi;

/**
 * This test verifies that the User account is not locked after 1 failed login attempts if the number of failed attempts is set to 2
 * 
 */
public class adminLoginAttemptsOK extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	String userMaxID;
	String userSallyID;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ForecastsModule myForecastsModule = new ForecastsModule();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
		// Create specific users for this test
				myUtils.standardTestDriver("rest/common/createLoginAttemptsUser.json",
						UserTokensArray, recordIDs);
		
		// Login as User and get the Authentication Tokens
				oauthToken = myUtils.userLogin("rest/common/loginAttemptsUser.json",
						UserTokensArray);
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException, InterruptedException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login 2 times with valid login the second and should succeed
		myUtils.standardTestDriver("rest/base/adminLoginAttemptsOK.json",
				UserTokensArray, recordIDs);
		
		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Users and Teams are explicitly deleted
		myUtils.standardTestDriver("rest/common/deleteLoginAttemptsUser.json",
				UserTokensArray, recordIDs);
	}

}
