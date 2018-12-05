package com.sugarcrm.test.subpanels;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_26297 extends SugarTest {
	StandardSubpanel meetingsSubpanel;
	DataSource meetings;

	public void setup() throws Exception {
		// Create 2 meeting records where status!="Held"
		meetings = testData.get(testName);
		ArrayList<Record> meetingRecords = sugar.meetings.api.create(meetings);

		// Login as admin user
		sugar.login();

		// Create a contact Record
		// TODO: VOOD-1320 (Record created via UI due to VOOD-1320) 
		sugar.contacts.create();

		// Linking the meeting records with the Contact record
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		meetingsSubpanel = sugar.contacts.recordView.subpanels.get(sugar.meetings.moduleNamePlural);
		meetingsSubpanel.linkExistingRecords(meetingRecords);
	}

	/**
	 * A Meeting can be closed in sub panel
	 * @throws Exception
	 */
	@Test
	public void Subpanels_26297_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet meetingStatus = testData.get(testName + "_status").get(0);

		// Verify that the meeting status!="Held"
		// TODO: VOOD-1424
		meetingsSubpanel.getDetailField(1, "status").assertEquals(meetingStatus.get("statusHeld"), false);
		meetingsSubpanel.getDetailField(1, "name").assertEquals(meetings.get(1).get("name"), true);

		// Click the "Close" option for the meeting in the first row in the sub-panel 
		meetingsSubpanel.click(); // Need to click somewhere else on the body to remove focus so that tool-tip does not hide the required button
		meetingsSubpanel.closeRecord(1);

		// Assert that the meeting status changed to "Held"
		// TODO: VOOD-1424
		meetingsSubpanel.getDetailField(1, "status").assertEquals(meetingStatus.get("statusHeld"), true);

		// Navigating to the record view of the closed meeting
		meetingsSubpanel.clickRecord(1);

		// Assert the name and the status of the closed meeting on the record view
		sugar.meetings.recordView.getDetailField("name").assertEquals(meetings.get(1).get("name"), true);
		sugar.meetings.recordView.getDetailField("status").assertEquals(meetingStatus.get("statusHeld"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}