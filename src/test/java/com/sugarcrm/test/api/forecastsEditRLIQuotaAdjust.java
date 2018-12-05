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
import com.sugarcrm.sugar.api.modules.*;
import com.sugarcrm.test.SugarTestApi;
/**
 * This test verifies that for a middle manager:  When a Quota is present, with forecasting by RLI,
 * then if the RLI likely amount is changed by the middle manager, the Forecast adjusted amount is not updated
 * 
 */
public class forecastsEditRLIQuotaAdjust extends SugarTestApi {
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
		oauthToken = myUtils.userLogin("rest/common/logingroupMgr1.json",
				UserTokensArray);
		oauthToken = myUtils.userLogin("rest/common/loginsalesMgr1.json",
				UserTokensArray);
	}

	@Test
	@Ignore //TR-9015
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the Basic Quota test as a
		// prerequisite
		myUtils.standardTestDriver("rest/base/forecastsQuotaBasic.json",
				UserTokensArray, recordIDs);

		// Calls a standard loop to process the main test case
		myUtils.standardTestDriver(
				"rest/base/forecastsEditRLIQuotaAdjust.json", UserTokensArray,
				recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete all records that were created in this test via record ids
		myUtils.deleteModules("rest/base/forecastsEditRLIQuotaAdjust.json",
				UserTokensArray);
		// Users and Teams are explicitly deleted
		myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
				UserTokensArray, recordIDs);
		// Reset Forecast Configuration
		myForecastsModule.forecastReset(adminUserID);
		// Calls a standard loop to set up Opportunities without RLI
		myUtils.standardTestDriver(
				"rest/common/opportunitiesOpportunitiesConfig.json",
				UserTokensArray);
	}

}
