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
 * This is a test to increase the useful counts and not useful counts for a
 * KBContent record using multiple users
 * 
 */
public class kbContentsMostUsefulPublished extends SugarTestApi
{
   JSONObject jsonObject;
   String oauthToken;
   String adminUserID;
   Response response;
   ApiUtils myUtils = new ApiUtils();
   ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
   ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

   public void setup() throws FileNotFoundException, IOException,
         ParseException, JSONException
   {
      // Login as Admin and get the Authentication Token
      adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
      // Create specific users for this test
      myUtils.standardTestDriver("rest/common/createUsers5Level3.json",
            UserTokensArray, recordIDs);
      // Login as Sales Reps and Managers and get the Authentication Tokens
      oauthToken = myUtils.userLogin("rest/common/loginsalesRep1.json",
            UserTokensArray);
      // Login as Sales Reps and Managers and get the Authentication Tokens
      oauthToken = myUtils.userLogin("rest/common/loginsalesRep2.json",
            UserTokensArray);
      // Login as Sales Reps and Managers and get the Authentication Tokens
      oauthToken = myUtils.userLogin("rest/common/logingroupMgr1.json",
            UserTokensArray);
      // Login as Sales Reps and Managers and get the Authentication Tokens
      oauthToken = myUtils.userLogin("rest/common/loginsalesMgr1.json",
            UserTokensArray);
   }

   @Test
   public void main() throws JSONException, ParseException,
         FileNotFoundException, IOException, ParseException
   {
      VoodooUtils.voodoo.log.info("Running " + testName + "...");

      // Calls a standard loop to process the common json test driven format
      myUtils.standardTestDriver(
            "rest/base/kbContentsMostUsefulPublished.json", UserTokensArray);

      VoodooUtils.voodoo.log.info(testName + " complete.");
   }

   public void cleanup() throws JSONException, ParseException,
         FileNotFoundException, IOException
   {
      // Delete all records that were created in this test
      myUtils.deleteModules("rest/base/kbContentsMostUsefulPublished.json",
            UserTokensArray);
      myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
            UserTokensArray, recordIDs);
   }

}
