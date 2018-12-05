package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22650 extends SugarTest {
	StandardSubpanel meetingsSubpanel;
	MeetingRecord myMeeting;

	public void setup() throws Exception {
		sugar().leads.api.create();
		myMeeting = (MeetingRecord)sugar().meetings.api.create();
		sugar().login();

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		meetingsSubpanel = sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.linkExistingRecord(myMeeting);
	}

	/**
	 * 22650 Verify that editing scheduled meeting related to a lead can be canceled
	 * @throws Exception
	 */
	@Test
	public void Leads_22650_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "edit" link for a scheduled meeting record in "Activities" sub-panel.
		meetingsSubpanel.editRecord(1);

		// TODO: VOOD-503
		// Edit subject of the meeting
		new VoodooControl("input", "css", "input[name='name']").set(testName);

		// Cancel editing the information of the meeting for the selected lead.
		meetingsSubpanel.cancelAction(1);

		// The information of the meeting is not changed. 
		FieldSet meetingName = new FieldSet();
		meetingName.put("name", testName);
		meetingsSubpanel.verify(1, meetingName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}