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
 * This test creates a Role with associated Mobile User and then modifies the
 * Mobile Account Record Layout for the given role and verifies the mobile meta
 * data is correct for the associated user. In addition the test verifies that
 * if Mobile Default Layout is changed the RBV layout is unchanged
 * 
 */
public class mobileModifyAccountRecordRBVwithDefaultMod extends SugarTestApi {
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
		// Create a Role to Link to the User
		myUtils.standardTestDriver("rest/base/adminRolesCreate.json",
				UserTokensArray, recordIDs);
		// Link the User to the Role
		myUtils.standardTestDriver("rest/base/adminRolesLinkUsers.json",
				UserTokensArray, recordIDs);
		// Modify the Role Based Record Layout and Log in as the associated User
		// to verif the correct Meta data
		myUtils.standardTestDriver(
				"rest/base/mobileModifyAccountRecordRBV.json", UserTokensArray,
				recordIDs);
	}

	@Test
	@Ignore
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Change the Default Mobile Record Layout and verify the RBV layout has
		// not
		// changed
		myUtils.standardTestDriver(
				"rest/base/mobileAdminAccountsRecordLayout.json",
				UserTokensArray);

		myUtils.standardTestDriver(
				"rest/base/mobileModifyAccountRecordRBVwithDefaultMod.json",
				UserTokensArray, recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete the Custom Role and Users created during this test - will also
		// delete the Custom RBV
		myUtils.standardTestDriver("rest/common/deleteDefaultRole.json",
				UserTokensArray, recordIDs);
		// Restore Account Record Layout to default, do this before deleting the
		// users
		myUtils.standardTestDriver(
				"rest/base/restoreMobileAdminAccountsRecordLayout.json",
				UserTokensArray);
		// Verify the initial default meta data in order to validate the test
		// itself
		myUtils.standardTestDriver(
				"rest/base/verifyMobileAdminAccountsRecordLayout.json",
				UserTokensArray);
		myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
				UserTokensArray, recordIDs);
	}

}
