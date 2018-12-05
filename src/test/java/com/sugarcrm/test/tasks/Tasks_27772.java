package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Tasks_27772 extends SugarTest {
	FieldSet myData = new FieldSet();
	UserRecord myUser;

	public void setup() throws Exception {
		FieldSet userData = testData.get(testName+"_user").get(0);
		myData = testData.get(testName).get(0);

		// Login as admin user
		sugar().login();	

		// Create a new user 
		myUser = (UserRecord) sugar().users.create(userData);
		sugar().logout();

		// Logging in with new user
		myUser.login(); 
		FieldSet timeZoneFS = new FieldSet();
		timeZoneFS.put("advanced_timeZone", myData.get("estProfile"));

		// Changing the time zone of the user to (-5:00)
		sugar().users.setPrefs(timeZoneFS);
		sugar().logout();

		// Re-logging in as for the above step to take effect
		sugar().login(myUser);

		// verifying an alert message is displayed upon changing the user's time zone
		sugar().alerts.getWarning().assertVisible(true);
		sugar().alerts.getWarning().assertContains(myData.get("alertMessage"), true);
		sugar().alerts.getWarning().closeAlert();

		// Creating a task record via UI as per the test case
		FieldSet task = new FieldSet();
		task = sugar().tasks.getDefaultData();
		task.put("date_start_time", myData.get("date_start_time"));
		task.put("date_due_time", myData.get("date_due_time"));
		task.put("date_start_date", myData.get("date_start_date"));
		task.put("date_due_date", myData.get("date_due_date"));
		sugar().tasks.create(task);
	}

	/**
	 * Verify sidecar modules respect the user's browser time zone
	 * @throws Exception
	 */
	@Test
	public void Tasks_27772_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().tasks.listView.clickRecord(1);

		// verifying that the task created display correct Start date and time
		String startDateTime = myData.get("date_start_date") + " " + myData.get("date_start_time");
		sugar().tasks.recordView.getDetailField("date_start_date").assertContains(startDateTime, true);

		// verifying that the task created display correct Due date and time
		String dueDateTime = myData.get("date_due_date") + " " + myData.get("date_due_time");
		sugar().tasks.recordView.getDetailField("date_due_date").assertContains(dueDateTime, true); 

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}