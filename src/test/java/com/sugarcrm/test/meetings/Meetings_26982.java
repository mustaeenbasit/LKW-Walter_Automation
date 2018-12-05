package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Meetings_26982 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Subject, start datetime, end datetime, and team are required fields to save a meeting
	 * @throws Exception
	 */
	@Test
	public void Meetings_26982_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		// Verify "Subject" field shows "Required" and "Start Date" and "End Date" fields are pre-populated with data (e.g Current date ).
		sugar().meetings.createDrawer.getEditField("name").assertAttribute("class", "required", true);
		sugar().meetings.createDrawer.getEditField("date_start_date").assertContains(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);
		sugar().meetings.createDrawer.getEditField("date_end_date").assertContains(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);

		// Verify Subject field become Red and an error message is being displayed
		sugar().meetings.createDrawer.getControl("saveButton").click(); // Save without filling required fields.
		sugar().alerts.getError().assertContains(testData.get(testName).get(0).get("error_msg"), true);
		sugar().alerts.getError().closeAlert();

		// Verify Meeting record gets saved and appears in the Meetings List View
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.save();
		sugar().meetings.listView.verifyField(1, "name", sugar().meetings.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}