package com.sugarcrm.test.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vaibhav Singhal <vsinghal@sugarcrm.com>
 */
public class Tasks_28871_TimeColon extends SugarTest {
	FieldSet customData,customProfFS;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();
		customProfFS = new FieldSet(); 
		customProfFS.put("advanced_timeFormat", customData.get("timeformat"));

		// Set time Format to 24 hour time format	
		sugar.navbar.navToProfile();
		sugar.users.setPrefs(customProfFS);
		customProfFS.clear();
	}

	/**
	 * Verify date and time formats set in user profile are displayed properly on the date and time picker.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_28871_TimeColon_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Get Month, Day, Year from Calendar
		Date date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String currentTime = timeFormat.format(date);

		// Create Task
		sugar.navbar.selectMenuItem(sugar.tasks, "createTask");

		//Set and verify invalid time format is not accepted
		sugar.tasks.createDrawer.getEditField("date_start_time").set(customData.get("defTimeformat"));
		sugar.tasks.createDrawer.getEditField("subject").click();
		sugar.tasks.createDrawer.getEditField("date_start_time").assertEquals(customData.get("defTimeformat"), false);
		sugar.tasks.createDrawer.getEditField("date_due_time").set(customData.get("defTimeformat"));
		sugar.tasks.createDrawer.getEditField("subject").click();
		sugar.tasks.createDrawer.getEditField("date_due_time").assertEquals(customData.get("defTimeformat"), false);

		//Set and verify valid time format is accepted
		sugar.tasks.createDrawer.getEditField("date_start_time").set(currentTime);
		sugar.tasks.createDrawer.getEditField("subject").click();
		sugar.tasks.createDrawer.getEditField("date_start_time").assertEquals(currentTime, true);
		sugar.tasks.createDrawer.getEditField("date_due_time").set(currentTime);
		sugar.tasks.createDrawer.getEditField("subject").click();
		sugar.tasks.createDrawer.getEditField("date_due_time").assertEquals(currentTime, true);
		
		//Verify that 24:00 does not appears in the time list
		// TODO: VOOD-1463
		new VoodooControl("li", "css", ".ui-timepicker-list").assertContains(customData.get("eodHour"), false);
		
		sugar.tasks.createDrawer.cancel();
		
		// Create Meeting
		sugar.navbar.selectMenuItem(sugar.meetings, "createMeeting");

		//Set and verify invalid time format is not accepted
		sugar.meetings.createDrawer.getEditField("date_start_time").set(customData.get("defTimeformat"));
		sugar.meetings.createDrawer.getEditField("name").click();
		sugar.meetings.createDrawer.getEditField("date_start_time").assertEquals(customData.get("defTimeformat"), false);
		sugar.meetings.createDrawer.getEditField("date_end_time").set(customData.get("defTimeformat"));
		sugar.meetings.createDrawer.getEditField("name").click();
		sugar.meetings.createDrawer.getEditField("date_end_time").assertEquals(customData.get("defTimeformat"), false);

		//Set and verify valid time format is accepted
		sugar.meetings.createDrawer.getEditField("date_start_time").set(currentTime);
		sugar.meetings.createDrawer.getEditField("name").click();
		sugar.meetings.createDrawer.getEditField("date_start_time").assertEquals(currentTime, true);
		sugar.meetings.createDrawer.getEditField("date_end_time").set(currentTime);
		sugar.meetings.createDrawer.getEditField("name").click();
		sugar.meetings.createDrawer.getEditField("date_end_time").assertEquals(currentTime, true);
		
		//Verify that 24:00 does not appears in the time list
		new VoodooControl("li", "css", ".ui-timepicker-list").assertContains(customData.get("eodHour"), false);
		sugar.meetings.createDrawer.cancel();
		
		// Create Call
		sugar.navbar.selectMenuItem(sugar.calls, "createCall");

		//Set and verify invalid time format is not accepted
		sugar.calls.createDrawer.getEditField("date_start_time").set(customData.get("defTimeformat"));
		sugar.calls.createDrawer.getEditField("name").click();
		sugar.calls.createDrawer.getEditField("date_start_time").assertEquals(customData.get("defTimeformat"), false);
		sugar.calls.createDrawer.getEditField("date_end_time").set(customData.get("defTimeformat"));
		sugar.calls.createDrawer.getEditField("name").click();
		sugar.calls.createDrawer.getEditField("date_end_time").assertEquals(customData.get("defTimeformat"), false);

		//Set and verify valid time format is accepted
		sugar.calls.createDrawer.getEditField("date_start_time").set(currentTime);
		sugar.calls.createDrawer.getEditField("name").click();
		sugar.calls.createDrawer.getEditField("date_start_time").assertEquals(currentTime, true);
		sugar.calls.createDrawer.getEditField("date_end_time").set(currentTime);
		sugar.calls.createDrawer.getEditField("name").click();
		sugar.calls.createDrawer.getEditField("date_end_time").assertEquals(currentTime, true);
		
		//Verify that 24:00 does not appears in the time list
		new VoodooControl("li", "css", ".ui-timepicker-list").assertContains(customData.get("eodHour"), false);
		sugar.calls.createDrawer.cancel();
		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 