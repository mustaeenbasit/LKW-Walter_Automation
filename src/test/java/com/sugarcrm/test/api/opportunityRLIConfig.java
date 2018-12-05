package com.sugarcrm.test.api;

import com.jayway.restassured.response.Response;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.*;
import com.sugarcrm.sugar.api.modules.ForecastsModule;
import com.sugarcrm.test.SugarTestApi;

public class opportunityRLIConfig extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();
	ForecastsModule myForecastsModule = new ForecastsModule();

	public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
	}

	@Test
	/**
	 * Test Case Verifies switching back and forth from Opp with RLI to Opp without RLI
	 * 
	 * @throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException
	 */
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Switch between Opp with RLI and Opp without RLI in Sequence
		int count = 1;
		while (count < 6) {
			myUtils.standardTestDriver(
					"rest/common/opportunitiesRLIConfig.json", UserTokensArray);
			myUtils.standardTestDriver(
					"rest/common/opportunitiesOpportunitiesConfig.json",
					UserTokensArray);
			count++;
		}
		
		// Switch between Opp with RLI and Opp without RLI out of Sequence
				count = 1;
				while (count < 6) {
					myUtils.standardTestDriver(
							"rest/common/opportunitiesRLIConfig.json", UserTokensArray);
					myUtils.standardTestDriver(
							"rest/common/opportunitiesRLIConfig.json", UserTokensArray);
					myUtils.standardTestDriver(
							"rest/common/opportunitiesOpportunitiesConfig.json",
							UserTokensArray);
					myUtils.standardTestDriver(
							"rest/common/opportunitiesOpportunitiesConfig.json",
							UserTokensArray);
					count++;
				}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Calls a standard loop to set up Opportunities without RLI
		myUtils.standardTestDriver(
				"rest/common/opportunitiesOpportunitiesConfig.json",
				UserTokensArray);
	}

}
