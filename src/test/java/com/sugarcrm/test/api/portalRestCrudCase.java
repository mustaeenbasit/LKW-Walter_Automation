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
import com.sugarcrm.sugar.api.modules.*;
import com.sugarcrm.test.SugarTestApi;

public class portalRestCrudCase extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	PortalsModule myPortalsModule = new PortalsModule();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);

		// Enable the Portal and Set Portal Support Users Password
		myPortalsModule.enablePortal(adminUserID, recordIDs, UserTokensArray);

		// Create Account and Portal Contact
		myUtils.standardTestDriver("rest/portal/common/portalCreateContactMarleneRuffner1.json",
				UserTokensArray, recordIDs);

		// Login to Portal with the portal contact user
		oauthToken = myUtils.userLogin("rest/portal/common/loginPortalMarleneRuffner1.json", UserTokensArray);
	}

	@Test
	/**
	 * Test Case portalRestCrudCase: Verifies the calls for Create, Read, Update and Delete Cases in Portal
	 * 
	 * @throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException
	 */
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver("rest/portal/portalRestCrudCase.json", UserTokensArray,
				recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");

	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {

		// Disable the Portal
		myPortalsModule.disablePortal(adminUserID);

		// Delete all records that were created in this test 
		myUtils.deleteModules("rest/portal/portalRestCrudCase.json", UserTokensArray);

		// Users and Teams are explicitly deleted
		myUtils.standardTestDriver("rest/portal/common/deleteUsersPortal.json",
				UserTokensArray, recordIDs);

	}
}
