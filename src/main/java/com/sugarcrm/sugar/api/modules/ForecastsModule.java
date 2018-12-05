package com.sugarcrm.sugar.api.modules;

import static com.jayway.restassured.RestAssured.given;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.*;

public class ForecastsModule {
	JSONObject jsonObject;
	JSONObject jsonObjectDriver;
	Object obj1 = null;
	ApiUtils myUtils = new ApiUtils();
	Response response;

	public void forecastReset(String adminUserID) {
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

		response = given().cookie("PHPSESSID", phpSessionID)
				.cookie("ck_login_id_20", "1").expect().statusCode(200).when()
				.get("index.php?module=Forecasts&action=ResetSettings&bwcFrame=1");

		// Restore REST basePath

		RestAssured.basePath = oldBasePath;
	}

}
