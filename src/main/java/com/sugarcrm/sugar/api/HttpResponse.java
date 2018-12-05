package com.sugarcrm.sugar.api;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static org.junit.Assert.*;

import com.jayway.restassured.response.Response;
import com.sugarcrm.sugar.VoodooUtils;

public class HttpResponse {
	Response response;

	public HttpResponse(Response myResponse) {
		response = myResponse;
	}

	public void validate(HttpRequest myRequest,
			ArrayList<RuntimeRecords> recordIDs) throws JSONException,
			ParseException {
		String validateType = (String) myRequest.validateResponse.get("type")
				.toString();
		switch (validateType) {
		case "single":
			validateSingle(myRequest, recordIDs);
			break;
		case "arrayone":
			validateArrayOne(myRequest, recordIDs);
			break;
		case "arraymany":
			validateArrayMany(myRequest);
			break;
		case "none":
			break;
		default:
			VoodooUtils.voodoo.log.info("UNKOWN validateType " + validateType);

		}
	}

	public void validateSingle(HttpRequest myRequest,
			ArrayList<RuntimeRecords> recordIDs) throws JSONException,
			ParseException {
		String validateValue = (String) myRequest.validateResponse.get("value")
				.toString();
		String identifyID;

		if (myRequest.validateResponse.containsKey("method")) {
			// Contains an Assert other than Json Equals
			validateMethod(myRequest);
		} else {
			JSONAssert.assertEquals(validateValue, response.asString(), false);
		}

		// The whichID has be optionally added - for future it would be
		// better to include it in all cases to avoid the IF Else
		if (myRequest.validateResponse.containsKey("extractID")) {
			if (myRequest.validateResponse.containsKey("whichID")) {
				identifyID = (String) myRequest.validateResponse.get("whichID");
			} else {
				identifyID = "id";
			}
			String recordID = response.path(identifyID);
			// Save the record ID for relationships and to Delete in clean
			// up
			String myModule = (String) myRequest.validateResponse.get(
					"module").toString();
			RuntimeRecords moduleID = new RuntimeRecords("ID", myModule,
					"", recordID);
			recordIDs.add(moduleID);
		}
	}

	public void validateArrayOne(HttpRequest myRequest,
			ArrayList<RuntimeRecords> recordIDs) throws JSONException,
			ParseException {
		Long myCount = (long) 0;
		Long validationCount = (long) 0;
		JSONParser parser = new JSONParser();
		validationCount = (Long) myRequest.validateResponse.get("count");
		Object obj1 = null;
		JSONObject myJsonValidate = null;
		String validateValue = (String) myRequest.validateResponse.get("value")
				.toString();
		String identifyID;

		obj1 = parser.parse(response.asString());

		myJsonValidate = (JSONObject) obj1;
		JSONArray myJsonArrayRecords = (JSONArray) myJsonValidate
				.get("records");
		Iterator<JSONObject> iteratorRecords = myJsonArrayRecords.iterator();

		while (iteratorRecords.hasNext()) {
			myCount++;
			JSONObject jsonObjectRecord = (JSONObject) iteratorRecords.next();

			JSONAssert.assertEquals(validateValue, jsonObjectRecord.toString(),
					false);

			// The whichID has be optionally added - for future it would be
			// better to include it in all cases to avoid the IF Else
			if (myRequest.validateResponse.containsKey("extractID")) {
				if (myRequest.validateResponse.containsKey("whichID")) {
					identifyID = (String) myRequest.validateResponse
							.get("whichID");
				} else {
					identifyID = "id";
				}
				// This is used to retrieve a single Record ID for future use
				String recordID = (String) jsonObjectRecord.get(identifyID)
						.toString();
				VoodooUtils.voodoo.log.info("Extracted Record ID " + recordID);
				// Save the record ID for relationships and to Delete in clean
				// up
				String myModule = (String) myRequest.validateResponse.get(
						"module").toString();
				RuntimeRecords moduleID = new RuntimeRecords("ID", myModule,
						"", recordID);
				recordIDs.add(moduleID);
			}

		}
		assertEquals(validationCount, myCount);
	}

	public void validateArrayMany(HttpRequest myRequest) throws JSONException,
			ParseException {
		String validateValue = "";
		Long myCount = (long) 0;
		Long validationCount = (long) 0;
		JSONParser parser = new JSONParser();
		validationCount = (Long) myRequest.validateResponse.get("count");
		Object obj1 = null;
		JSONObject myJsonValidate = null;

		obj1 = parser.parse(response.asString());
		myJsonValidate = (JSONObject) obj1;
		JSONArray myJsonArrayValidate = (JSONArray) myRequest.validateResponse
				.get("records");
		Iterator<JSONObject> iteratorValidate = myJsonArrayValidate.iterator();
		JSONArray myJsonArrayRecords = (JSONArray) myJsonValidate
				.get("records");

		Iterator<JSONObject> iteratorRecords = myJsonArrayRecords.iterator();
		while (iteratorRecords.hasNext()) {
			myCount++;
			JSONObject jsonObjectRecord = (JSONObject) iteratorRecords.next();
			JSONObject jsonObjectValidations = (JSONObject) iteratorValidate
					.next();
			validateValue = (String) jsonObjectValidations.toString();

			JSONAssert.assertEquals(validateValue, jsonObjectRecord.toString(),
					false);
		}
		assertEquals(validationCount, myCount);
	}

	public void validateMethod(HttpRequest myRequest) throws JSONException,
			ParseException {
		JSONParser parser = new JSONParser();
		Object obj1 = null;
		JSONObject myJsonValidate = null;
		String validateValue = (String) myRequest.validateResponse.get("value")
				.toString();
		obj1 = parser.parse(validateValue);
		myJsonValidate = (JSONObject) obj1;

		JSONArray myJsonArrayValidate = (JSONArray) myJsonValidate
				.get("contains");

		Iterator<JSONObject> iteratorContains = myJsonArrayValidate.iterator();

		while (iteratorContains.hasNext()) {
			JSONObject jsonObjectRecord = (JSONObject) iteratorContains.next();
			validateValue = (String) jsonObjectRecord.get("string");
			assertTrue(response.asString().contains(validateValue));
		}

		myJsonArrayValidate = (JSONArray) myJsonValidate.get("notcontains");
		Iterator<JSONObject> iteratorNotContains = myJsonArrayValidate
				.iterator();
		while (iteratorNotContains.hasNext()) {
			JSONObject jsonObjectRecord = (JSONObject) iteratorNotContains
					.next();
			validateValue = (String) jsonObjectRecord.get("string");
			assertFalse(response.asString().contains(validateValue));
		}
	}
}
