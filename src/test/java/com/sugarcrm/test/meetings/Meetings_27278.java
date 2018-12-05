package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Meetings_27278 extends SugarTest {
	String within24HoursDate, pastDate, dateAfter2Days,newToTime;

	public void setup() throws Exception {
		// Get current date in another format
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date dt = new Date();
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(dt);
		String currentDate = sdf.format(dt);
		int hour = cal.get(Calendar.HOUR); // Get current hours
		int min = cal.get(Calendar.MINUTE); // Get current minutes
		String amPm = cal.get(Calendar.AM_PM) > 0 ? "pm" : "am";
		if (hour < 0) hour += 12;

		// Sanitize the value
		String newFromTime, myHour, myMin = "";

		// Calculating new From time
		if (amPm.isEmpty() || (!amPm.contentEquals("am") && !amPm.contentEquals("pm")))
			amPm = "am";

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

		// set future date within next 24 hours
		within24HoursDate = currentDate;
		hour += 6; // Add 6 hours to current time so that end time remains within next 24 hours
		/*
		 * If calculated hour value becomes more than 12, we need to check and adjust 
		 * all date, time and amPm. During day time, within 6 am to 12 pm day, we 
		 * just need to adjust time and amPm but during evening time, within 6 pm 
		 * to 12 am, we need to adjust time, amPm and date also.
		 */
		if (hour >=12) { 
			hour -= 12;
			if (amPm == "pm") {
				Calendar cal1 = Calendar.getInstance();
				cal1.add(Calendar.DATE, 1);
				dt = cal1.getTime();
				within24HoursDate = sdf.format(dt);
			}
			amPm = amPm == "am" ? "pm" : "am";
		}

		// Calculate newToTime
		hour += 1;
		if (hour >= 1 && hour <= 9) 
			myHour = "0" + hour;
		else if (hour <= 0 || hour > 12) 
			myHour = "12";
		else 
			myHour = String.valueOf(hour);

		newToTime = myHour + ":" + myMin + amPm;

		FieldSet fs = new FieldSet();
		fs.put("date_start_date", currentDate);
		fs.put("date_start_time", newFromTime);
		fs.put("date_end_date", currentDate);
		fs.put("date_end_time", newToTime);
		sugar().meetings.api.create(fs);

		// Setting past date
		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.DATE, -2);
		dt = cal1.getTime();
		pastDate = sdf.format(dt);

		// Setting beyond 24 hours
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.DATE, 2);
		dt = cal2.getTime();
		dateAfter2Days = sdf.format(dt);

		sugar().login();
	}

	/**
	 * Verify that Start Date has no background color if Meeting Status is Held in list view
	 * @throws Exception
	 */
	@Test
	public void Meetings_27278_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		// when hour less than 9, appending "0" as prefix for time field
		if(newToTime.indexOf(":") == 1)
			newToTime = "0" + newToTime;

		sugar().meetings.navToListView();
		// Verify Start Date has blue in color
		sugar().meetings.listView.getDetailField(1, "date_start_date").assertCssAttribute("color", customData.get("rgb_blue_color_val"), true);

		VoodooControl startDateEditControl = sugar().meetings.recordView.getEditField("date_start_date");
		VoodooControl startTimeEditControl = sugar().meetings.recordView.getEditField("date_start_time");
		VoodooControl startDateListCtrl = sugar().meetings.listView.getDetailField(1, "date_start_date");

		/*
		 * Scenario1: Start date is within 24 hours comparing now
		 * Scenario2: Start Date is past comparing now. 
		 * Scenario3: Start Date is beyond 24 hours comparing now 
		 */
		for(int i=0; i<=2; i++){
			sugar().meetings.listView.clickRecord(1);
			sugar().meetings.recordView.edit();
			if(i==0){
				sugar().meetings.recordView.getEditField("status").set(customData.get("status"));
				startTimeEditControl.set(newToTime);
				startDateEditControl.set(within24HoursDate);
			}
			else if(i==1)
				startDateEditControl.set(pastDate);
			else if(i==2)
				startDateEditControl.set(dateAfter2Days);

			sugar().meetings.recordView.save();
			sugar().meetings.navToListView();

			// Verify Start Date has no color and date time displayed correctly
			startDateListCtrl.assertCssAttribute("color", customData.get("rgb_no_color_val"), true);
			if(i==0)
				startDateListCtrl.assertContains(within24HoursDate +" "+ newToTime, true);
			else if(i==1)
				startDateListCtrl.assertContains(pastDate +" "+ newToTime, true);
			else if(i==2)
				startDateListCtrl.assertContains(dateAfter2Days +" "+ newToTime, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}