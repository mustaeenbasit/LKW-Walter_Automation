package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

public class Tasks_21026 extends SugarTest {
	ContactRecord myContact;
	TaskRecord myTask;

	public void setup() throws Exception {
		myTask = (TaskRecord) sugar().tasks.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();

		// Login as Admin user
		sugar().login();

		// Create one Task record related to "contact" module
		sugar().tasks.navToListView();
		sugar().tasks.listView.clickRecord(1);
		sugar().tasks.recordView.edit();
		sugar().tasks.recordView.showMore();
		sugar().tasks.recordView.getEditField("contactName").set(myContact.getRecordIdentifier());
		sugar().tasks.recordView.save();
	}

	/**
	 * View Tasks
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21026_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Tasks module and click one task name link which is related to "contact" in task list view
		sugar().tasks.navToListView();
		sugar().tasks.listView.clickRecord(1);

		// Verify that the Tasks detail view is displayed
		// TODO: VOOD-1887 - Uncomment the line written below after the VOOD is resolved
		// sugar().tasks.recordView.assertVisible(true);
		sugar().tasks.recordView.getDetailField("subject").assertEquals(myTask.getRecordIdentifier(), true);

		// Also asserting the Contacts field with correct data
		VoodooControl contactsFieldCtrl = sugar().tasks.recordView.getDetailField("contactName");
		contactsFieldCtrl.assertEquals(myContact.get("fullName"), true);

		// Click contact name link in "Contact" field
		contactsFieldCtrl.click();
		VoodooUtils.waitForReady();

		// The contact detail view is displayed
		// TODO: VOOD-1887 - Uncomment the line written below after the VOOD is resolved
		// sugar().contacts.recordView.assertVisible(true);
		sugar().contacts.recordView.getDetailField("fullName").assertEquals(myContact.get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}