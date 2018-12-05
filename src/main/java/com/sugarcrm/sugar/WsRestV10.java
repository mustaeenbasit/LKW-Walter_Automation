package com.sugarcrm.sugar;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.webservices.WS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.io.File;

/**
 * WsRestV10 encapsulates REST calls for Sugar CRUD functions. The class targets
 * sugar7 V10 REST api.
 * 
 * @author Soon Han
 */
public class WsRestV10 {

	private String url;
	private String username;
	private String password;
	private String token;
    private Logger logger;
	private Map<String, String> payload = new LinkedHashMap<String, String>();

	private ArrayList<HashMap<String, String>> listOfPostHeaders = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> postHeaders = new HashMap<String, String>();

	/*
		Be careful. To call REST API correctly at the moment, you need to instantiate a new
		WsRestV10() because if only one instance is used over the course of the whole test
		run, the access token might expire during the test
	 */
	public WsRestV10() {
		this.logger = getLogger();

		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		loadBasicParams();
		this.token = getToken(this.username, this.password);
	}

	private Logger getLogger() {
		try {
            this.logger = Logger.getLogger(WsRestV10.class.getName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return logger;
	}

	/**
	 * Load basic parameters such as the Sugar url, username, password, sugar
	 * REST entry point, etc.
	 */
	private void loadBasicParams() {
		final String currentWorkingPath = System.getProperty("user.dir");
		final String relativeResourcesPath = File.separator + "src"
				+ File.separator + "test" + File.separator + "resources"
				+ File.separator;

		String grimoirePropsPath = currentWorkingPath + relativeResourcesPath
				+ "grimoire.properties";
		Configuration grimoireConfig = new Configuration();
		try {
			grimoireConfig.load(new File(grimoirePropsPath));
			this.url = new SugarUrl().getRestUrl();
			this.username = grimoireConfig.getValue("sugar_api_user", "admin2");
			this.password = grimoireConfig.getValue("sugar_api_pass", "asdf");
			this.logger.info("REST entry point = " + this.url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create module records
	 *
	 * @param	module	name of the module in which to create the 
	 * @param	listOfMaps	as Map<String, Object>
	 * @return	result as Map<String, Object>
	 */
	public ArrayList<HashMap<String, Object>> create(String module, ArrayList<HashMap<String, String>> listOfMaps) {
		String endpoint = module;
		ArrayList<HashMap<String, Object>> listOfMapsResult = new ArrayList<HashMap<String, Object>>();

		for(HashMap<String, String> hashMap : listOfMaps) {
			Map<String, String> payload = new LinkedHashMap<String, String>();
			payload.putAll(hashMap);
			logger.info(payload.toString());
			Map<String, Object> result = postRequest(WS.OP.POST, endpoint, payload);
			listOfMapsResult.add((HashMap<String, Object>) result);
		}

		return listOfMapsResult;
	}

	/**
	 * Log a message in sugarcrm.log.
	 *
	 * @param	level	a String specifying the log level.
	 * @param	message	a String specifying the message to log.
	 * @param	channel	a String representing the channel to tag the message with.
	 * @return	result	boolean true for success, false otherwise
	 */
	public boolean log(String level, String message, String channel) {
		String endpoint = "logger";
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, String> payload = new LinkedHashMap<String, String>();
		payload.put("level", level);
		payload.put("message", message);
		payload.put("channel", channel);
		result = postRequest(WS.OP.POST, endpoint, payload);
		return (Boolean)result.get("status");
	}

	/**
	 * Post the request
	 * 
	 * @return results in Map<String, Object>
	 */
	private Map<String, Object> postRequest(WS.OP op, String endPoint, Map<String, String> payload) {

		Map<String, Object> mapParse = null;

		String restEntry = this.url + "/" + endPoint;

		postHeaders.clear();
		listOfPostHeaders.clear();
		postHeaders.put("Accept", "application/json");
		postHeaders.put("OAuth-Token", this.token);
		listOfPostHeaders.add(postHeaders);

		try {
			mapParse = WS.request(op, restEntry, payload, null, listOfPostHeaders);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapParse;
	}

	/**
	 * Get the token for the rest session
	 * 
	 * @param username
	 *            as String
	 * @param password
	 *            as String
	 * @return token as String
	 */
	private String getToken(String username, String password) {

		Map<String, String> payload = fillTokenPayload();
		Map<String, Object> mapParse = postRequest(WS.OP.POST, "oauth2/token", payload);

		this.token = (String) mapParse.get("access_token");
		return this.token;
	}

	/**
	 * Fill in the parameter values required for obtaining the token.
	 * 
	 * @return payload as Map<String, String>
	 */
	private Map<String, String> fillTokenPayload() {

		// Not sure if order matters, use an ordered map
		Map<String, String> payload = new LinkedHashMap<String, String>();
		payload.put("grant_type", "password");
		payload.put("username", this.username);
		payload.put("password", this.password); // no md5 encryption
		payload.put("client_id", "sugar");
		payload.put("client_secret", "");
		payload.put("platform", "base");

		return payload;
	}

	/**
	 * Add the token to the payload
	 * 
	 * @return payload as Map<String, String>
	 */
	private Map<String, String> addTokenToPayload() {

		payload = new LinkedHashMap<String, String>();
		// Strange, requires "OAuth-Token" in both the payload and POST header.
		// Otherwise, got HTTP 401 error
		payload.put("OAuth-Token", this.token);

		return payload;
	}

	/**
	 * Get records of a module
	 * 
	 * @param module
	 *            as String
	 * @return POST request results as an ArrayList<Map<String, Object>>
	 */
	private ArrayList<Map<String, Object>> getRecords(String module) {
		Map<String, String> payload = addTokenToPayload();
		Map<String, Object> mapParse = postRequest(WS.OP.GET, module, payload);

		@SuppressWarnings("unchecked")
		ArrayList<Map<String, Object>> arrList = (ArrayList<Map<String, Object>>) mapParse
				.get("records");

		return arrList;
	}

	/**
	 * Get record ids of a module
	 * 
	 * @param module
	 *            as String
	 * @return ids in an ArrayList<String>
	 */
	private ArrayList<String> getRecordsIds(String module) {
		ArrayList<Map<String, Object>> records = getRecords(module);

		String attrib = "id";
		ArrayList<String> myArrList = new ArrayList<String>();
		for (Map<String, Object> entry : records) {
			for (String key : entry.keySet()) {
				if (key.equals(attrib)) {
					myArrList.add((String) entry.get(key));
				}
			}
		}

		return myArrList;
	}

	/**
	 * Delete a record specified by record id and module
	 * 
	 * @param module
	 *            as String and id as String
	 * @return id of deleted record. id is a String
	 */
	private String deleteRecord(String module, String id) {
		String endpoint = module + "/" + id;
		Map<String, String> payload = addTokenToPayload();
		Map<String, Object> mapParse = postRequest(WS.OP.DELETE, endpoint, payload);

		return (String) mapParse.get("id");
	}

	/**
	 * Delete all records in a specified module
	 * 
	 * @param module
	 *            as String and id as String
	 * @return ids of deleted records, in ArrayList<String>
	 * @throws Exception 
	 */
	public ArrayList<String> deleteAll(String module) throws Exception {

		ArrayList<String> ids;
		ArrayList<String> idsDeleted = new ArrayList<String>();
		
		do {
			ids = getRecordsIds(module);

			for (String id : ids) {
				String idDeleted = deleteRecord(module, id);
				idsDeleted.add(idDeleted);
			}
		} while(getTotalCount(module) > 0);

		return idsDeleted;
	}

	/**
	 * Get count of records in module.
	 * 
	 * @param module	The name of the module to return a count for. 
	 * @return an int representing the number of records in the specified module, to a maximum of 99999.
	 * @throws Exception
	 */
	public int getTotalCount(String module) throws Exception {
		String endpoint = module + "/count";
		Map<String, String> payload = addTokenToPayload();
		Map<String, Object> mapParse = postRequest(WS.OP.GET, endpoint, payload);
		
		return Integer.parseInt((String)mapParse.get("record_count"));
	}

	/**
	 * Determines whether the view is basic opportunities or Revenue Line Items opportunities
	 *
	 * @return	a boolean that indicates the result
	 * @throws Exception
	 */
	public boolean isOpportunitiesView() throws Exception {
		String endpoint = "/Opportunities/config/";
		Map<String, String> payload = addTokenToPayload();

		Map<String, Object> mapParse = postRequest(WS.OP.GET, endpoint, payload);
		String result = (String) mapParse.get("opps_view_by");
		switch(result) {
			case "Opportunities": return true;
			case "RevenueLineItems": return false;
			default: throw new Exception("The REST call " + endpoint + " returns "
					+ result + " instead of Opportunities or RevenueLineItems");
		}
	}

	/**
	 * Switch to Basic Opportunities View if the current view is not already in this view
	 *
	 * @throws Exception
	 */
	public void switchToOpportunitiesView() throws Exception {
		if(!isOpportunitiesView()) {
			toggleOpportunitiesViews("Opportunities");
		}
	}

	/**
	 * Switch to Revenue Line Items View if the current view is not already in this view
	 * 
	 * @throws Exception
	 */
	public void switchToRevenueLineItemsView() throws Exception {
		if(isOpportunitiesView()) {
			toggleOpportunitiesViews("RevenueLineItems");
		}
	}

	/**
	 * Resets Sugar to an empty state.
	 *
	 * This works using two parts:
	 *
	 * <ul>
	 * <li>We created a service that listens on port 5000 on the Sugar
	 * instance.</li>
	 * <li>When we deploy Sugar on CI, we also create a copy of the
	 * deployment and the database which we leave untouched. </li>
	 * </ul>
	 *
	 * When the service receives a HTTP Request with body
	 * {"instance":"instanceName"}
	 * It restores both the filesystem and the database to their
	 * untouched state. This generally shouldn't take more than
	 * 10 seconds, depending on how much in the filesystem was
	 * changed
	 *
	 * @throws Exception
	 */
	public void restoreSugar() throws Exception {
		final String restEntry = new SugarUrl().getRestoreUrl();
		// The instance name is everything after fqdn:port
		// http://fqdn:port/instanceName -> instanceName
		final String instanceName = url.split("/")[3];

		logger.info("Making api call to " + restEntry);
		postHeaders.clear();
		postHeaders.put("Content-Type", "application/json");
		final Map<String, Object> body = new HashMap<>();
		body.put("instance", instanceName);

		final Map <String, Object> mapParse = WS.request(WS.OP.POST, restEntry, postHeaders, body);
		/* If the call succeeded, the service sends back
		 * {
		 *     "success":"true"
		 * }
		 * If the call failed, we'll get
		 * {
		 *     "success":"false",
		 *     "error":"reasonForError"
		 * }
		 * So if we get that, we parse the error, then throw
		 */
		if (!"true".equals(mapParse.get("success").toString())) {
			throw new Exception("Restoring Sugar failed: " + mapParse.get("error"));
		}
	}

	/**
	 * Toggle between basic opportunities view and opportunities revenue line items view based on the parameter's value
	 *
	 * @param newView This is the view you want to switch to. Valid values are "Opportunities" or "RevenueLineItems"
	 * @throws Exception
	 */
	private void toggleOpportunitiesViews(String newView) throws Exception {
		Map<String, String> payload = addTokenToPayload();
		payload.put("opps_closedate_rollup", "latest");
		payload.put("opps_view_by", newView);

		Map<String, Object> mapParse = postRequest(WS.OP.POST, "/Opportunities/config/", payload);
		String result = (String) mapParse.get("opps_view_by");
		if(!result.equals(newView)) {
			throw new Exception("switchToOpportunitiesView() was unsuccessful (opps_view_by != " + newView + ")");
		}
	}

	/**
	 * Print list of maps. Used for debugging only
	 * 
	 * @param arrList
	 *            as ArrayList<Map<String, Object>>
	 */
	@SuppressWarnings("unused")
	private void printListOfMaps(ArrayList<Map<String, Object>> arrList) {
		for (Map<String, Object> entry : arrList) {
			for (Map.Entry<String, Object> item : entry.entrySet()) {
				String key = item.getKey();
				Object value = item.getValue();
				System.out.println("printListOfMaps(): key = " + key
						+ "  value = " + value);
			}
		}
	}

	/**
	 * This main() illustrates usage of the class
	 * @param args 
	 * 
	 */	
	public static void main(String[] args) {

		try {
			WsRestV10 rest = new WsRestV10();

			int total = rest.getTotalCount("Accounts");
			System.out.println("Before adding accounts: number of accounts = "
					+ total);

			HashMap<String, String> accountInfo = new HashMap<String, String>();
			accountInfo.put("name", "myName");
			accountInfo.put("account_type", "Special customer");
			accountInfo.put("description", "My Special Example Account");

			ArrayList<HashMap<String, String>> listOfMaps = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < 2; i++) {
				listOfMaps.add(accountInfo);
			}

			rest.create("Accounts", listOfMaps);

			total = rest.getTotalCount("Accounts");
			System.out.println("After adding accounts: number of accounts = "
					+ total);

			rest.deleteAll("Accounts");
			total = rest.getTotalCount("Accounts");
			System.out.println("After delete all accounts: number of accounts = " + total);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
