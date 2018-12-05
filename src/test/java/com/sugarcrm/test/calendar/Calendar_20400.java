package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Calendar_20400 extends SugarTest {
	UserRecord qauser;

	public void setup() throws Exception {
		// Create a task record 
		FieldSet taskDate = new FieldSet();
		String todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		taskDate.put("date_start_date", todaysDate);
		taskDate.put("date_due_date", todaysDate);
		sugar().tasks.api.create(taskDate);

		// Login to sugar as an admin user
		sugar().login();

		// Assign task to qauser
		sugar().tasks.navToListView();
		sugar().tasks.listView.editRecord(1);
		qauser = new UserRecord(sugar().users.getQAUser());
		sugar().tasks.listView.getEditField(1, "relAssignedTo").set(qauser.get("userName"));
		sugar().tasks.listView.saveRecord(1);
	}

	/**
	 * Drag and drop of Tasks within calendar - week view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20400_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Logout and Log in as test user
		sugar().logout();
		qauser.login();

		// Click "Calendar" navigation tab.
		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// On Week view, Left click on the Task and drag it to a different date. 
		// TODO: VOOD-863
		VoodooControl dropHere = new VoodooControl("div", "css", "div.week div:nth-child(7) div:nth-child(22)");
		String newDateTime = dropHere.getAttribute("datetime");
		new VoodooControl("div", "css", ".head").dragNDrop(dropHere);
		VoodooUtils.waitForReady();

		// Verify that you can drag and drop the Task to a different day of the week.
		dropHere.assertEquals(sugar().tasks.getDefaultData().get("subject"), true);

		// Display the detailed view of the task
		dropHere.click();

		// Verify that "Due Date" and "Time" displays the new date and time.
		sugar().tasks.recordView.getDetailField("date_due_date").assertEquals(newDateTime, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}