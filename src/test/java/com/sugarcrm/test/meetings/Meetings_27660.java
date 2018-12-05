package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27660 extends SugarTest {	
	FieldSet meetingData;
	String todaysDate, dateAfter3Days, dateAfter2Days, dateForDailyMeeting;
	Date date;
	SimpleDateFormat sdf;
	Calendar cal;

	public void setup() throws Exception {
		meetingData = testData.get(testName).get(0);

		// Date of two days after today.
		date = new Date();
		sdf = new SimpleDateFormat("MM/dd/yyyy");
		todaysDate = sdf.format(date);
		cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 3);
		date = cal.getTime();
		dateAfter3Days = sdf.format(date);
		cal.add(Calendar.DATE, -3);
		cal.add(Calendar.DATE, 2);
		date = cal.getTime();
		dateAfter2Days = sdf.format(date);
		cal.add(Calendar.DATE, -2);
		sugar().login();
	}

	/**
	 * Verify that "or" between Repeat Until & Repeat Occurrences fields in Meetings
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27660_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Meetings.Create a new Meeting.
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		// TODO: VOOD-1169
		// Select "Daily" in "Repeat Type".Select "Repeat Until" set 3 days after today.
		VoodooControl repeatType = new VoodooSelect("span", "css", ".fld_repeat_type.edit div");
		VoodooControl repeatUntil = new VoodooControl("input", "css", ".fld_repeat_until.edit input");
		repeatType.set(meetingData.get("repeat_type_daily"));
		repeatUntil.set(dateAfter3Days);

		// Observe that "or" appears in the between "Repeat Until" and "Repeat Occurrences".
		new VoodooControl("span", "css", "div.layout_Meetings .fld_recurrence div[data-type='label']").assertContains(meetingData.get("or"), true);

		// Observe that "Repeat Occurrence" becomes blank.
		VoodooControl repeatCountCtrl = new VoodooControl("input", "css", ".fld_repeat_count.edit input");
		repeatCountCtrl.click(); // Need to click, to change the focus so that changes are reflected to the "Repeat Occurrence" field.
		Assert.assertTrue("Repeat Occurrence field is not becomes blank",repeatCountCtrl.getText().isEmpty());

		// Fill in all required fields and click on Save.
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("date_start_date").set(todaysDate);
		sugar().meetings.createDrawer.save();

		// Verify that in Meetings listview, 4 new meetings are created.  The first meeting has Start Date of today, 2nd meeting has Start Date as tomorrow and so on. 
		for(int i=0;i<4;i++){
			cal.add(Calendar.DATE, i);
			date = cal.getTime();
			dateForDailyMeeting = sdf.format(date);

			// Using XPath to select meetings by unique date.
			new VoodooControl("tr", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+dateForDailyMeeting+"')]").assertContains(testName, true);
			cal.add(Calendar.DATE, -i);
		}

		// Try to create another Meeting.
		sugar().meetings.listView.create();

		// Select "Weekly" in "Repeat Type".Select "Repeat Until" set 2 days after today.		
		repeatType.set(meetingData.get("repeat_type_weekly"));
		repeatUntil.set(dateAfter2Days);

		// Select 5 in "Repeat Occurrence". 
		repeatCountCtrl.set(meetingData.get("repeat_occurrence"));

		// Observe that the value in "Repeat Until" is disappeared.  It has "mm/dd/yyyy" placeholder.
		repeatUntil.assertAttribute("placeholder", meetingData.get("repeat_until_placeholder"), true);

		// Fill in all required fields and click on Save.
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("date_start_date").set(todaysDate);
		sugar().meetings.createDrawer.save();

		//  Observe that 5 new meetings are created. The 1st meeting has Start Date in today, the 2nd meeting has the Start Date as next week at the same time of the 1st meeting Start Time, and so on.
		for(int i=0;i<5;i++){
			cal.add(Calendar.DATE, (7*i));
			date = cal.getTime();
			dateForDailyMeeting = sdf.format(date);

			// Using XPath to select meetings by unique date.
			new VoodooControl("tr", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+dateForDailyMeeting+"')]").assertContains(testName, true);
			cal.add(Calendar.DATE, -(7*i));
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}