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
import com.sugarcrm.test.SugarTestApi;

/**
 * This test converts an Employee to a User
 * 
 */
public class adminEmployeeCreateUser extends SugarTestApi {
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
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create the Employee and verify via REST API on the User record
		myUtils.standardTestDriver("rest/base/adminEmployeeCreate.json",
				UserTokensArray, recordIDs);

		// Create a User from the Employee
		myUtils.standardTestDriver("rest/base/adminEmployeeCreateUser.json",
				UserTokensArray, recordIDs);

		// Login as Employee1 and get the Authentication Token
		oauthToken = myUtils.userLogin("rest/common/loginEmployee1.json",
				UserTokensArray);

		// Verify Employee1 can CRUD on Accounts
		myUtils.standardTestDriver("rest/base/crudAccountsEmployee1.json",
				UserTokensArray);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete the Created Employee from the Users
		myUtils.standardTestDriver("rest/common/deleteDefaultEmployee.json",
				UserTokensArray, recordIDs);
		// Delete all records that were created in this test
		myUtils.deleteModules("rest/base/crudAccountsEmployee1.json",
				UserTokensArray);
	}

}
