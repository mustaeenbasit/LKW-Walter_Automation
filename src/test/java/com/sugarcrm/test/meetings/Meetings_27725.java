package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27725 extends SugarTest {
	MeetingRecord myMeeting;
	String newDate, newFromTime, newToTime;
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	public void setup() throws Exception {
		// Get current date in another format
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt);
		
		// Get future date
		c.add(Calendar.DATE, 1); // set future date within next 24 hours
		dt = c.getTime();
		newDate = sdf.format(dt);
		
		// Get future time
		int hour = c.get(Calendar.HOUR); // Get current hours
		int min = c.get(Calendar.MINUTE); // Get current minutes
		String amPm = c.get(Calendar.AM_PM) > 0 ? "pm" : "am";
		
		hour -= 6; // set future date within next 24 hours
		if (hour < 0) hour += 12;
		
		// Sanitize the value
		newFromTime = ""; 	
		newToTime = ""; 	
		String myHour = "";
		String myMin = "";
	
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

		// Calculate newToTime
		hour += 1;
		if (hour >= 1 && hour <= 9) 
			myHour = "0" + hour;
		else if (hour <= 0 || hour > 12) 
			myHour = "12";
		else 
			myHour = String.valueOf(hour);
	
		newToTime = myHour + ":" + myMin + amPm;

		sugar().login();
		
		FieldSet fs = new FieldSet();
		fs.put("date_start_date", newDate);
		fs.put("date_start_time", newFromTime);
		fs.put("date_end_date", newDate);
		fs.put("date_end_time", newToTime);

		myMeeting = (MeetingRecord) sugar().meetings.create(fs);
	}

	/**
	 * Verify Start Date has no background color if Start Date is passed 24 hours in Meetings listview
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27725_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that Start Date should not have blue or red color
		sugar().meetings.navToListView();
		VoodooControl startDate = new VoodooControl("span", "css", "[data-voodoo-name='recordlist'] table tbody tr:nth-child(1) td:nth-child(4) span");
		startDate.assertAttribute("class", "list fld_date_start", true);
		
		// Set revised start date/time and end date/time beyond 24 hours
		myMeeting.navToRecord();
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("date_start_date").set(sugar().meetings.getDefaultData().get("date_start_date"));
		sugar().meetings.recordView.getEditField("date_end_date").set(sugar().meetings.getDefaultData().get("date_end_date"));
		sugar().meetings.recordView.save();

		// Verify that Start Date becomes white
		sugar().meetings.navToListView();
		startDate.assertAttribute("class", "label-info", false);
		
		// Assert Start Date color is white
		startDate.assertCssAttribute("color", "rgba(85, 85, 85, 1)", true);
		
		// Edit existing record and set status = Canceled
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("status").set("Canceled");
		sugar().meetings.recordView.save();		

		// Verify that Start Date is white
		sugar().meetings.navToListView();
		startDate.assertAttribute("class", "label-info", false);
		startDate.assertAttribute("class", "label-important", false);
		
		// Assert Start Date color is white
		startDate.assertCssAttribute("color", "rgba(85, 85, 85, 1)", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
