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
import com.sugarcrm.sugar.api.modules.RenameModules;
import com.sugarcrm.test.SugarTestApi;

/**
 * This test verifies renaming modules
 * 
 */
public class adminRenameModules extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	RenameModules myRenameModules = new RenameModules();
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
		// Verify the original state of Module Names to validate the test
		// results
		myRenameModules.ValidateModuleNames(adminUserID,
				"rest/base/adminRestoreModuleNames.json");
	}

	@Test
	@Ignore
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Rename Modules
		myUtils.standardTestDriver("rest/base/adminRenameModules.json",
				UserTokensArray, recordIDs);
		// Validate the Renamed Modules
		myRenameModules.ValidateModuleNames(adminUserID,
				"rest/base/adminRenameModules.json");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {

		// Rename the Modules back to their default and validate the default
		// names
		myUtils.standardTestDriver("rest/base/adminRestoreModuleNames.json",
		UserTokensArray, recordIDs);
		myRenameModules.ValidateModuleNames(adminUserID,
		"rest/base/adminRestoreModuleNames.json");
	}

}
