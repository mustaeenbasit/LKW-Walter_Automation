package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;

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
import com.sugarcrm.test.SugarTestApi;

/**
 * This is a test for Process with a simple field modification action
 * 
 */
public class processCreateRelatedRecord extends SugarTestApi
{
   JSONObject jsonObject;
   String oauthToken;
   Response response;
   ApiUtils myUtils = new ApiUtils();
   ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
   ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

   @Override
   public void setup() throws FileNotFoundException, IOException,
         ParseException, JSONException
   {
      // Login as Admin and get the Authentication Token
      jsonObject = myUtils.getJson("loginAdmin.json");
      response = given().body(jsonObject).expect().statusCode(200).when()
            .post("oauth2/token");
      oauthToken = response.path("access_token");
      UserTokens userToken = new UserTokens("admin", oauthToken);
      UserTokensArray.add(userToken);
      // Generate unique Client UIds for creating graphic objects
      for (int x = 0; x < 5; x = x + 1)
      {
         long millisecs = System.currentTimeMillis();
         String myStrmillisecs = "UIID" + x + millisecs;
         RuntimeRecords moduleID = new RuntimeRecords("ID", "UIID/", "",
               myStrmillisecs);
         recordIDs.add(moduleID);
         VoodooUtils.voodoo.log.info(moduleID + " ADDED");
      }
   }

   @Test
   public void main() throws JSONException, ParseException,
         FileNotFoundException, IOException, ParseException
   {
      VoodooUtils.voodoo.log.info("Running " + testName + "...");

      // Calls a standard loop to process the common json test driven format
      myUtils.standardTestDriver("rest/base/processCreateRelatedRecord.json",
            UserTokensArray, recordIDs);

      VoodooUtils.voodoo.log.info(testName + " complete.");
   }

   @Override
   public void cleanup() throws JSONException, ParseException,
         FileNotFoundException, IOException
   {
      // Delete all records that were created in this test
      JSONArray myModuleArray;
      jsonObject = myUtils.getJson("rest/base/processCreateRelatedRecord.json");
      myModuleArray = (JSONArray) jsonObject.get("modules_used");
      myUtils.deleteRecords(myModuleArray, UserTokensArray);
      // Processes are deleted via REST to clean up all related Tables
      myUtils.standardTestDriver("rest/common/deleteProcess.json",
            UserTokensArray, recordIDs);

   }

}
