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
 * This is a basic smoke test for the Creation, Read, Update and Delete of Contracts
 */
public class crudContracts extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	Response response;
	String adminUserID;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver("rest/base/crudContracts.json",
				UserTokensArray);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete all records that were created in this test
		myUtils.deleteModules("rest/base/crudContracts.json", UserTokensArray);
	}

}
