package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22649 extends SugarTest {
	StandardSubpanel meetingSubpanel;

	public void setup() throws Exception {
		// Create Lead and Meetings record
		sugar().leads.api.create();
		MeetingRecord myMeeting = (MeetingRecord)sugar().meetings.api.create();

		//  Login to system as valid user.
		sugar().login();
		
		// Meeting records which are scheduled for a lead exist.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		meetingSubpanel = sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingSubpanel.linkExistingRecord(myMeeting);
	}

	/**
	 * Edit Scheduled Meeting_Verify that scheduled meeting related to a lead can be modified when using "Edit" function in "Activities" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Leads_22649_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Leads" tab on navigation bar.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Click "edit" link for a scheduled meeting record in "Activities"  meeting sub-panel.
		meetingSubpanel.editRecord(1);

		// Modify all the fields of the meeting and save it.
		FieldSet editMeetingData = testData.get(testName).get(0);
		meetingSubpanel.getEditField(1, "name").set(editMeetingData.get("fullName"));
		meetingSubpanel.getEditField(1, "status").set(editMeetingData.get("status"));
		meetingSubpanel.saveAction(1);

		// The modified meeting is displayed in the lead detail view correctly.
		meetingSubpanel.getDetailField(1, "name").assertEquals(editMeetingData.get("fullName"), true);
		meetingSubpanel.getDetailField(1, "status").assertEquals(editMeetingData.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}