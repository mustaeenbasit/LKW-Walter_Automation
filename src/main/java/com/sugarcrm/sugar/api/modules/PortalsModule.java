package com.sugarcrm.sugar.api.modules;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.ApiUtils;
import com.sugarcrm.sugar.api.RuntimeRecords;
import com.sugarcrm.sugar.api.UserTokens;

public class PortalsModule {
	JSONObject jsonObject;
	JSONObject jsonObjectDriver;
	Object obj1 = null;
	ApiUtils myUtils = new ApiUtils();
	Response response;

	public void enablePortal(String adminUserID,
			ArrayList<RuntimeRecords> recordIDs,
			ArrayList<UserTokens> UserTokensArray)
			throws FileNotFoundException, JSONException, ParseException,
			IOException {

		// Log in to BWC to get PHPSESSIONID Cookie
		response = given().header("OAuth-Token", adminUserID).expect()
				.statusCode(200).when().post("/oauth2/bwc/login");

		// Store and Set the Rest Assured API global environment variables to
		// call regular http

		String oldBasePath = RestAssured.basePath;
		RestAssured.basePath = "";

		String myCookie = response.getHeader("Set-Cookie");

		Pattern pattern = Pattern.compile("PHPSESSID=(.*?);");
		Matcher matcher = pattern.matcher(myCookie);

		String phpSessionID = "";
		if (matcher.find()) {
			phpSessionID = matcher.group(1);
			VoodooUtils.voodoo.log.info("Extracted phpSessionID "
					+ phpSessionID);
		}

		response = given()
				.cookie("PHPSESSID", phpSessionID)
				.cookie("ck_login_id_20", "1")
				.expect()
				.statusCode(200)
				.when()
				.get("index.php?to_pdf=1&sugar_body_only=1&module=ModuleBuilder&action=portalconfigsave&appStatus=true&logoURL=&maxQueryResult=20&defaultUser=");

		// Restore REST basePath
		 VoodooUtils.voodoo.log.info(" Set Portal Response " + response.asString());

		RestAssured.basePath = oldBasePath;

		// Sets the Portal Support Users Password
		myUtils.standardTestDriver(
				"rest/portal/common/setPortalUserPassword.json",
				UserTokensArray, recordIDs);
	}

	public void disablePortal(String adminUserID) {
		Response response;

		// Log in to BWC to get PHPSESSIONID Cookie
		response = given().header("OAuth-Token", adminUserID).expect()
				.statusCode(200).when().post("/oauth2/bwc/login");

		// Store and Set the Rest Assured API global environment variables to
		// call regular
		// http

		String oldBasePath = RestAssured.basePath;
		RestAssured.basePath = "";

		String myCookie = response.getHeader("Set-Cookie");

		Pattern pattern = Pattern.compile("PHPSESSID=(.*?);");
		Matcher matcher = pattern.matcher(myCookie);

		String phpSessionID = "";
		if (matcher.find()) {
			phpSessionID = matcher.group(1);
			VoodooUtils.voodoo.log.info("Extracted phpSessionID "
					+ phpSessionID);
		}

		response = given()
				.cookie("PHPSESSID", phpSessionID)
				.cookie("ck_login_id_20", "1")
				.expect()
				.statusCode(200)
				.when()
				.get("index.php?to_pdf=1&sugar_body_only=1&module=ModuleBuilder&action=portalconfigsave&appStatus=online&logoURL=&maxQueryResult=20&defaultUser=");

		// Restore REST basePath

		RestAssured.basePath = oldBasePath;
	}

}
