package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Users_29987 extends SugarTest {
	StandardSubpanel meetingsSubpanel;

	public void setup() throws Exception {
		sugar().login();

		sugar().cases.api.create();
		MeetingRecord myMeeting = (MeetingRecord) sugar().meetings.api.create();

		// Link meeting record to the case
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		meetingsSubpanel = sugar().cases.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.scrollIntoViewIfNeeded(false);
		meetingsSubpanel.linkExistingRecord(myMeeting);
	}

	/**
	 * Verify that Inline editing of Assigned user from subpanel is working properly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_29987_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet meetingData = new FieldSet();
		meetingData.put("assignedTo", sugar().users.getQAUser().get("userName"));

		// Already on Meetings recordview
		meetingsSubpanel.scrollIntoViewIfNeeded(false);
		meetingsSubpanel.expandSubpanel();

		// Update assigned to user in Meetings subpanel
		meetingsSubpanel.editRecord(1, meetingData);

		// Verify that new user now appears in the Meetings subpanel
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		meetingsSubpanel = sugar().cases.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.scrollIntoViewIfNeeded(false);
		meetingsSubpanel.verify(1, meetingData, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
