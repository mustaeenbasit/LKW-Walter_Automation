package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27089 extends SugarTest {

	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify that inline edit Start Date and End Date correctly
	 * @throws Exception
	 */
	@Test
	public void Meetings_27089_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String date1 = sdf.format(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 3);
		date = cal.getTime();
		String date2 = sdf.format(date);

		int hour = cal.get(Calendar.HOUR); // Get current hours
		int min = cal.get(Calendar.MINUTE); // Get current minutes
		int booleanAmPm = cal.get(Calendar.AM_PM); // Get current 0/1 : am/pm
		String amPm = (booleanAmPm == 0) ? "am" : "pm";
		String time = "";
		if(hour <= 0) hour = 12;

		// Set hours and minutes as per requirement
		if(min >= 0 && min < 15) {			
			time = hour+":15"+amPm;
		} else if(min >= 15 && min < 30)
			time = hour+":30"+amPm;
		else if(min >= 30 && min < 45)
			time = hour+":45"+amPm;
		else if(min >= 45 && min < 59) {
			if(hour != 12) hour = (hour+1);
			time = hour+":00pm";
		}

		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		
		// TODO: VOOD-1222 - Need library support to verify fields in ListView and RecordView of Calls/Meetings module
		new VoodooControl ("i", "css", "span[data-name='duration']").hover();
		new VoodooControl ("i", "css", "span[data-name='duration'] .fa.fa-pencil").click();
		sugar().meetings.recordView.getEditField("date_start_date").set(date1);
		sugar().meetings.recordView.getEditField("date_start_time").set(time);
		sugar().meetings.recordView.getEditField("date_end_date").set(date2);
		sugar().meetings.recordView.getEditField("date_end_time").set(time);
		sugar().meetings.recordView.save();

		// Verify newly changed date & time values
		sugar().meetings.recordView.getDetailField("date_start_date").assertContains(date1, true);
		sugar().meetings.recordView.getDetailField("date_start_time").assertContains(time, true);
		sugar().meetings.recordView.getDetailField("date_end_date").assertContains(date2, true);
		sugar().meetings.recordView.getDetailField("date_end_time").assertContains(time, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}