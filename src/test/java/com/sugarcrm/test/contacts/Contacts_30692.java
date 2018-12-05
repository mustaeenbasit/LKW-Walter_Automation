package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Basant Chandak <bchandak@sugar()crm.com>
 */
public class Contacts_30692 extends SugarTest {
	TaskRecord taskData;

	public void setup() throws Exception {
		sugar().contacts.api.create();
		taskData = (TaskRecord)sugar().tasks.api.create();
		sugar().login();
	}

	/**
	 * Verify Inline editing of Assigned user from subpanel is working properly
	 * @throws Exception
	 */
	@Test
	public void Contacts_30692_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet userData = testData.get(testName).get(0);
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Link task record in Contacts Subpanel
		StandardSubpanel tasksSubpanel = sugar().contacts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		tasksSubpanel.linkExistingRecord(taskData);

		// Verify the Assigned user is currently Administrator
		VoodooControl relAssignedTo = tasksSubpanel.getDetailField(1, "relAssignedTo");
		relAssignedTo.assertEquals(userData.get("userName"), true);

		// Change the Assigned user to qauser
		String qauser = sugar().users.getQAUser().get("userName");
		tasksSubpanel.editRecord(1);
		tasksSubpanel.getEditField(1, "relAssignedTo").set(qauser);
		tasksSubpanel.saveAction(1);

		// Verify that the AssignedTo field is now changed to qauser
		relAssignedTo.assertEquals(qauser, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}