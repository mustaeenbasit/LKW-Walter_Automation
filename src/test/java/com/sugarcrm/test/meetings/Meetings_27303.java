package com.sugarcrm.test.meetings;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27303 extends SugarTest {
	
	public void setup() throws Exception {
		// Login with qauser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that Repeat type field is editable if meeting is not already recurring - user can set recurrence just as if they were creating a new meeting.
	 * @throws Exception
	 */
	@Test
	public void Meetings_27303_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Initializing test data
		FieldSet customData = testData.get(testName).get(0);
				
		// Calculating the values for Current date & Repeat Until date
		DateTime date = DateTime.now();
		String currentYearDate = date.toString("MM/dd/yyyy");
		String yearNextDate = date.plusYears(1).toString("MM/dd/yyyy");
		
		// Create meeting record
		FieldSet meetingData = new FieldSet();
		meetingData.put("date_start_date", currentYearDate);
		sugar().meetings.create(meetingData);
		
		// Open the meeting created above
		sugar().meetings.listView.clickRecord(1);
		
		// Edit the meeting: Change Repeat type to yearly and repeat = 2 
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("repeatType").set(customData.get("repeat_type"));
		VoodooUtils.waitForReady();
		String repeatCount = customData.get("repeat_count");
		sugar().meetings.recordView.getEditField("repeatOccur").set(repeatCount);
		sugar().meetings.recordView.save();
		
		// Navigate back to Meetings List View
		sugar().meetings.navToListView();
		
		// Verify that 2 meeting records are displayed
		Assert.assertEquals("Number of meetings not equal to 2 when it should be.", sugar().meetings.listView.countRows(), Integer.parseInt(repeatCount));
		
		// Sorting meeting records w.r.t Start Dates
		sugar().meetings.listView.sortBy("headerDatestart", false);
		
		String startTime = sugar().meetings.getDefaultData().get("date_start_time");
		
		// Verify a record in list view is displayed for the next year with the same time as in present year
		sugar().meetings.listView.getDetailField(1, "date_start_date").assertEquals(yearNextDate + " " + startTime, true);
		
		// Verify a record in list view is displayed for the present year with same time as saved before
		sugar().meetings.listView.getDetailField(2, "date_start_date").assertEquals(currentYearDate + " " + startTime, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}