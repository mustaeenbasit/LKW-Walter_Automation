package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.*;

import com.jayway.restassured.response.Response;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.api.*;
import com.sugarcrm.sugar.api.modules.*;
import com.sugarcrm.test.SugarTestApi;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jayway.restassured.RestAssured;

import java.io.File;

public class soapReportingLoop extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	ApiUtils myUtils = new ApiUtils();

	public void setup() throws FileNotFoundException, IOException,
			ParseException, JSONException {
	}

	@Test
	/**
	 * Test Case soapReportingLoop: Verifies a Reporting Loop cannot be created by Updating a User
	 * 
	 * @throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException
	 */
	public void main() throws JSONException, ParseException,
			FileNotFoundException, IOException, ParseException {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		Pattern pattern = Pattern
				.compile("<id xsi:type=\"xsd:string\">(.*?)</id>");

		// Using SOAP so un-set the RestAssured variables
		String oldBasePath = RestAssured.basePath;
		RestAssured.basePath = "";

		// Login to Portal
		String content = new Scanner(new File(
				"src/test/resources/apidata/soap/common/soapLoginAdmin.xml"))
				.useDelimiter("\\Z").next();
		VoodooUtils.voodoo.log.info("SOAP Request " + content);

		String xml = given().request().contentType("text/xml; charset=UTF-8;")
				.body(content).when().post("service/v4_1/soap.php").andReturn().asString();
		VoodooUtils.voodoo.log.info("SOAP Response " + xml);

		Matcher matcher = pattern.matcher(xml);

		// Extract the Session ID to replace it in the XML body of future SOAP
		// calls
		String sessionID = "";
		if (matcher.find()) {
			sessionID = matcher.group(1);
			VoodooUtils.voodoo.log.info("Extracted sessionID " + sessionID);
		}

		// Update Reports To in User
		content = new Scanner(new File(
				"src/test/resources/apidata/soap/common/soapReportsTo.xml"))
				.useDelimiter("\\Z").next();

		String newRequestBodyString = content.replace("mySessionID", sessionID);
		VoodooUtils.voodoo.log.info("SOAP Update Reports To in User "
				+ newRequestBodyString);

		xml = given().request().contentType("text/xml; charset=UTF-8;")
				.body(newRequestBodyString).when().post("service/v4_1/soap.php").andReturn()
				.asString();
		VoodooUtils.voodoo.log
				.info("SOAP Response from Update Reports To in User " + xml);

		RestAssured.basePath = oldBasePath;

	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {

	}
}
