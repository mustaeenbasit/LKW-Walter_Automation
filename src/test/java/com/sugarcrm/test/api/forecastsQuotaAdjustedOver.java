package com.sugarcrm.test.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
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

/**
 * This test verifies that for a middle manager: When Forecasting is by RLI and
 * the Group Manager sets a Quota and the Middle manager allocates more than the
 * total Quota to his directs and himself, both the original Quota and the over
 * allocated Quota are visible to the Group manager
 * 
 */
public class forecastsQuotaAdjustedOver extends SugarTestApi
{
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
         ParseException, JSONException
   {
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
   public void main() throws JSONException, ParseException,
         FileNotFoundException, IOException, ParseException
   {
      VoodooUtils.voodoo.log.info("Running " + testName + "...");

      // Calls a standard loop to process the common json test driven format
      myUtils.standardTestDriver("rest/base/forecastsQuotaAdjustedOver.json",
            UserTokensArray, recordIDs);

      VoodooUtils.voodoo.log.info(testName + " complete.");
   }

   @Override
   public void cleanup() throws JSONException, ParseException,
         FileNotFoundException, IOException
   {
      // Users and Teams are explicitly deleted
      myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
            UserTokensArray, recordIDs);
      // Delete all records that were created in this test via record ids
      myUtils.deleteModules("rest/base/forecastsQuotaAdjustedOver.json",
            UserTokensArray);
      // Reset Forecast Configuration
      myForecastsModule.forecastReset(adminUserID);
   }

}
