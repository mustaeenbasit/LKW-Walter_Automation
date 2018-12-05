package com.sugarcrm.test.tasks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Tasks_27731 extends SugarTest {
	String futureDate, newToTime;

	public void setup() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		// Get current date in another format
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
		futureDate = currentDate;
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
				futureDate = sdf.format(dt);
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
		fs.put("date_due_date", currentDate);
		fs.put("date_due_time", newFromTime);
		sugar.tasks.api.create(fs);

		sugar.login();
	}

	/**
	 * Verify that Due Date should be blue if Due Date is within next 24 hours and is not marked as Held status Task listview
	 * @throws Exception
	 */
	@Test
	public void Tasks_27731_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource statusDS = testData.get(testName);

		// Tasks Listview
		sugar.tasks.navToListView();
		VoodooControl statusEditControl = sugar.tasks.recordView.getEditField("status");
		VoodooControl dueTimeEditControl = sugar.tasks.recordView.getEditField("date_due_time");
		VoodooControl dueDateEditControl = sugar.tasks.recordView.getEditField("date_due_date");
		VoodooControl dueDateListCtrl = sugar.tasks.listView.getDetailField(1, "date_due_date");

		for (int i = 0; i < statusDS.size(); i++) {
			sugar.tasks.listView.clickRecord(1);
			sugar.tasks.recordView.edit();
			statusEditControl.set(statusDS.get(i).get("status"));

			// date and time fix for all scenarios
			dueTimeEditControl.set(newToTime);
			dueDateEditControl.set(futureDate);
			sugar.tasks.recordView.save();
			sugar.tasks.navToListView();

			// Verify Due Date is Blue and also Date and Time appears.
			dueDateListCtrl.assertCssAttribute("color", statusDS.get(i).get("rgb_color_val"), true);
			dueDateListCtrl.assertContains(futureDate, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}