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
 * This is a Utility test that is not run in production, it can be amended in
 * order to Create large volumes of data, in the 1,500 records range for test cases
 * that require such numbers
 * 
 */
public class createData extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
	}

	@Test
	@Ignore
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		int x = 1;
		while (x < 640) {
			// Calls a standard loop to process the common json test driven
			// format
			myUtils.standardTestDriver("rest/base/createUser.json",
					UserTokensArray);
			x++;
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {

	}

}
