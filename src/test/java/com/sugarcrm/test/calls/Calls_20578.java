package com.sugarcrm.test.calls;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_20578 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Rounding of calls start time according to current system time
	 * @throws Exception
	 */
	@Test
	public void Calls_20578_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Get current date and time
		Date date = new Date();
		Calendar calObj = Calendar.getInstance();
		calObj.setTime(date);
		int hour = calObj.get(Calendar.HOUR);
		int min = calObj.get(Calendar.MINUTE); // Get current minutes
		int booleanAmPm = calObj.get(Calendar.AM_PM); // Get current 0/1 : am/pm
		String amPm = (booleanAmPm == 0) ? "am" : "pm";

		// Set hours and minutes as per requirement
		String time = "";
		if(hour <= 0) hour = 12;
		if(min >= 0 && min <= 30) 			
			time = hour+":30"+amPm;
		else if(min > 30 && min < 59) {
			hour = (hour == 12 && min > 30? 1 : hour+1);
			time = hour+":00"+amPm;
		}

		// Go to Calls > createDrawer
		sugar().calls.navToListView();
		sugar().calls.listView.create();

		// Verify that Default start date should be current date and time should be set current round(time)
		sugar().calls.createDrawer.getEditField("date_start_time").assertContains(time, true);
		sugar().calls.createDrawer.getEditField("date_start_date").assertEquals(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}