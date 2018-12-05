package com.sugarcrm.test.api;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.*;

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

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jayway.restassured.RestAssured;

import java.io.File;

public class portalCrudBugs extends SugarTestApi {
	JSONObject jsonObject;
	String oauthToken;
	String adminUserID;
	Response response;
	ApiUtils myUtils = new ApiUtils();
	PortalsModule myPortalsModule = new PortalsModule();
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

		// Enable the Portal
		myPortalsModule.enablePortal(adminUserID, recordIDs, UserTokensArray);

		// Create Account and Portal Contact
		myUtils.standardTestDriver("rest/portal/common/portalCreateContact.json",
				UserTokensArray, recordIDs);
	}

	@Test
	/**
	 * Test Case portalCrudCase: Verifies the soap calls for Create, Read, Update and Delete Bugs
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
				"src/test/resources/apidata/soap/common/soapPortalLogin.xml"))
				.useDelimiter("\\Z").next();
		VoodooUtils.voodoo.log.info("SOAP Request " + content);

		String xml = given().request().contentType("text/xml; charset=UTF-8;")
				.body(content).when().post("soap.php").andReturn().asString();
		VoodooUtils.voodoo.log.info("SOAP Response " + xml);

		Matcher matcher = pattern.matcher(xml);

		// Extract the Session ID to replace it in the XML body of future SOAP
		// calls
		String sessionID = "";
		if (matcher.find()) {
			sessionID = matcher.group(1);
			VoodooUtils.voodoo.log.info("Extracted sessionID " + sessionID);
		}

		// Create a Case in Portal
		content = new Scanner(new File(
				"src/test/resources/apidata/soap/common/portalCreateBug.xml"))
				.useDelimiter("\\Z").next();

		String newRequestBodyString = content.replace("mySessionID", sessionID);
		VoodooUtils.voodoo.log.info("SOAP Request " + newRequestBodyString);

		xml = given().request().contentType("text/xml; charset=UTF-8;")
				.body(newRequestBodyString).when().post("soap.php").andReturn()
				.asString();
		
		VoodooUtils.voodoo.log.info("SOAP Rerurned " + xml);
		
		//Get the ID of the Bugs record via a filter
		
		content = new Scanner(
				new File(
						"src/test/resources/apidata/soap/common/portalGetEntryListFilterBugs.xml"))
				.useDelimiter("\\Z").next();

		newRequestBodyString = content.replace("mySessionID", sessionID);
		VoodooUtils.voodoo.log.info("SOAP Request " + newRequestBodyString);

		xml = given().request().contentType("text/xml; charset=UTF-8;")
				.body(newRequestBodyString).when().post("soap.php").andReturn().asString();

		matcher = pattern.matcher(xml);

		// Extract the Record ID to replace it in the XML body of future SOAP
		// calls
		String recordID = "";
		if (matcher.find()) {
			recordID = matcher.group(1);
			VoodooUtils.voodoo.log.info("Extracted recordID " + recordID);
		}

		// Verify the created Case is present with portal get entry request for
		// the ID
		content = new Scanner(
				new File(
						"src/test/resources/apidata/soap/common/portalGetEntryBugSingle.xml"))
				.useDelimiter("\\Z").next();

		newRequestBodyString = content.replace("mySessionID", sessionID);
		String getRecord = newRequestBodyString.replace("myBugID", recordID);
		VoodooUtils.voodoo.log.info("SOAP Request " + getRecord);

		xml = given().request().contentType("text/xml; charset=UTF-8;")
				.body(getRecord).when().post("soap.php").andReturn().asString();
		VoodooUtils.voodoo.log.info("SOAP Response " + xml);

		assertTrue(xml.contains("MYNEW Bug ABC"));

		// Verify the created Case can be updated using the ID
		content = new Scanner(
				new File(
						"src/test/resources/apidata/soap/common/portalSetEntryUpdateBugs.xml"))
				.useDelimiter("\\Z").next();

		newRequestBodyString = content.replace("mySessionID", sessionID);
		content = newRequestBodyString.replace("myBugID", recordID);
		VoodooUtils.voodoo.log.info("SOAP Request " + content);

		xml = given().request().contentType("text/xml; charset=UTF-8;")
				.body(content).when().post("soap.php").andReturn().asString();
		VoodooUtils.voodoo.log.info("SOAP Response " + xml);

		xml = given().request().contentType("text/xml; charset=UTF-8;")
				.body(getRecord).when().post("soap.php").andReturn().asString();
		VoodooUtils.voodoo.log.info("SOAP Response " + xml);

		assertTrue(xml.contains("MYNEWOLD Bug ABCDEFGHI"));
		assertFalse(xml.contains("MYNEW Bug ABC"));

		// Verify the created Case can be deleted using the ID
		content = new Scanner(
				new File(
						"src/test/resources/apidata/soap/common/portalSetEntryDeleteBugs.xml"))
				.useDelimiter("\\Z").next();

		newRequestBodyString = content.replace("mySessionID", sessionID);
		content = newRequestBodyString.replace("myBugID", recordID);
		VoodooUtils.voodoo.log.info("SOAP Request " + content);

		xml = given().request().contentType("text/xml; charset=UTF-8;")
				.body(content).when().post("soap.php").andReturn().asString();
		VoodooUtils.voodoo.log.info("SOAP Response " + xml);

		xml = given().request().contentType("text/xml; charset=UTF-8;")
				.body(getRecord).when().post("soap.php").andReturn().asString();
		VoodooUtils.voodoo.log.info("SOAP Response " + xml);

		assertFalse(xml.contains("MYNEWOLD Bug ABCDEFGHI"));
		assertFalse(xml.contains("MYNEW Bug ABC"));

		RestAssured.basePath = oldBasePath;

	}

	public void cleanup() throws JSONException, ParseException,
			FileNotFoundException, IOException {

		// Disable the Portal
		myPortalsModule.disablePortal(adminUserID);

		// Delete all records that were created in this test via record ids
		JSONArray myModuleArray;
		jsonObject = myUtils.getJson("rest/portal/common/portalCreateContact.json");
		myModuleArray = (JSONArray) jsonObject.get("modules_used");
		myUtils.deleteRecords(myModuleArray, UserTokensArray);

		// Users and Teams are explicitly deleted
		myUtils.standardTestDriver("rest/portal/common/deleteUsersPortal.json",
				UserTokensArray, recordIDs);

	}
}
