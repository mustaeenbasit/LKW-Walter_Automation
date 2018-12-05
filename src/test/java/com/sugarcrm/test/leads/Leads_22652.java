package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22652 extends SugarTest{
	StandardSubpanel meetingSubpanel;

	public void setup() throws Exception {
		// Create Lead and Meetings record
		sugar().leads.api.create();
		MeetingRecord myMeeting = (MeetingRecord)sugar().meetings.api.create();

		// Login to system as valid user.
		sugar().login();
		
		// Meeting records which are scheduled for a lead exist.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		meetingSubpanel = sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingSubpanel.linkExistingRecord(myMeeting);
	}

	/**
	 * Remove Scheduled Meeting_Verify that meeting can be canceled removing from the lead detail view when using "rem" function.
	 * @throws Exception
	 */
	@Test
	public void Leads_22652_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Click "Leads" tab on navigation bar.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		
		// Click the unlink button from action drop down of a meeting record in meeting sub-panel.
		meetingSubpanel.expandSubpanelRowActions(1);
		meetingSubpanel.getControl("unlinkActionRow01").click();
		
		// Click "Cancel" button on the pop-up dialog box.
		sugar().alerts.getWarning().cancelAlert();
		
		// Verify The meeting is not removed from meetings sub-panel.
		Assert.assertTrue("Meetings record does not contain one record.", meetingSubpanel.countRows() == 1);
		meetingSubpanel.getDetailField(1, "name").assertEquals(sugar().meetings.getDefaultData().get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
