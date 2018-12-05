package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class Admin_26974 extends SugarTest {
	FieldSet customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);	
		sugar().calls.api.create();
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify renamed contact is shown correctly in calls/meetings module while rename contacts on the second times
	 * @throws Exception
	 */
	@Test
	public void Admin_26974_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().admin.renameModule(sugar().contacts, customData.get("singularName"), customData.get("pluralName"));
		
		// Verify that renamed calls and meetings modules name are display perfectly on calls/meetings list-view
		sugar().calls.navToListView();
		sugar().calls.listView.assertElementContains(customData.get("acc_name"), true);
		sugar().meetings.navToListView();
		sugar().meetings.listView.assertElementContains(customData.get("acc_name"), true);
		
		// second times rename contact module with different value
		sugar().admin.renameModule(sugar().contacts, customData.get("singularName2"), customData.get("pluralName2"));
		
		// Verify that Second time renamed calls and meetings modules name are display perfectly on calls/meetings list-view
		sugar().calls.navToListView();
		sugar().calls.listView.assertElementContains(customData.get("acc_name2"), true);
		sugar().meetings.navToListView();
		sugar().meetings.listView.assertElementContains(customData.get("acc_name2"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}