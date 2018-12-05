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
import com.sugarcrm.test.SugarTestApi;

/**
 * This is a basic smoke test to Set Update and Un-Set Team Based Roles
 * 
 */
public class adminTeamRolesCRUD extends SugarTestApi
{
   JSONObject jsonObject;
   String oauthToken;
   String adminUserID;
   Response response;
   ApiUtils myUtils = new ApiUtils();
   ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
   ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

   @Override
   public void setup() throws FileNotFoundException, IOException, ParseException, JSONException
   {
      // Login as Admin and get the Authentication Token
      adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
      // Create specific users for this test
      myUtils.standardTestDriver("rest/common/createUsers5Level3.json", UserTokensArray, recordIDs);
      // Login as Sales Reps and get the Authentication Tokens
      oauthToken = myUtils.userLogin("rest/common/loginsalesRep1.json", UserTokensArray);
      oauthToken = myUtils.userLogin("rest/common/loginsalesRep2.json", UserTokensArray);
   }

   @Test
   public void main() throws JSONException, ParseException, FileNotFoundException, IOException, ParseException
   {
      VoodooUtils.voodoo.log.info("Running " + testName + "...");

      // Calls a standard loop to process the common json test driven format
      myUtils.standardTestDriver("rest/base/adminTeamRolesCRUD.json", UserTokensArray, recordIDs);

      VoodooUtils.voodoo.log.info(testName + " complete.");
   }

   @Override
   public void cleanup() throws JSONException, ParseException, FileNotFoundException, IOException
   {
      // Delete the created records and unset the Team Based Roles
     myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json", UserTokensArray, recordIDs);
      // Unset Team based ACL
    myUtils.standardTestDriver("rest/base/adminTeamRolesUnset.json", UserTokensArray, recordIDs);

   }

}
