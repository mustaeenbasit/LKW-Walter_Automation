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
import com.sugarcrm.sugar.api.modules.PortalsModule;
import com.sugarcrm.test.SugarTestApi;

public class portalRestPortalNotEnabled extends SugarTestApi
{
   JSONObject jsonObject;
   String oauthToken;
   String adminUserID;
   Response response;
   PortalsModule myPortalsModule = new PortalsModule();
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
      adminUserID = oauthToken;
      UserTokens userToken = new UserTokens("admin", oauthToken);
      UserTokensArray.add(userToken);

      // Create Account and Portal Contact
      myUtils.standardTestDriver("rest/portal/common/portalCreateContact.json",
            UserTokensArray, recordIDs);
   }

   @Test
   /**
    * Test Case portalRestPortalNotenabled: 
    * Verifies that a User cannot login to portal if it has not beeen enabled
    * 
    * @throws JSONException, ParseException,
   		FileNotFoundException, IOException, ParseException
    */
   public void main() throws JSONException, ParseException,
         FileNotFoundException, IOException, ParseException
   {
      VoodooUtils.voodoo.log.info("Running " + testName + "...");

      // Try to Login to Portal with the portal contact user and incorrect
      // Password - Status 400 expected
      jsonObject = myUtils
            .getJson("rest/portal/common/loginPortalMarleneRuffner1.json");
      response = given().body(jsonObject).expect().statusCode(400).when()
            .post("oauth2/token");

      // Enable the Portal then login
      myPortalsModule.enablePortal(adminUserID, recordIDs, UserTokensArray);

      jsonObject = myUtils
            .getJson("rest/portal/common/loginPortalMarleneRuffner1.json");
      response = given().body(jsonObject).expect().statusCode(200).when()
            .post("oauth2/token");

      VoodooUtils.voodoo.log.info("Response " + response.asString());

      VoodooUtils.voodoo.log.info(testName + " complete.");

   }

   @Override
   public void cleanup() throws JSONException, ParseException,
         FileNotFoundException, IOException
   {

      // Disable the Portal
      myPortalsModule.disablePortal(adminUserID);

      JSONArray myModuleArray;
      jsonObject = myUtils
            .getJson("rest/portal/common/portalCreateContact.json");
      myModuleArray = (JSONArray) jsonObject.get("modules_used");
      myUtils.deleteRecords(myModuleArray, UserTokensArray);

      // Users and Teams are explicitly deleted
      myUtils.standardTestDriver("rest/portal/common/deleteUsersPortal.json",
            UserTokensArray, recordIDs);

   }
}
