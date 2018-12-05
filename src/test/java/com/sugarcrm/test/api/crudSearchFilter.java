package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;

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
import com.sugarcrm.test.SugarTestApi;

/**
 * This is a test for CRUD a basic Search Filter
 * 
 */
public class crudSearchFilter extends SugarTestApi
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
   }

   @Test
   @Ignore //This is only for 7.8
   public void main() throws JSONException, ParseException,
         FileNotFoundException, IOException, ParseException
   {
      VoodooUtils.voodoo.log.info("Running " + testName + "...");

      // Calls a standard loop to process the common json test driven format
      myUtils.standardTestDriver("rest/base/crudSearchFilter.json",
            UserTokensArray, recordIDs);

      VoodooUtils.voodoo.log.info(testName + " complete.");
   }

   @Override
   public void cleanup() throws JSONException, ParseException,
         FileNotFoundException, IOException
   {
   // Delete all records that were created in this test via record ids
      myUtils.deleteModules("rest/base/crudSearchFilter.json",
            UserTokensArray);
   }

}
