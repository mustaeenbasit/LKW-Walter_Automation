package com.sugarcrm.sugar.api;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.VoodooUtils;

public class ApiUtils {
	JSONObject jsonObject;
	JSONObject jsonObjectDriver;
	Object obj1 = null;
	Response response;

	public JSONObject getJson(String readFile) throws FileNotFoundException,
			IOException, ParseException {
		readFile = "src/test/resources/apidata/" + readFile;
		Object myObj;
		JSONParser parser = new JSONParser();
		myObj = parser.parse(new FileReader(readFile));
		jsonObject = (JSONObject) myObj;
		return jsonObject;
	}

	public String userLogin(String readFile,
			ArrayList<UserTokens> UserTokensArray)
			throws FileNotFoundException, IOException, ParseException {
		jsonObject = getJson(readFile);
		response = given().body(jsonObject).expect().statusCode(200).when()
				.post("oauth2/token");
		String oauthToken = response.path("access_token");
		String username = (String) jsonObject.get("username");
		UserTokens userToken = new UserTokens(username, oauthToken);
		UserTokensArray.add(userToken);
		VoodooUtils.voodoo.log.info("Extracted oauthToken " + username + " "
				+ oauthToken);
		return oauthToken;
	}

	public String getID(JSONObject myJson, ArrayList<RuntimeRecords> recordIDs) {
		String foundID = "Notfound";
		String myModule = (String) myJson.get("module");
		long count = 0;

		for (int i = 0; i < recordIDs.size(); i++) {
			RuntimeRecords myRec = recordIDs.get(i);
			if (myRec.recordModule.contains(myModule)
					&& (myRec.recordType.contains("ID"))) {
				count++;
				long myIndex = (long) myJson.get("index");
				foundID = myRec.recordValue;
				if (count == myIndex) {
					break;
				}
			}
		}
		return foundID;
	}

	public String getDate(String myValue) {
		String myDate = "Notfound";
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");
		switch (myValue) {
		case "now":
			myDate = formatter.format(currentDate.getTime());
			VoodooUtils.voodoo.log.info("My Date = " + myDate);
			break;
		default:

		}
		return myDate;
	}
	
	public String getJustDate(String myValue) {
      String myDate = "Notfound";
      Calendar currentDate = Calendar.getInstance();
      SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd");
      switch (myValue) {
      case "now":
         myDate = formatter.format(currentDate.getTime());
         VoodooUtils.voodoo.log.info("My Date = " + myDate);
         break;
      default:

      }
      return myDate;
   }

	public void deleteRecords(JSONArray myModuleArray,
			ArrayList<UserTokens> UserTokensArray) throws JSONException,
			ParseException {
		String oauthToken = getUserToken(UserTokensArray, "admin");
		Iterator<JSONObject> iterator = myModuleArray.iterator();
		Response response = null;
		JSONParser parser = new JSONParser();
		Object obj1 = null;
		Long nextOffset;

		while (iterator.hasNext()) {
			jsonObject = (JSONObject) iterator.next();
			String myModule = (String) jsonObject.get("module");
			VoodooUtils.voodoo.log.info("Deleting " + myModule + " ......");
			do {
				response = given().header("OAuth-Token", oauthToken).expect()
						.statusCode(200).when().get(myModule);
				obj1 = parser.parse(response.asString());
				jsonObject = (JSONObject) obj1;
				nextOffset = (long) jsonObject.get("next_offset");
				JSONArray myJsonArrayRecords = (JSONArray) jsonObject
						.get("records");
				Iterator<JSONObject> iteratorRecords = myJsonArrayRecords
						.iterator();
				while (iteratorRecords.hasNext()) {
					JSONObject jsonObjectRecord = (JSONObject) iteratorRecords
							.next();
					String myId = (String) jsonObjectRecord.get("id");
					response = given().header("OAuth-Token", oauthToken)
							.expect().statusCode(200).when()
							.delete(myModule + "/" + myId);
				}
				// Indicates there are more records to be retrieved and
				// deleted
			} while (nextOffset != -1);
		}
	}

	/**
	 * Implements parsing and execution of a standard data driven json test case
	 * 
	 * @throws JSONException
	 *             , ParseException, FileNotFoundException, IOException
	 */
	public void standardTestDriver(String testData,
			ArrayList<UserTokens> UserTokensArray) throws JSONException,
			ParseException, FileNotFoundException, IOException {

		ArrayList<RuntimeRecords> recordIDs = new ArrayList<RuntimeRecords>();

		standardTestDriver(testData, UserTokensArray, recordIDs);

	}

