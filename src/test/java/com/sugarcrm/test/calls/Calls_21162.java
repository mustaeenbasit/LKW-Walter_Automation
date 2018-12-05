package com.sugarcrm.test.calls;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class Calls_21162 extends SugarTest {
	ArrayList<Record> myCalls;
	DataSource callDS, dropdownDS;

	public void setup() throws Exception {
		callDS = testData.get(testName);
		dropdownDS = testData.get(testName+"_1");
		myCalls = sugar.calls.api.create(callDS);
		sugar.login();
	}

	/**
	 * New action dropdown list in calls detail view page. 
	 * @throws Exception
	 */
	@Test
	public void Calls_21162_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		for(Record call: myCalls) {
			call.navToRecord();
			sugar.calls.recordView.openPrimaryButtonDropdown();
			sugar.calls.recordView.getControl("editButton").assertVisible(true);
			sugar.calls.recordView.getControl("editButton").assertEquals(dropdownDS.get(0).get("dropdown_menu_action"), true);
			sugar.calls.recordView.getControl("deleteButton").assertVisible(true);
			sugar.calls.recordView.getControl("deleteButton").assertEquals(dropdownDS.get(2).get("dropdown_menu_action"), true);
			sugar.calls.recordView.getControl("copyButton").assertVisible(true);
			sugar.calls.recordView.getControl("copyButton").assertEquals(dropdownDS.get(1).get("dropdown_menu_action"), true);
			sugar.calls.recordView.getControl("share").assertVisible(true);
			sugar.calls.recordView.getControl("share").assertEquals(dropdownDS.get(3).get("dropdown_menu_action"), true);

			// if status is not held, close and close create new button not appears
			if(call.get("status").equals(callDS.get(0).get("status"))) {
				sugar.calls.recordView.getControl("closeAndCreateNew").assertVisible(false);
				sugar.calls.recordView.getControl("close").assertVisible(false);
			}
			else {
				sugar.calls.recordView.getControl("closeAndCreateNew").assertVisible(true);
				sugar.calls.recordView.getControl("closeAndCreateNew").assertEquals(dropdownDS.get(5).get("dropdown_menu_action"), true);
				sugar.calls.recordView.getControl("close").assertVisible(true);
				sugar.calls.recordView.getControl("close").assertEquals(dropdownDS.get(4).get("dropdown_menu_action"), true);
			}

			// Trigger delete menu action from dropdown
			sugar.calls.recordView.getControl("deleteButton").click();
			sugar.alerts.getWarning().confirmAlert();
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}