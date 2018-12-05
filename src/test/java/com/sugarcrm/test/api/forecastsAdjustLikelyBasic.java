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
import com.sugarcrm.sugar.api.modules.ForecastsModule;
import com.sugarcrm.test.SugarTestApi;
/**
 * This test verifies that if a middle manager adjusts the likely case forecast
 * for a direct report and then commits the forecast the middle manager's manager (group manager) can see the adjusted
 * likely case for the forecast of the appropriate indirect report.
 * 
 */
public class forecastsAdjustLikelyBasic extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	String userMaxID;
	String userSallyID;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ForecastsModule myForecastsModule = new ForecastsModule();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

	@Override
   public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
		// Calls a common json to Configue Forecasts
		// Calls a standard loop to set up RLI with Opportunities
				myUtils.standardTestDriver("rest/common/opportunitiesRLIConfig.json",
						UserTokensArray);
		myUtils.standardTestDriver("rest/common/forecastsConfig.json",
				UserTokensArray, recordIDs);
		// Create specific users for this test
		myUtils.standardTestDriver("rest/common/createUsers5Level3.json",
				UserTokensArray, recordIDs);
		// Login as Sales Reps and Managers and get the Authentication Tokens
		oauthToken = myUtils.userLogin("rest/common/loginsalesRep1.json",
				UserTokensArray);
		oauthToken = myUtils.userLogin("rest/common/loginsalesRep2.json",
				UserTokensArray);
		oauthToken = myUtils.userLogin("rest/common/loginsalesMgr1.json",
				UserTokensArray);
		oauthToken = myUtils.userLogin("rest/common/logingroupMgr1.json",
				UserTokensArray);
		// Use this test as a prerequisite
		myUtils.standardTestDriver("rest/base/forecastsRollup.json",
				UserTokensArray, recordIDs);
	}

	@Test
	@Ignore //TR-12155
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver("rest/base/forecastsAdjustLikelyBasic.json",
				UserTokensArray, recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete all records that were created in this test via record ids
		myUtils.deleteModules("rest/base/forecastsAdjustLikelyBasic.json",
				UserTokensArray);
		// Users and Teams are explicitly deleted
		myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
				UserTokensArray, recordIDs);
		// Reset Forecast Configuration
		myForecastsModule.forecastReset(adminUserID);
	}

}
