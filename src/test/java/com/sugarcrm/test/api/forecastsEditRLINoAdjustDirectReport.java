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
 * This test verifies that for a non-manager:  When the likely forecast, by RLI, amount is not adjusted by the manager
 * then if the RLI likely amount is changed by the direct report, the adjusted amount is automatically updated to match 
 * 
 */
public class forecastsEditRLINoAdjustDirectReport extends SugarTestApi {
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
		// Calls a standard loop to set up RLI with Opportunities
		myUtils.standardTestDriver("rest/common/opportunitiesRLIConfig.json",
				UserTokensArray);
		// Calls a common json to Configue Forecasts
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
	}

	@Test
	@Ignore //TR-12155
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the basic roll up rli s as a prerequisite
		myUtils.standardTestDriver("rest/base/forecastsRollup.json",
				UserTokensArray, recordIDs);
		
		// Calls a standard loop to process the basic test of this junit
				myUtils.standardTestDriver("rest/base/forecastsEditRLINoAdjustDirectReport.json",
						UserTokensArray, recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete all records that were created in this test via record ids
		myUtils.deleteModules("rest/base/forecastsEditRLINoAdjustDirectReport.json",
				UserTokensArray);
		// Users and Teams are explicitly deleted
		myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
				UserTokensArray, recordIDs);
		// Reset Forecast Configuration
		myForecastsModule.forecastReset(adminUserID);
	}

}
