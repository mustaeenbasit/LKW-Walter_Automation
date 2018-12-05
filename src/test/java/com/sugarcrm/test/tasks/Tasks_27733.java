package com.sugarcrm.test.tasks;

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
public class Tasks_27733 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
		sugar.tasks.navToListView();
		sugar.tasks.listView.create();
		sugar.tasks.createDrawer.getEditField("subject").set(sugar.tasks.getDefaultData().get("subject"));
		sugar.tasks.createDrawer.save();
	}

	/**
	 * Verify that Due Date should be no color if Status is Held in Task list view
	 * @throws Exception
	 */
	@Test
	public void Tasks_27733_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		// Get current date in another format
		Date dt = new Date();
		Calendar cal = Calendar.getInstance();  
		cal.setTime(dt);
		cal.add(Calendar.DATE, 1); // 1 date add
		cal.add(Calendar.HOUR, -5); // within 24 hours date (becoz Due date compared with Date created field if no start date)
		dt = cal.getTime();
		String within24HourDate = sdf.format(dt);

		int hour = cal.get(Calendar.HOUR); // Get hours
		int min = cal.get(Calendar.MINUTE); // Get minutes
		int booleanAmPm = cal.get(Calendar.AM_PM); // Get 0/1 : am/pm
		String amPm = "am";
		String newTime = "";

		if(booleanAmPm > 0) amPm = "pm";
		if(hour <= 0) hour = 12;

		// Set hours and minutes
		if(min >= 0 && min < 15) {			
			newTime = hour+":15"+amPm;
		} else if(min >= 15 && min < 30)
			newTime = hour+":30"+amPm;
		else if(min >= 30 && min < 45)
			newTime = hour+":45"+amPm;
		else if(min >= 45 && min < 59) {
			if(hour != 12) hour = (hour+1);
			newTime = hour+":00pm";
		}

		// Setting past date
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.DATE, -2); // any past date
		dt = cal2.getTime();
		String pastDate = sdf.format(dt);

		VoodooControl statusEditControl = sugar.tasks.recordView.getEditField("status");
		VoodooControl dueTimeEditControl = sugar.tasks.recordView.getEditField("date_due_time");
		VoodooControl dueDateEditControl = sugar.tasks.recordView.getEditField("date_due_date"); 
		VoodooControl dueDateListCtrl = sugar.tasks.listView.getDetailField(1, "date_due_date");

		// when hour less than 9, appending "0" as prefix for time field
		if(newTime.indexOf(":") == 1)
			newTime = "0" + newTime;

		for (int i = 0; i < 2; i++) {
			sugar.tasks.listView.clickRecord(1);
			sugar.tasks.recordView.edit();
			statusEditControl.set(customData.get("status"));
			dueTimeEditControl.set(newTime);
			if(i==0)
				dueDateEditControl.set(within24HourDate);
			else
				dueDateEditControl.set(pastDate);

			sugar.tasks.recordView.save();
			sugar.tasks.navToListView();

			// Verify Due Date has no in color and also Date and Time appears.
			dueDateListCtrl.assertCssAttribute("color", customData.get("rgb_color_val"), true);
			if(i==0)
				dueDateListCtrl.assertContains(within24HourDate + " " +newTime, true);
			else
				dueDateListCtrl.assertContains(pastDate + " " +newTime, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}