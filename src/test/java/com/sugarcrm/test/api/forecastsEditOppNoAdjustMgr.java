package com.sugarcrm.test.api;

import com.jayway.restassured.response.Response;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.*;
import com.sugarcrm.sugar.api.modules.*;
import com.sugarcrm.test.SugarTestApi;

/**
 * This test verifies that for a middle manager: When the likely forecast by
 * Opportunity amount is NOT adjusted by the middle manager him self then if the
 * Opportunity likely amount is changed by the middle manager, the adjusted
 * amount is updated automatically to match
 * 
 */
public class forecastsEditOppNoAdjustMgr extends SugarTestApi {
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
		// Store future Dates in the Modules array for use in creating RLIs

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");

		currentDate = Calendar.getInstance();
		int offset = Integer.parseInt("100");
		currentDate.add(Calendar.DATE, offset);
		String date100 = formatter.format(currentDate.getTime());
		VoodooUtils.voodoo.log.info("Today + 100 = " + date100);
		RuntimeRecords moduleID = new RuntimeRecords("ID", "Dates/", "",
				date100);
		recordIDs.add(moduleID);

		currentDate = Calendar.getInstance();
		offset = Integer.parseInt("200");
		currentDate.add(Calendar.DATE, offset);
		String date200 = formatter.format(currentDate.getTime());
		VoodooUtils.voodoo.log.info("Today + 200 = " + date200);
		moduleID = new RuntimeRecords("ID", "Dates/", "", date200);
		recordIDs.add(moduleID);

		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
		// Calls a standard loop to set up Opportunities without RLI
		myUtils.standardTestDriver(
				"rest/common/opportunitiesOpportunitiesConfig.json",
				UserTokensArray);
		// Calls a common json to Configue Forecasts
		myUtils.standardTestDriver("rest/common/forecastsConfigOpp.json",
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
	}

	@Test
	@Ignore
	// SFA-3218
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver(
				"rest/base/forecastsEditOppNoAdjustMgr.json", UserTokensArray,
				recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete all records that were created in this test via record ids
		myUtils.deleteModules("rest/base/forecastsEditOppNoAdjustMgr.json",
				UserTokensArray);
		// Users and Teams are explicitly deleted
		myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
				UserTokensArray, recordIDs);
		// Reset Forecast Configuration
		myForecastsModule.forecastReset(adminUserID);
		// Calls a standard loop to set up RLI with Opportunities
		myUtils.standardTestDriver("rest/common/opportunitiesRLIConfig.json",
				UserTokensArray);
	}

}
