package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.MeetingRecord;

public class Meetings_update extends SugarTest {
	MeetingRecord myMeeting;

	public void setup() throws Exception {
		myMeeting = (MeetingRecord)sugar().meetings.api.create();
		sugar().login();
	}

	@Test
	public void Meetings_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "Another Meeting!");

		// Edit the meeting using the UI.
		myMeeting.edit(newData);
		
		// Verify the meeting was edited.
		myMeeting.verify(newData);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
} 