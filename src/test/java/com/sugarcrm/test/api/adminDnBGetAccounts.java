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
import com.sugarcrm.sugar.api.modules.RolesModule;
import com.sugarcrm.test.SugarTestApi;

/**
 * This is a basic smoke test to verify the DnB retrieval of Account information
 * 
 */
public class adminDnBGetAccounts extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	RolesModule myRolesModule = new RolesModule();
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

	@Override
   public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
		
		myUtils.standardTestDriver("rest/base/adminDnBSetUp.json",
				UserTokensArray, recordIDs);
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		myUtils.standardTestDriver("rest/base/adminDnBGetAccounts.json",
				UserTokensArray, recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
	}

}
