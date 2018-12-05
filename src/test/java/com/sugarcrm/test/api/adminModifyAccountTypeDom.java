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
 * This is a basic smoke test to modify the Account Type DOM DropDown in Admin
 * 
 */
public class adminModifyAccountTypeDom extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
		// Verify the initial Account Type DOM layout
		myUtils.standardTestDriver("rest/base/verifyAccountTypeDom.json",
				UserTokensArray);
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify the expected Default account type DOM for validity of the
		// test
		myUtils.standardTestDriver("rest/base/adminAccountTypeDomVerify.json",
				UserTokensArray);

		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver("rest/base/adminModifyAccountTypeDom.json",
				UserTokensArray);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Restore Account Record Layout to default and verify the default
		// layout
		myUtils.standardTestDriver("rest/base/adminRestoreAccountTypeDom.json",
				UserTokensArray);
		myUtils.standardTestDriver("rest/base/verifyAccountTypeDom.json",
				UserTokensArray);
	}

}
