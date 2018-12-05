package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_29025 extends SugarTest {
	FieldSet meetingData;

	public void setup() throws Exception {
		meetingData = testData.get(testName).get(0);
		String currentDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");

		// Create a Meeting record with 1 hr duration
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		fs.put("date_start_time", meetingData.get("meeting_start_time"));
		fs.put("date_end_time", meetingData.get("meeting_end_time"));
		fs.put("date_end_date", currentDate);
		fs.put("date_start_date", currentDate);
		sugar().meetings.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify Meeting start/end date-times should reflect correctly in create drawer
	 * inline time line for each invitee
	 * @throws Exception
	 */
	@Test
	public void Meetings_29025_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to meetings record view
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);

		// Verifying for Meeting created timeslot the span contains class 'busy' for both the invitees
		// TODO: VOOD-1699
		for(int i = 1; i < 10; i++) {
			for(int j = 1; j < 5; j++) {
				if(i == 5) {
					new VoodooControl("div", "css", ".participant .times  div:nth-child("+ i +") span:nth-child("+ j +")")
					.assertAttribute("class", meetingData.get("booked_slot"), true);
					new VoodooControl("div", "css", ".participants-schedule div:nth-child(5).participant .times  div:nth-child("+ i +") span:nth-child("+ j +")").
					assertAttribute("class", meetingData.get("booked_slot"), true);
				}
				else {
					new VoodooControl("div", "css", ".participant .times  div:nth-child("+ i +") span:nth-child("+ j +")")
					.assertAttribute("class", meetingData.get("booked_slot"), false);
					new VoodooControl("div", "css", ".participants-schedule div:nth-child(5).participant .times  div:nth-child("+ i +") span:nth-child("+ j +")").
					assertAttribute("class", meetingData.get("booked_slot"), false);
				}
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}