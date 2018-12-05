package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.Response;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.*;
import com.sugarcrm.sugar.api.modules.*;
import com.sugarcrm.test.SugarTestApi;

public class portalRestCrudBugs extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	PortalsModule myPortalsModule = new PortalsModule();
	ApiUtils myUtils = new ApiUtils();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
		// Login as Admin and get the Authentication Token
		jsonObject = myUtils.getJson("loginAdmin.json");
		response = given().body(jsonObject).expect().statusCode(200).when()
				.post("oauth2/token");
		oauthToken = response.path("access_token");
		adminUserID = oauthToken;
		UserTokens userToken = new UserTokens("admin", oauthToken);
		UserTokensArray.add(userToken);

		// Enable Module Bugs for Display - Need to revisit as all Modules are
		// enabled in a single PHP Form Post
		// myUtils.enableBugsModule(adminUserID);

		// Enable the Portal
		myPortalsModule.enablePortal(adminUserID, recordIDs, UserTokensArray);

		// Create Account and Portal Contact
		myUtils.standardTestDriver(
				"rest/portal/common/portalCreateContact.json", UserTokensArray,
				recordIDs);

		// Login to Portal with the portal contact user
		jsonObject = myUtils
				.getJson("rest/portal/common/loginPortalMarleneRuffner1.json");
		response = given().body(jsonObject).expect().statusCode(200).when()
				.post("oauth2/token");
		oauthToken = response.path("access_token");
		userToken = new UserTokens("MarleneRuffner1", oauthToken);
		UserTokensArray.add(userToken);
	}

	@Test
	/**
	 * Test Case portalRestCrudBugs: Verifies the calls for Create, Read, Update and Delete Bugs in Portal
	 * 
	 * @throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException
	 */
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls a standard loop to process the common json test driven format
		myUtils.standardTestDriver("rest/portal/portalRestCrudBugs.json",
				UserTokensArray, recordIDs);

		VoodooUtils.voodoo.log.info(testName + " complete.");

	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {

		// Disable the Portal
		myPortalsModule.disablePortal(adminUserID);

		// Reset Displayed Modules - This needs to be revisited as All modules
		// are enabled as a block in a PHP Post
		// myUtils.restoreDisplayedModules(adminUserID);

		// Delete all records that were created in this test via record ids
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
