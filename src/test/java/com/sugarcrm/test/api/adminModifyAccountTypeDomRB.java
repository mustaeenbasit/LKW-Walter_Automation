package com.sugarcrm.test.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
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
 * This test creates a Role with associated User and then modifies the
 * account_type_dom for the given role and verifies the meta data is correct for
 * the associated user
 * 
 */
public class adminModifyAccountTypeDomRB extends SugarTestApi {
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
		// Create specific users for this test
		// Verify the initial Account Type DOM layout
		myUtils.standardTestDriver("rest/base/verifyAccountTypeDom.json",
				UserTokensArray);
		myUtils.standardTestDriver("rest/common/createUsers5Level3.json",
				UserTokensArray, recordIDs);
		// Create a Role to Link to the User
		myUtils.standardTestDriver("rest/base/adminRolesCreate.json",
				UserTokensArray, recordIDs);
		// Link the User to the Role
		myUtils.standardTestDriver("rest/base/adminRolesLinkUsers.json",
				UserTokensArray, recordIDs);
		// Login as Sales Rep to get the Authentication Token
		oauthToken = myUtils.userLogin("rest/common/loginsalesRep1.json",
				UserTokensArray);
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");


		// Modify the Role Based LOV and Log in as the associated User to verify
		// the correct Meta data
		myUtils.standardTestDriver(
				"rest/base/adminModifyAccountTypeDomRB.json", UserTokensArray,
				recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete the Custom Role and Users created during this test - will also
		// delete the Custom RB LOV
		myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
				UserTokensArray, recordIDs);
		// Verify the Account Type DOM layout
		myUtils.standardTestDriver("rest/base/verifyAccountTypeDom.json",
				UserTokensArray);
		myUtils.standardTestDriver("rest/common/deleteDefaultRole.json",
            UserTokensArray, recordIDs);
	}

}
