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
import com.sugarcrm.sugar.api.UserTokens;
import com.sugarcrm.test.SugarTestApi;

/**
 * This is a basic smoke test to verify that the oauth Token cannot be sent as
 * part of the URL, BR-3370
 * 
 */
public class securityOauthInURL extends SugarTestApi
{
   JSONObject jsonObject;
   String oauthToken;
   String adminUserID;
   Response response;
   ApiUtils myUtils = new ApiUtils();
   ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();

   @Override
   public void setup() throws FileNotFoundException, IOException,
         ParseException
   {
      // Login as Admin and get the Authentication Token
      adminUserID = myUtils.userLogin("loginAdmin.json", UserTokensArray);
   }

   @Test
   public void main() throws JSONException, ParseException,
         FileNotFoundException, IOException, ParseException
   {
      VoodooUtils.voodoo.log.info("Running " + testName + "...");

      // Calls a standard loop to process the common json test driven format
      myUtils
            .standardTestDriver("rest/base/securityOauthInURL.json", UserTokensArray);

      VoodooUtils.voodoo.log.info(testName + " complete.");
   }

   @Override
   public void cleanup() throws JSONException, ParseException,
         FileNotFoundException, IOException
   {
      // Delete all records that were created in this test
      myUtils.deleteModules("rest/base/securityOauthInURL.json", UserTokensArray);
   }

}
