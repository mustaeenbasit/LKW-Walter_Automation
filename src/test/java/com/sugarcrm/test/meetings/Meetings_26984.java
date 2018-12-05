package com.sugarcrm.test.meetings;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_26984 extends SugarTest {
	FieldSet meetingData;

	public void setup() throws Exception {
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that the default values for Status, Repeat and Assigned To fields
	 * @throws Exception
	 */
	@Test
	public void Meetings_26984_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Meetings record save with required fields
		sugar().navbar.selectMenuItem(sugar().meetings, "create" + sugar().meetings.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.save();

		// Open the meeting record view to look at the values in these 3 fields - Status, Repeat, and Assigned To
		sugar().meetings.listView.clickRecord(1);

		// Verify Subject name, Status:Scheduled, Repeat Type:Blank, Assigned To: QAUser
		sugar().meetings.recordView.getDetailField("name").assertEquals(sugar().meetings.getDefaultData().get("name"), true);
		sugar().meetings.recordView.getDetailField("status").assertEquals(sugar().meetings.getDefaultData().get("status"), true);
		sugar().meetings.recordView.getDetailField("assignedTo").assertEquals(sugar().users.getQAUser().get("userName"), true);
		sugar().meetings.recordView.getDetailField("repeatType").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}