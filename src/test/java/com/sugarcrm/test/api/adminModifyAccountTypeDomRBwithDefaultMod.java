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
 * This test creates a Role (Role1) with associated User and then modifies the
 * account_type_dom for Role1 and verifies the meta data is correct for the
 * associated user. Following this the Default account_type_dom is changed and
 * another Role Based account_type_dom is made (Role2). Verification is then
 * made that Role1 is unchanged and Role2 includes the changes to the default
 * account_type_dom. Finally, when the default LOV DOM is changed to the
 * original, the RB LOV DOMs are also updated
 * 
 */
public class adminModifyAccountTypeDomRBwithDefaultMod extends SugarTestApi {
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
		// Verify the Account Type DOM layout
		myUtils.standardTestDriver("rest/base/verifyAccountTypeDom.json",
				UserTokensArray);
		// Create specific users for this test
		myUtils.standardTestDriver("rest/common/createUsers5Level3.json",
				UserTokensArray, recordIDs);
		// Create 2 Roles
		myUtils.standardTestDriver("rest/base/admin2RolesCreate.json",
				UserTokensArray, recordIDs);
		// Link the User to the Role
		myUtils.standardTestDriver("rest/base/adminRolesLink2Users.json",
				UserTokensArray, recordIDs);
		// Login as Sales Reps to get the Users Authentication Token
		oauthToken = myUtils.userLogin("rest/common/loginsalesRep1.json",
				UserTokensArray);
		oauthToken = myUtils.userLogin("rest/common/loginsalesRep2.json",
				UserTokensArray);
		// Modify the Role Based LOV and Log in as the associated User to verify
		// the correct Meta data
		myUtils.standardTestDriver(
				"rest/base/adminModifyAccountTypeDomRB.json", UserTokensArray,
				recordIDs);
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify the expected Default account type DOM for validity of the
		// test
		myUtils.standardTestDriver("rest/base/adminAccountTypeDomVerify.json",
				UserTokensArray);

		// Modify the Default LOV for account_type,
		myUtils.standardTestDriver("rest/base/adminModifyAccountTypeDom.json",
				UserTokensArray);

		// Create a new modified Role and verify that the original Role LOV is
		// unchanged whilst the new Role LOV contains the modifications to the
		// Default LOV
		myUtils.standardTestDriver(
				"rest/base/adminModifyAccountTypeDomRBwithDefaultMod.json",
				UserTokensArray, recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete the Custom Role and Users created during this test - will also
		// delete the Custom RB LOV
		myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
				UserTokensArray, recordIDs);
		// Restore Account Record Layout to default
		myUtils.standardTestDriver("rest/base/adminRestoreAccountTypeDom.json",
				UserTokensArray);
		// Verify the Account Type DOM layout
		myUtils.standardTestDriver("rest/base/verifyAccountTypeDom.json",
				UserTokensArray);
		myUtils.standardTestDriver("rest/common/delete2DefaultRoles.json",
            UserTokensArray, recordIDs);
	}

}
