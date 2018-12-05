package com.sugarcrm.test.meetings;

import static org.junit.Assert.assertTrue;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27662 extends SugarTest {	
	FieldSet meetingData = new FieldSet();
	String todaysDate = "";
	String dateAfter3Days = "";
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	Calendar cal = Calendar.getInstance();;

	public void setup() throws Exception {
		meetingData = testData.get(testName).get(0);

		// Set Date for today and two days after today.
		todaysDate = sdf.format(date);
		cal.setTime(date);
		cal.add(Calendar.DATE, 3);
		date = cal.getTime();
		dateAfter3Days = sdf.format(date);
		cal.add(Calendar.DATE, -3);
		sugar().login();
	}

	/**
	 * Verify that changing between Repeat Until & Repeat Occurrences fields in Edit meeting
	 * @throws Exception
	 */
	@Test
	public void Meetings_27662_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Meetings and create a new Meeting.
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		// Select "Daily" in "Repeat Type" and "Repeat Until" set 2 days after today.
		sugar().meetings.createDrawer.getEditField("repeatType").set(meetingData.get("repeat_type_daily"));
		sugar().meetings.createDrawer.getEditField("repeatUntil").set(dateAfter3Days);

		// Fill in all required fields and click on Save.
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("date_start_date").set(todaysDate);
		sugar().meetings.createDrawer.save();
		
		// Verify meetings count on list view
		assertTrue("Meetings count is not three on list view", sugar().meetings.listView.countRows() == 4);

		// Verify that 4 daily repeated meetings are created. The 1st meeting has Start Date as today. The 2nd meeting has Start Date as tomorrow and so on. 
		String dateForTestMeeting = "";
		sugar().meetings.listView.sortBy("headerDatestart", true);
		for (int i = 0; i < 4; i++) {
			cal.add(Calendar.DATE, i);
			date = cal.getTime();
			dateForTestMeeting = sdf.format(date);
			sugar().meetings.listView.getDetailField(i + 1, "date_start_date").assertContains(dateForTestMeeting, true);
			cal.add(Calendar.DATE, -i);
		}

		// Open the first meeting and Edit All Recurrence.
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		sugar().meetings.recordView.getControl("editAllReocurrences").click();
		VoodooUtils.waitForReady();
		
		// Change Repeat Type to yearly and select 4 in "Repeat Occurrence" 
		sugar().meetings.recordView.getEditField("repeatType").set(meetingData.get("repeat_type_yearly"));
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(meetingData.get("repeatOccurencesType"));
		sugar().meetings.recordView.getEditField("repeatOccur").set(meetingData.get("repeat_occurrence"));
		
		// Save it.
		sugar().meetings.recordView.save();
		
		// Navigate to meeting list view and sort by start date ascending
		sugar().meetings.navToListView();
		sugar().meetings.listView.sortBy("headerDatestart", true);

		// 4 Yearly repeated meetings are created.The 1st meeting has Start Date as today.The 2nd meeting has Start Date in next year with same Start Time of 1st meeting, and so on.
		for (int i = 0; i < 4; i++) {
			cal.add(Calendar.YEAR, i);
			date = cal.getTime();
			dateForTestMeeting = sdf.format(date);
			sugar().meetings.listView.getDetailField(i + 1, "date_start_date").assertContains(dateForTestMeeting, true);
			cal.add(Calendar.YEAR, -i);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}