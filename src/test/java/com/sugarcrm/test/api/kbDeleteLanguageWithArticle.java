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
 * This is a basic smoke test for the Deletion of a Language that has existing
 * articles created, the KB Articles should alos be deleted
 * 
 */
public class kbDeleteLanguageWithArticle extends SugarTestApi
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

      // Calls a standard loop to process the common json test driven format
      myUtils.standardTestDriver("rest/base/kbConfigLanguage.json",
            UserTokensArray, recordIDs);
   }

   @Test
   // @Ignore
   public void main() throws JSONException, ParseException,
         FileNotFoundException, IOException, ParseException
   {
      VoodooUtils.voodoo.log.info("Running " + testName + "...");

      // Calls a standard loop to process the common json test driven format
      myUtils.standardTestDriver("rest/base/kbCreateLanguageArticle.json",
            UserTokensArray, recordIDs);

      // Remove the Language just created
      myUtils.standardTestDriver("rest/base/kbConfigRemoveLanguage.json",
            UserTokensArray, recordIDs);

      // Verify that the KB Article with the removed language has been deleted
      // automatically
     myUtils.standardTestDriver("rest/base/kbLanguageArticleDeleted.json",
            UserTokensArray, recordIDs);

      VoodooUtils.voodoo.log.info(testName + " complete.");
   }

   public void cleanup() throws JSONException, ParseException,
         FileNotFoundException, IOException
   {

      // Delete all records that were created in this test
      myUtils.deleteModules("rest/base/kbLanguageArticleDeleted.json",
      UserTokensArray);
   }

}
