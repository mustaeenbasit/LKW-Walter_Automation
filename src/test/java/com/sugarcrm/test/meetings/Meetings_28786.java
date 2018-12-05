package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_28786 extends SugarTest {

	public void setup() throws Exception {
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that Tasks Are NOT displayed in FreeBusy Timeline
	 * @throws Exception
	 */
	@Test
	public void Meetings_28786_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet meetingTime = testData.get(testName).get(0);

		// Calculating Current Date & Time
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:00a");
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		date = c1.getTime();
		String todaysDate = sdf.format(date);
		String startTime = timeFormat.format(date);
		c1.add(Calendar.HOUR, 1);
		date = c1.getTime();
		String dueTime = timeFormat.format(date);
		String dueTimeTask = dueTime.replace("AM", "am").replace("PM","pm");

		// Creating Task having current Date & Time
		sugar().tasks.navToListView();
		sugar().tasks.listView.create();
		sugar().tasks.createDrawer.getEditField("subject").set(sugar().tasks.getDefaultData().get("subject"));
		sugar().tasks.createDrawer.getEditField("date_due_date").set(todaysDate);
		sugar().tasks.createDrawer.getEditField("date_due_time").set(dueTime);
		sugar().tasks.createDrawer.getEditField("date_start_date").set(todaysDate);
		sugar().tasks.createDrawer.getEditField("date_start_time").set(startTime);
		sugar().tasks.createDrawer.save();

		// Creating Meeting
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("date_start_date").set(todaysDate);
		sugar().meetings.createDrawer.getEditField("date_start_time").set(meetingTime.get("date_start_time"));
		sugar().meetings.createDrawer.getEditField("date_end_date").set(todaysDate);
		sugar().meetings.createDrawer.getEditField("date_end_time").set(meetingTime.get("date_end_time"));
		sugar().meetings.createDrawer.save();

		sugar().meetings.listView.clickRecord(1);

		// Verifying Each span of timeblock ( Verifying at meeting created timeslot the span contains class 'busy')
		// Used xPath to verify the timeslot on specified Timeline
		for(int i = 1; i < 10; i++) {
			for(int j = 1; j < 4; j++) {
				if(i == 5) 
					new VoodooControl("div", "xpath", "//*[@id='content']/div/div/div[1]/div/div[1]/div/div[2]/div[7]/div/span[1]/span/div/div[3]/div[2]/div[3]/div["+ i +"]/span["+ j +"]").assertAttribute("class", "busy", true);
				else
					new VoodooControl("div", "xpath", "//*[@id='content']/div/div/div[1]/div/div[1]/div/div[2]/div[7]/div/span[1]/span/div/div[3]/div[2]/div[3]/div["+ i +"]/span["+ j +"]").assertAttribute("class", "busy", false);		
			}
		}

		// TODO: VOOD-863
		// Verifying the Task created is shown in the Calendar
		sugar().calendar.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".week div[time='"+ dueTimeTask +"'] div.head div:nth-child(2)").assertEquals(sugar().tasks.getDefaultData().get("subject"), true);
		VoodooUtils.focusDefault();
	}

	public void cleanup() throws Exception {}
}