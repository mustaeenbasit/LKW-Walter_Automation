package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_23282 extends SugarTest {
	DataSource ds = new DataSource();
	
	public void setup() throws Exception {
		ds = testData.get(testName);
		FieldSet accountName = new FieldSet();
		
		// Create Accounts Records
		for (int i = 0; i < ds.size(); i++) {
			accountName.put("name", testName+"_"+i);
			sugar().accounts.api.create(accountName);
			accountName.clear();
		}
		
		sugar().login();
		
		// Create Cases Records
		// TODO:VOOD-444
		sugar().cases.create(ds);
	}

	/**
	 * Sort List_Verify that cases can be sorted by column titles in case list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Cases_23282_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource headersName = testData.get(testName+"_fields");
		
		// Navigate to Cases Listview
		sugar().cases.navToListView();
		
		for (int i = 0; i < 7; i++) {
			// Sort column title in Ascending order
			sugar().cases.listView.sortBy(headersName.get(i).get("listHeadersName"), true);
			
			// Verify that cases are sorted according to the column titles in Ascending order
			for (int j = 0; j < ds.size(); j++) {
				sugar().cases.listView.verifyField((j+1), "name", ds.get(j).get("name"));
			}
			
			// Sort column title in Descending order
			sugar().cases.listView.sortBy(headersName.get(i).get("listHeadersName"), false);
			
			// Verify that cases are sorted according to the column titles in Descending order
			for (int j = 0; j < ds.size(); j++) {
				sugar().cases.listView.verifyField((3-j), "name", ds.get(j).get("name"));
			}
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