	/**
	 * Implements parsing and execution of a standard data driven json test case
	 * This Version will become the master as it has the RecordIDs as a
	 * parameter Having the Record IDs held in the Test allows for the Same
	 * records to be passed from init, test to cleanup
	 * 
	 * @throws JSONException
	 *             , ParseException, FileNotFoundException, IOException
	 */
	public void standardTestDriver(String testData,
			ArrayList<UserTokens> UserTokensArray,
			ArrayList<RuntimeRecords> recordIDs) throws JSONException,
			ParseException, FileNotFoundException, IOException {
		JSONArray myModuleArray;
		Response response;
		jsonObjectDriver = getJson(testData);
		JSONArray myJsonArrayTC = (JSONArray) jsonObjectDriver.get("testCases");
		Iterator<JSONObject> iteratorTests = myJsonArrayTC.iterator();

		while (iteratorTests.hasNext()) {
			jsonObjectDriver = (JSONObject) iteratorTests.next();
			String testCaseName = (String) jsonObjectDriver.get("testCaseName")
					.toString();
			VoodooUtils.voodoo.log.info("Running test case " + testCaseName
					+ "...");
			JSONArray myJsonArrayRequests = (JSONArray) jsonObjectDriver
					.get("requests");
			Iterator<JSONObject> iteratorRequests = myJsonArrayRequests
					.iterator();
			while (iteratorRequests.hasNext()) {
				jsonObjectDriver = (JSONObject) iteratorRequests.next();
				HttpRequest myRequest = new HttpRequest(jsonObjectDriver,
						recordIDs, UserTokensArray);
				response = myRequest.execute(recordIDs);
				HttpResponse myResponse = new HttpResponse(response);
				myResponse.validate(myRequest, recordIDs);
			}
		}
	}

	public boolean IsJson(Response testResponse) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject myJsonObject = null;
		myJsonObject = (JSONObject) obj1;
		obj1 = parser.parse(testResponse.asString());
		if (obj1 instanceof JSONObject) {
			return true;
		} else {
			return false;
		}
	}

	public String getUserToken(ArrayList<UserTokens> UserTokensArray,
			String requestUser) {
		String foundToken = "Notfound";

		for (int i = 0; i < UserTokensArray.size(); i++) {
			UserTokens userToken = UserTokensArray.get(i);
			if (userToken.userName.equals(requestUser)) {
				foundToken = userToken.userToken;
				break;
			}
		}
		return foundToken;
	}

	public void deleteModules(String readFile,
			ArrayList<UserTokens> UserTokensArray) throws ParseException,
			FileNotFoundException, IOException, JSONException {
		// Delete all records that were created in this test
		JSONArray myModuleArray;
		jsonObject = getJson(readFile);
		myModuleArray = (JSONArray) jsonObject.get("modules_used");
		deleteRecords(myModuleArray, UserTokensArray);
	}

	public void deleteTeam(String teamName, String adminUserID)
			throws ParseException {
		Object obj1 = null;
		JSONObject myJson = null;
		JSONParser parser = new JSONParser();
		Response response;

		response = given().header("OAuth-Token", adminUserID).expect()
				.statusCode(200).when()
				.get("Teams?filter[0][name][$equals]=" + teamName);

		obj1 = parser.parse(response.asString());
		myJson = (JSONObject) obj1;

		JSONArray myJsonArrayRecords = (JSONArray) myJson.get("records");

		Iterator<JSONObject> iteratorRecords = myJsonArrayRecords.iterator();
		while (iteratorRecords.hasNext()) {
			JSONObject jsonObjectRecord = (JSONObject) iteratorRecords.next();
			String teamID = (String) jsonObjectRecord.get("id").toString();

			response = given().header("OAuth-Token", adminUserID).expect()
					.statusCode(200).when().delete("Teams/" + teamID);

		}
	}

	public void enableBugsModule(String adminUserID) {
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
				.formParam("module", "Administration")
				.formParam("user_edit_tabs", "1")
				.formParam("button", "Save")
				.formParam("disabled_tabs",
						"[\"project\",\"bugs\",\"products\",\"contracts\"]")
				.formParam(
						"enabled_tabs",
						"[\"Accounts\",\"Calendar\",\"Cases\",\"Contacts\",\"Opportunities\",\"Bugs\",\"Leads\",\"Reports\",\"Quotes\",\"Documents\",\"Emails\",\"Campaigns\",\"Calls\",\"Meetings\",\"Tasks\",\"Notes\",\"Forecasts\",\"Prospects\",\"ProspectLists\",\"RevenueLineItems\"]")
				.formParam("action", "SaveTabs").when().post("index.php");

		// Restore REST basePath

		RestAssured.basePath = oldBasePath;
	}

	public void restoreDisplayedModules(String adminUserID) {
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
				.formParam("module", "Administration")
				.formParam("user_edit_tabs", "1")
				.formParam("button", "Save")
				.formParam("disabled_tabs",
						"[\"project\",\"bugs\",\"products\",\"contracts\"]")
				.formParam(
						"enabled_tabs",
						"[\"Accounts\",\"Calendar\",\"Cases\",\"Contacts\",\"Opportunities\",\"Leads\",\"Reports\",\"Quotes\",\"Documents\",\"Emails\",\"Campaigns\",\"Calls\",\"Meetings\",\"Tasks\",\"Notes\",\"Forecasts\",\"Prospects\",\"ProspectLists\",\"RevenueLineItems\"]")
				.formParam("action", "SaveTabs").when().post("index.php");

		// Restore REST basePath

		RestAssured.basePath = oldBasePath;
	}

}
