package com.sugarcrm.test.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vaibhav Singhal <vsinghal@sugarcrm.com>
 */
public class Tasks_17116 extends SugarTest {
	FieldSet customData,customProfFS;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();
		customProfFS = new FieldSet(); 
		customProfFS.put("advanced_dateFormat", customData.get("dateFormat"));
		customProfFS.put("advanced_timeFormat", customData.get("timeformat"));

		// Set Date Format to mm-dd-yyyy	
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
	public void Tasks_17116_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Get Month, Day, Year from Calendar
		Date date = new Date();
		SimpleDateFormat dateFormat  = new SimpleDateFormat("MM-dd-yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String currentDate = dateFormat.format(date);
		String currentTime = timeFormat.format(date);

		// Create Task
		sugar.navbar.navToModule(sugar.tasks.moduleNamePlural);
		sugar.tasks.listView.create();

		//Set and verify the date
		sugar.tasks.createDrawer.getEditField("date_start_date").set(currentDate);

		sugar.tasks.createDrawer.getEditField("date_start_date").set(currentDate);
		sugar.tasks.createDrawer.getEditField("date_start_date").assertEquals(currentDate, true);

		//Set and verify the time
		sugar.tasks.createDrawer.getEditField("date_start_time").set(currentTime);
		sugar.tasks.createDrawer.getEditField("date_start_time").assertEquals(currentTime, true);
		sugar.tasks.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}