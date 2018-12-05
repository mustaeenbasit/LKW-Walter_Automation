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
import java.util.Iterator;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.*;
import com.sugarcrm.test.SugarTestApi;

/**
 * This is a Utility test that verifies that the java infrastructure works, if
 * this test fails then there are major issues with the testing framework itself
 * 
 */
public class createRecords extends SugarTestApi {
	static JSONObject jsonObject;
	static String oauthToken;
	static Response response;
	static ApiUtils myUtils = new ApiUtils();
	static ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();
	JSONArray myModuleArray;
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();

	public void setup() throws FileNotFoundException, IOException,
			ParseException {
		// Login as Admin and get the Authentication Token
		jsonObject = myUtils.getJson("loginAdmin.json");
		response = given().body(jsonObject).expect().statusCode(200).when()
				.post("oauth2/token");
		oauthToken = response.path("access_token");
		UserTokens userToken = new UserTokens("admin", oauthToken);
		UserTokensArray.add(userToken);
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		jsonObject = myUtils.getJson("rest/base/create.json");
		JSONArray myJsonArray = (JSONArray) jsonObject.get("requests");
		myModuleArray = (JSONArray) jsonObject.get("modules_used");
		Iterator<JSONObject> iterator = myJsonArray.iterator();

		while (iterator.hasNext()) {
			jsonObject = (JSONObject) iterator.next();
			HttpRequest myRequest = new HttpRequest(jsonObject, recordIDs,
					UserTokensArray);
			response = myRequest.execute(recordIDs);
			HttpResponse myResponse = new HttpResponse(response);
			myResponse.validate(myRequest, recordIDs);
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException {
		// Delete all records that were created in this test

		myUtils.deleteRecords(myModuleArray, UserTokensArray);

	}

}
