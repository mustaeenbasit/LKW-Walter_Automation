package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.ApiUtils;
import com.sugarcrm.sugar.api.RuntimeRecords;
import com.sugarcrm.sugar.api.UserTokens;
import com.sugarcrm.sugar.api.modules.ForecastsModule;
import com.sugarcrm.test.SugarTestApi;

/**
 * This test verifies that the User receives an error message if the User enters
 * user and password directly into the URL
 */
public class getUrlUserPassword extends SugarTestApi {
	Response response;
	String oldBasePath;
	ApiUtils myUtils = new ApiUtils();
	ForecastsModule myForecastsModule = new ForecastsModule();
	ArrayList<UserTokens> UserTokensArray = new ArrayList<UserTokens>();
	ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

	@Override
   public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {

		// Store and Set the Rest Assured API global environment variables to
		// call regular HTTP
		oldBasePath = RestAssured.basePath;
		RestAssured.basePath = "";
	}

	@Test
	@Ignore // SC-3803
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Try to use user and password on the url and a 406 Not Acceptable is expected
		response = given().expect().statusCode(406).when()
				.get("/?username=x&password=y");
		VoodooUtils.voodoo.log.info("Response - Body " + response.asString());
		assertTrue(response.asString().contains("Not Acceptable"));
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Override
   public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// Restore REST basePath
		RestAssured.basePath = oldBasePath;
	}
}
