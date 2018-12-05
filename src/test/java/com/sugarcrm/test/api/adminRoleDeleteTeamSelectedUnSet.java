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
import com.sugarcrm.sugar.api.modules.RolesModule;
import com.sugarcrm.test.SugarTestApi;

/**
 * This is a Role ACL test for Delete set to team when the record has selected to true
 * 
 */
public class adminRoleDeleteTeamSelectedUnSet extends SugarTestApi
{
   JSONObject jsonObject;
   String oauthToken;
   String adminUserID;
   Response response;
   RolesModule myRolesModule = new RolesModule();
   ApiUtils myUtils = new ApiUtils();
   ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
   ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

   @Override
   public void setup() throws FileNotFoundException, IOException, ParseException, JSONException
   {
      // Login as Admin and get the Authentication Token
      adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
   // Calls a standard loop to set up RLI with Opportunities
      myUtils.standardTestDriver("rest/common/opportunitiesRLIConfig.json",
            UserTokensArray);
      myUtils.standardTestDriver("rest/base/adminTeamRolesSet.json", UserTokensArray, recordIDs);
      // Set the Team based ACL
      myRolesModule.CreateRole(adminUserID,
            "rest/base/adminRoleDeleteTeamSetUp.json");
      // Create specific users for this test
      myUtils.standardTestDriver("rest/common/createUsers5Level3.json", UserTokensArray, recordIDs);
      oauthToken = myUtils.userLogin("rest/common/loginsalesRep1.json", UserTokensArray);
      oauthToken = myUtils.userLogin("rest/common/loginsalesRep2.json", UserTokensArray);
   }

   @Test
   public void main() throws JSONException, ParseException, FileNotFoundException, IOException, ParseException
   {
      VoodooUtils.voodoo.log.info("Running " + testName + "...");

      // Verify the Access for Delete Edit = Owner for a record that is not selected then a record that is selected
      myUtils.standardTestDriver("rest/base/adminRoleDeleteTeam.json",
      UserTokensArray, recordIDs);
      
      myUtils.standardTestDriver("rest/base/adminRoleDeleteTeamSelected.json",
            UserTokensArray, recordIDs);
      
      // Unset Team based ACL
      myUtils.standardTestDriver("rest/base/adminTeamRolesUnset.json", UserTokensArray, recordIDs);

      // Verify the Access for TBACL no longer applies
      myUtils.standardTestDriver("rest/base/adminRoleDeleteTeamUnSet.json", UserTokensArray, recordIDs);

      VoodooUtils.voodoo.log.info(testName + " complete.");
   }

   @Override
   public void cleanup() throws JSONException, ParseException, FileNotFoundException, IOException
   {
      //Delete the users creted for this test
      myUtils.standardTestDriver("rest/common/deleteUsers5Level3.json",
      UserTokensArray, recordIDs);
      // Delete all records that were created in this test via record ids
      myUtils.deleteModules("rest/base/adminRoleDeleteTeam.json",
      UserTokensArray);
      // Unset Team based ACL
      myUtils.standardTestDriver("rest/base/adminTeamRolesUnset.json",
      UserTokensArray, recordIDs);
   // Delete the Custom Standard Role created during this test
      myUtils.standardTestDriver("rest/common/deleteDefaultRole.json",
      UserTokensArray, recordIDs);
   }

}
