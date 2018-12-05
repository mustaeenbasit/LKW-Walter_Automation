package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_26999 extends SugarTest {
	DataSource meetingData;

	public void setup() throws Exception {
		// Multiple meeting records
		meetingData = testData.get(testName);
		sugar().meetings.api.create(meetingData);
		sugar().login();
	}

	/**
	 * Verify that Delete from meeting list view works correctly 
	 * @throws Exception
	 */
	@Test
	public void Meetings_26999_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();

		// check 2nd and 3rd record to delete
		sugar().meetings.listView.checkRecord(2);
		sugar().meetings.listView.checkRecord(3);
		sugar().meetings.listView.openActionDropdown();
		sugar().meetings.listView.delete();

		// Verify warning message to appear while deleting records
		sugar().alerts.getWarning().assertContains(testData.get(testName+"_alert").get(0).get("warning_msg"), true);
		sugar().alerts.getWarning().confirmAlert();

		// Verify success message appears after deletion of records
		sugar().alerts.getSuccess().assertContains(testData.get(testName+"_alert").get(0).get("success_msg"), true);

		// Verify there is only 1 record in listview
		sugar().meetings.listView.verifyField(1, "name", meetingData.get(2).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}