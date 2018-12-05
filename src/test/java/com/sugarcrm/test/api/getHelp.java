package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.Response;
import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTestApi;
/**
 * This test verifies that the GET help request returns the rest API help documentation
 * 
 */
public class getHelp extends SugarTestApi {
	Response response;

	public void setup() throws FileNotFoundException, IOException,
			ParseException {
		// No login required for Help
	}

	@Test
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		response = given().expect().statusCode(200).when().get("help");

		String myString = response.asString();
		assert (myString.contains("SugarCRM Auto Generated API Help"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {
		// No clean up is needed as no records have been created
	}

}
