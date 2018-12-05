package com.sugarcrm.test.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.ApiUtils;
import com.sugarcrm.sugar.api.RuntimeRecords;
import com.sugarcrm.sugar.api.UserTokens;
import com.sugarcrm.sugar.api.modules.ForecastsModule;
import com.sugarcrm.test.SugarTestApi;

public class rollUpOpportunity extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();
	ForecastsModule myForecastsModule = new ForecastsModule();

	@Override
   public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
		// Calls a standard loop to set up RLI with Opportunities
		myUtils.standardTestDriver("rest/common/opportunitiesRLIConfig.json",
				UserTokensArray);
	// Generate unique Client UIds for creating GUIDs for RLI
      for (int x = 0; x < 5; x = x + 1) {
         long millisecs = System.currentTimeMillis();
         String myStrmillisecs = "UIID" + x + millisecs;
         RuntimeRecords moduleID = new RuntimeRecords("ID", "UIID/", "",
               myStrmillisecs);
         recordIDs.add(moduleID);
         VoodooUtils.voodoo.log.info(moduleID + " ADDED");
      }
	}

	@Test
	/**
	 * Test Case rollUpOpportunity: Verifies the RLI can roll up values into Opportunity, No Forecast setting needed
	 * 
	 * @throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException
	 */
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver("rollUpOpportunity.json", UserTokensArray, recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Delete all records that were created in this test
		JSONArray myModuleArray;
		jsonObject = myUtils.getJson("rollUpOpportunity.json");
		myModuleArray = (JSONArray) jsonObject.get("modules_used");
		myUtils.deleteRecords(myModuleArray, UserTokensArray);
	}

}
