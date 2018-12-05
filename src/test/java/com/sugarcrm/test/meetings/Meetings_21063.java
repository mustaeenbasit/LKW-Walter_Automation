package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21063 extends SugarTest {
	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify that the meeting's status is changed automatically after clicking the "Close and Create New" button. 
	 * @throws Exception
	 */
	@Test
	public void Meetings_21063_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		String meetingStatus = sugar().meetings.getDefaultData().get("status");
		VoodooControl status = sugar().meetings.recordView.getDetailField("status");

		// Verify Meeting record is 'Planned/Scheduled'
		status.assertEquals(meetingStatus, true);

		// Edit meeting record with close and create New dropdown action
		sugar().meetings.recordView.closeAndCreateNew();

		// Verify Meeting record is 'Planned/Scheduled' in create drawer
		sugar().meetings.createDrawer.getEditField("status").assertEquals(meetingStatus, true);

		// Verify Meeting record is 'Held'
		sugar().meetings.createDrawer.cancel();
		status.assertEquals(fs.get("status"), true);

		// Verify time and duration hour is the same as meeting scheduled
		sugar().meetings.recordView.getDetailField("date_start_time").assertEquals(fs.get("date_time_duration"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 