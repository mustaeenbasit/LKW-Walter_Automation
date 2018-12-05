package com.sugarcrm.test.api;

import com.jayway.restassured.response.Response;

import org.json.JSONException;
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
 * This is a basic smoke test to modify the Mobile Accounts edit record layout
 * 
 */
public class mobileAdminAccountsRecordLayout extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
		// Create specific users for this test
		myUtils.standardTestDriver("rest/common/createUsers5Level3.json",
				UserTokensArray, recordIDs);
		// Log in as a Mobile Users for later mobile meta data verification
		oauthToken = myUtils
				.userLogin("rest/mobile/common/loginMobileSalesRep1.json",
						UserTokensArray);
		oauthToken = myUtils
				.userLogin("rest/mobile/common/loginMobileSalesRep2.json",
						UserTokensArray);
		// Verify the initial default meta data in order to validate the test
		// itself
		myUtils.standardTestDriver(
				"rest/base/verifyMobileAdminAccountsRecordLayout.json",
				UserTokensArray);
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Modify the Mobile Account Edit layout and verify the meta data with a
		// mobile user
		myUtils.standardTestDriver(
				"rest/base/mobileAdminAccountsRecordLayout.json",
				UserTokensArray);

		// This pause is needed for the above POST to complete before the
		// restore is executed in cleanup
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Restore Account Record Layout to default, do this before deleting the
		// users
		myUtils.standardTestDriver(
				"rest/base/restoreMobileAdminAccountsRecordLayout.json",
				UserTokensArray);
		// Delete all records that were created in this test
		myUtils.deleteModules("rest/base/mobileAdminAccountsRecordLayout.json",
				UserTokensArray);
		// Users and Teams are explicitly deleted
		myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
				UserTokensArray, recordIDs);
	}

}
