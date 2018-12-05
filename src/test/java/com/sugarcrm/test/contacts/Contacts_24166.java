package com.sugarcrm.test.contacts;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Contacts_24166 extends SugarTest {
	StandardSubpanel subTasks;
	ContactRecord myContact;
	FieldSet defaultTaskData, tasksSubpanelData = new FieldSet();

	public void setup() throws Exception {
		subTasks = sugar().contacts.recordView.subpanels.get("Tasks");
		defaultTaskData = sugar().tasks.getDefaultData();
		tasksSubpanelData.put("subject", defaultTaskData.get("subject"));
		sugar().login();
		myContact = (ContactRecord) sugar().contacts.api.create();
	}

	/**
	 * Test Case 24166: Create task_Verify that related task can be created from contact detail view.
	 */
	@Test
	public void Contacts_24166_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myContact.navToRecord();
		subTasks.addRecord();

		// Put default data and Cancel
		sugar().tasks.createDrawer.showMore();
		sugar().tasks.createDrawer.setFields(defaultTaskData);
		sugar().tasks.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Verify the task related to the case has been created
		subTasks.verify(1, tasksSubpanelData, true);
		// Verify a task
		// TODO: VOOD-1007
		// new TaskRecord(defaultTaskData).verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
