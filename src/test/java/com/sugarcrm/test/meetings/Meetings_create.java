package com.sugarcrm.test.meetings;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Meetings_create extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void Meetings_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		MeetingRecord myMeeting = (MeetingRecord) sugar().meetings.create();
		// Using custom field from CSV for duration since it is different from editview '2h 0m' vs '2 hours'
		myMeeting.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 