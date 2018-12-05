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
 * Verify a field level ACL then delete the role and double check access
 * 
 */
public class adminRolesFieldAccessDelete extends SugarTestApi
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
      oauthToken = myUtils.userLogin("rest/common/loginsalesRep1.json", UserTokensArray);
      oauthToken = myUtils.userLogin("rest/common/loginsalesRep2.json", UserTokensArray);
      // Create a Role to Link to the salesRep1 User
      myUtils.standardTestDriver("rest/base/adminRolesCreate.json", UserTokensArray, recordIDs);
      myUtils.standardTestDriver("rest/base/adminRolesLinkUsers.json", UserTokensArray, recordIDs);
   }

   @Test
   public void main() throws JSONException, ParseException, FileNotFoundException, IOException, ParseException
   {
      VoodooUtils.voodoo.log.info("Running " + testName + "...");

      // Set basic field access and test with an associated user
      myUtils.standardTestDriver("rest/base/adminRolesFieldAccess.json", UserTokensArray, recordIDs);
      myUtils.standardTestDriver("rest/common/deleteDefaultRole.json", UserTokensArray, recordIDs);
      myUtils.standardTestDriver("rest/base/adminRolesFieldAccessDelete.json", UserTokensArray, recordIDs);


      VoodooUtils.voodoo.log.info(testName + " complete.");
   }

   @Override
   public void cleanup() throws JSONException, ParseException, FileNotFoundException, IOException
   {
      // Delete the Custom Role and Users created during this test
      myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json", UserTokensArray, recordIDs);
      // Delete all records that were created in this test via record ids
      myUtils.deleteModules("rest/base/adminRolesFieldAccess.json", UserTokensArray);
   }

}
