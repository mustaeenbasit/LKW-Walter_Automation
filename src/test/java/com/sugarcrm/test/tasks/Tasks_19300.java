package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_19300 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();	
	}
	/**
	 * Verify that invalid date should not be accepted by the date picker when create a task record 
	 * @throws Exception
	 */	
	@Test
	public void Tasks_19300_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		FieldSet customData = testData.get(testName).get(0);

		// Click Create on Task ListView
		sugar().tasks.navToListView();
		sugar().tasks.listView.create();

		// Enter an invalid date into Start Date and End Date & hit Enter
		// TODO: VOOD-1437
		sugar().tasks.createDrawer.getEditField("subject").set(testName);
		sugar().tasks.createDrawer.getEditField("date_start_date").set(customData.get("startDate") + '\uE007');
		sugar().tasks.createDrawer.getEditField("date_due_date").set(customData.get("dueDate") + '\uE007');

		// Verify Date picker field is cleared 
		sugar().tasks.createDrawer.getEditField("date_start_date").assertContains("", true);
		sugar().tasks.createDrawer.getEditField("date_due_date").assertContains("", true);

		// Click Save button
		sugar().tasks.createDrawer.save();
		sugar().tasks.listView.clickRecord(1);

		// Verify task is created without start and end date if input date is wrong 
		sugar().tasks.recordView.getDetailField("date_start_date").assertContains("", true);
		sugar().tasks.recordView.getDetailField("date_due_date").assertContains("", true);

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}