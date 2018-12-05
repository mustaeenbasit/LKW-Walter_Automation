package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27723 extends SugarTest {
	String newDate, newFromTime, newToTime;

	public void setup() throws Exception {
		sugar().meetings.api.create();
		// Get current date in another format
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt);
		dt = c.getTime();
		newDate = sdf.format(dt);

		// Get future time
		int hour = c.get(Calendar.HOUR); // Get current hours
		int min = c.get(Calendar.MINUTE); // Get current minutes
		int booleanAmPm = c.get(Calendar.AM_PM); // Get current 0/1 : am/pm

		// Set am/pm
		String amPm = "am";
		if(booleanAmPm > 0) amPm = "pm";

		hour -= 6; // set future date within next 24 hours
		if (hour < 0) hour += 12;

		// Sanitize the value
		newFromTime = ""; 	
		newToTime = ""; 	
		String myHour = "";
		String myMin = "";

		if(min == 0)
			myMin = "00";
		else if(min > 0 && min <= 15)
			myMin = "15";
		else if(min > 15 && min <= 30)
			myMin = "30";
		else if(min > 30 && min <= 45)
			myMin = "45";
		else {
			hour++;
			myMin = "00";
		}

		if (hour >= 1 && hour <= 9) 
			myHour = "0" + hour;
		else if (hour <= 0 || hour > 12) 
			myHour = "12";
		else 
			myHour = String.valueOf(hour);

		newFromTime = myHour + ":" + myMin + amPm;

		// Calculate newToTime
		hour += 1;
		if (hour >= 1 && hour <= 9) 
			myHour = "0" + hour;
		else if (hour <= 0 || hour > 12) 
			myHour = "12";
		else 
			myHour = String.valueOf(hour);

		newToTime = myHour + ":" + myMin + amPm;

		// Login as Admin
		sugar().login();

	}

	/**
	 * Verify Start Date in blue background if Start Date is within next 24 hours and meeting status is not Held
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27723_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that Start Date should not have blue or red color
		sugar().meetings.navToListView();
		VoodooControl listViewStartDate = sugar().meetings.listView.getDetailField(1, "date_start_date");
		FieldSet customFS = testData.get(testName).get(0);

		// Assert Start Date color is white
		listViewStartDate.assertCssAttribute("color", customFS.get("whiteColor"));

		// Set revised start date/time and end date/time
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("date_start_date").set(newDate);
		sugar().meetings.recordView.getEditField("date_start_time").set(newFromTime);
		sugar().meetings.recordView.getEditField("date_end_date").set(newDate);
		sugar().meetings.recordView.getEditField("date_end_time").set(newToTime);
		sugar().meetings.recordView.save();

		// Verify that Start Date is blue
		sugar().meetings.navToListView();

		// Assert Start Date color is blue
		listViewStartDate.assertCssAttribute("color", customFS.get("blueColor"));

		// Edit existing record and set status = Canceled
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("status").set("Canceled");
		sugar().meetings.recordView.save();

		// Verify that Start Date color should be blue "rgba(255, 255, 255, 1)" and Status color should be red "class='label-important'" 
		sugar().meetings.navToListView();
		listViewStartDate.assertCssAttribute("color", customFS.get("blueColor"));
		sugar().meetings.listView.getDetailField(1, "status").assertAttribute("class", "label-important", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}