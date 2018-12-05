package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27575 extends SugarTest {
	MeetingRecord myMeeting;
	
	public void setup() throws Exception {
		sugar().login();
		myMeeting = (MeetingRecord) sugar().meetings.create();
	}

	/**
	 * Verify that no warning appear when user navigates away from edit mode nothing is edited
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27575_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		myMeeting.navToRecord();
		sugar().meetings.recordView.edit();
		
		// Verify that Opportunity list view loads, no any warn message. 
		sugar().opportunities.navToListView();

		sugar().alerts.assertContains("Warning", false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}