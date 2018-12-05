package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21060 extends SugarTest {
	DataSource meetingRecords;
	FieldSet fs, customFS;

	public void setup() throws Exception {
		meetingRecords = testData.get(testName);
		customFS = testData.get(testName+"_1").get(0);
		sugar().meetings.api.create(meetingRecords);
		sugar().login();

		// Set max listView items per page to 2
		fs = new FieldSet();
		fs.put("maxEntriesPerPage", customFS.get("itemShowOnListView"));
		sugar().admin.setSystemSettings(fs);
	}

	/**
	 * Verify that corresponding records are displayed in meeting list view when clicking the pagination control link in meeting list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_21060_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();

		// Verify that the meeting records on the first page are displayed.
		sugar().meetings.listView.verifyField(1, "name", meetingRecords.get(5).get("name"));
		sugar().meetings.listView.verifyField(2, "name", meetingRecords.get(4).get("name"));

		VoodooControl showMoreCtrl = sugar().meetings.listView.getControl("showMore");
		showMoreCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the meeting records on the second page are displayed.
		sugar().meetings.listView.verifyField(3, "name", meetingRecords.get(3).get("name"));
		sugar().meetings.listView.verifyField(4, "name", meetingRecords.get(2).get("name"));

		showMoreCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the meeting records on the first page are displayed.
		sugar().meetings.listView.verifyField(5, "name", meetingRecords.get(1).get("name"));
		sugar().meetings.listView.verifyField(6, "name", meetingRecords.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}