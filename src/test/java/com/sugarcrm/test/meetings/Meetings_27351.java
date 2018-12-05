package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27351 extends SugarTest {
	FieldSet meetingsFieldSet; 
	String todaysDate, dateAfter3Days, dateAfter3Months;;
	String startDateForNextMeeting, endDateForNextMeeting;

	public void setup() throws Exception {
		meetingsFieldSet = testData.get(testName).get(0);

		// Date of three days after today.
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		todaysDate = sdf.format(date);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.DATE, 3);
		date = c1.getTime();
		dateAfter3Days = sdf.format(date);
		
		// Start and end date of next meeting
		c1.add(Calendar.DATE, 4);
		date = c1.getTime();
		startDateForNextMeeting = sdf.format(date);
		c1.add(Calendar.DATE, 3);
		date = c1.getTime();
		endDateForNextMeeting = sdf.format(date);

		// Date after 3 months
		c1.add(Calendar.MONTH, 3);
		date = c1.getTime();
		dateAfter3Months = sdf.format(date);

		// Create a meeting (Start date is current date)
		FieldSet fs = new FieldSet();
		fs.put("date_start_date", todaysDate);
		sugar().meetings.api.create(fs);
				
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that duration is longer than 1 day can be saved in Meeting.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27351_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit the meeting to a recurring meeting. Start Date is current day. End Date is 3 days after Start Date.
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("repeatType").set("Weekly");
		sugar().meetings.recordView.getEditField("repeatUntil").set("dateAfter3Months");
		sugar().meetings.recordView.getEditField("date_start_time").set(meetingsFieldSet.get("start_time"));
		sugar().meetings.recordView.getEditField("date_end_date").set(dateAfter3Days);
		sugar().meetings.recordView.getEditField("date_end_time").set(meetingsFieldSet.get("end_time"));
		sugar().meetings.recordView.save();

		// Verify start date/time and end date/time of the meeting
		sugar().meetings.recordView.getDetailField("date_start_date").assertContains(todaysDate, true);
		sugar().meetings.recordView.getDetailField("date_start_time").assertContains(meetingsFieldSet.get("start_time"), true);
		sugar().meetings.recordView.getDetailField("date_end_date").assertContains(dateAfter3Days, true);
		sugar().meetings.recordView.getDetailField("date_end_time").assertContains(meetingsFieldSet.get("end_time"), true);
		
		// TODO: VOOD-1288
		// Go to listview of the Meetings.Select a child meeting and open the record view
		// Using XPath to select meetings by unique date
		sugar().meetings.navToListView();
		new VoodooControl("a", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+startDateForNextMeeting+"')]/td[2]/span/div/a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify start date/time and end date/time of the meeting
		sugar().meetings.recordView.getDetailField("date_start_date").assertContains(startDateForNextMeeting, true);
		sugar().meetings.recordView.getDetailField("date_start_time").assertContains(meetingsFieldSet.get("start_time"), true);
		sugar().meetings.recordView.getDetailField("date_end_date").assertContains(endDateForNextMeeting, true);
		sugar().meetings.recordView.getDetailField("date_end_time").assertContains(meetingsFieldSet.get("end_time"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}