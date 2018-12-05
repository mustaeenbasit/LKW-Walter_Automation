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
 * This test verifies that if a non-manager creates two RLIs, one included and one excluded from forecasts,
 * that individual can only view the included RLI amounts in their Forecast
 * 
 */
public class forecastsBasic extends SugarTestApi {
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
		myUtils.standardTestDriver("getNonAdminUserEmailSetUp.json",
				UserTokensArray, recordIDs);
		// Login as Non-Admin bob and get the Authentication Token
		oauthToken = myUtils.userLogin("loginBob.json", UserTokensArray);
	}

	@Test
	@Ignore //TR-12155
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver("rest/base/forecastsBasic.json",
				UserTokensArray, recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete all records that were created in this test via record ids
		myUtils.deleteModules("rest/base/forecastsBasic.json", UserTokensArray);

		// Users and Teams are explicitly deleted

		myUtils.standardTestDriver("getNonAdminUserEmailCleanUp.json",
				UserTokensArray, recordIDs);

		// Because the Teams have been created in the background they are
		// deleted via the default Team Name
		myUtils.deleteTeam("bob", adminUserID);

		// Reset Forecast Configuration
		myForecastsModule.forecastReset(adminUserID);
	}

}
