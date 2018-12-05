package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27301 extends SugarTest {
	FieldSet meetingRecord = new FieldSet();
	String firstMeetingDate, nextMeetingDate = "";

	public void setup() throws Exception {
		// Initializing test data
		meetingRecord = testData.get(testName).get(0);

		// Login as admin
		sugar().login();

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.DATE, 1);
		date = c1.getTime();
		firstMeetingDate = sdf.format(date);
		c1.add(Calendar.DATE, 2);
		date = c1.getTime();
		String repeatUntilDate = sdf.format(date);

		c1.add(Calendar.DATE, 3);
		date = c1.getTime();
		nextMeetingDate = sdf.format(date);

		// Navigate to Meetings list view
		sugar().meetings.navToListView();

		// Create a recurring meeting
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(meetingRecord.get("name"));
		sugar().meetings.createDrawer.getEditField("date_start_date").set(firstMeetingDate);
		sugar().meetings.createDrawer.getEditField("repeatType").set(meetingRecord.get("repeatType"));
		sugar().meetings.createDrawer.getEditField("repeatUntil").set(repeatUntilDate);
		sugar().meetings.createDrawer.save();
	}

	/**
	 * Verify that the user is not able to edit the recurring information if not editing all recurrences
	 * @throws Exception
	 */
	@Test
	public void Meetings_27301_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1288 - Need Lib support to identify Meetings/Calls > listView > parent record 
		// Using XPath to select a meeting by unique date
		// Open the parent meeting record view 
		new VoodooControl("a", "xpath", "//*[@id='content']/div/div/div[1]/span/div/div[2]/div[2]/div[2]/div[3]/div[1]/table/tbody/tr[contains(.,'"+ firstMeetingDate +"')]/td[2]/span/div/a").click();
		VoodooUtils.waitForReady();

		// Edit the record (subject, start date) 
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("name").set(testName);
		sugar().meetings.recordView.getEditField("date_start_date").set(nextMeetingDate);
		sugar().meetings.recordView.save();

		// Verify that the subject, Start Date are changed in the parent meeting.
		sugar().meetings.recordView.getDetailField("name").assertContains(testName, true);
		sugar().meetings.recordView.getDetailField("date_start_date").assertContains(nextMeetingDate, true);

		// Navigate to next meeting record
		sugar().meetings.recordView.gotoNextRecord();
		VoodooUtils.waitForReady();

		// Verify that the Subject, Start Date are remaining as original info in the child meeting.
		sugar().meetings.recordView.getDetailField("name").assertContains(meetingRecord.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}