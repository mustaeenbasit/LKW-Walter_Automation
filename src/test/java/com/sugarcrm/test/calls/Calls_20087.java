package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_20087 extends SugarTest {
	FieldSet fs = new FieldSet();
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify searches on date fields with different time zones
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_20087_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		customData = testData.get(testName).get(0);
		
		// Set "Time Zone" to something like Asia/Tokyo (GMT +9:00)
		fs.put("advanced_timeZone", customData.get("setTimezone")); 
		sugar().users.setPrefs(fs);
		fs.clear();

		// Create call records
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.save();

		// Create filter
		sugar().calls.listView.getControl("filterDropdown").click();
		sugar().calls.listView.getControl("filterCreateNew").click();

		// Select "Start Date" = Last 7 days
		// TODO: VOOD-1478
		new VoodooSelect("div", "css", "div.filter-definition-container div:nth-child(1) div div[data-filter='field']").set(customData.get("startDate"));
		new VoodooSelect("div", "css",  "div.filter-definition-container div:nth-child(1) div div[data-filter='operator']").set(customData.get("last7Days"));
		VoodooUtils.waitForReady();

		// Verify that the started date are respected to the timezone setting
		sugar().calls.listView.verifyField(1, "name", testName);
		sugar().calls.listView.verifyField(1, "date_start_date", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}