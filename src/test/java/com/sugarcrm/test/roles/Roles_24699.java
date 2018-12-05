package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_24699 extends SugarTest {
	FieldSet roleRecordData; 

	public void setup() throws Exception {
		roleRecordData = testData.get(testName).get(0);
		sugar().contacts.api.create();
		sugar().quotes.api.create();
		sugar().login();
	}

	/**
	 * Role management: Verify that can't create a task from sub panel when the user cannot access "Tasks" module.
	 * @throws Exception
	 */
	@Test
	public void Roles_24699_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// 'Disable' access privilege of "Tasks" module
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Tasks_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Tasks_access div select").set(roleRecordData.get("access"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);

		// Logout from the Admin user
		sugar().logout();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Go to a Contact record view
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Verify that No "Tasks" subpanel display under Contact record
		sugar().contacts.recordView.subpanels.get(sugar().tasks.moduleNamePlural).assertExists(false);

		// Go to a Quotes detail view
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);

		// Verify that No "create task" option displays under Activities subpanel
		// TODO: VOOD-826
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "Activities_createtask_button_create_").assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}