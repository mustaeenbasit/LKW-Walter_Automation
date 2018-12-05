package com.sugarcrm.test.roles;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Roles_21212 extends SugarTest {
	DataSource roleRecordData = new DataSource();
	DataSource rolePermissionData = new DataSource();

	public void setup() throws Exception {
		rolePermissionData = testData.get(testName);
		roleRecordData = testData.get("env_role_setup");
		sugar().accounts.api.create();
		sugar().login();

		// Create a role myRole
		AdminModule.createRole(roleRecordData.get(0));

		VoodooUtils.focusFrame("bwc-frame");

		// For Accounts module, Set all field permissions to "Read/Owner Write"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'" + sugar().accounts.moduleNamePlural + "')]").click();
		VoodooUtils.waitForReady();
		for (int i = 0; i < rolePermissionData.size(); i++) {
			// Need to click again on fieldCtrl if permissionsCtrl is not visible as some time script fails to click.
			VoodooControl fieldCtrl = new VoodooControl("div", "id", rolePermissionData.get(i).get("fields"));
			VoodooControl permissionsCtrl = new VoodooControl("select", "id", rolePermissionData.get(i).get("permissions"));
			fieldCtrl.click();
			if (!permissionsCtrl.queryVisible())
				fieldCtrl.click();
			permissionsCtrl.set(rolePermissionData.get(0).get("access"));
		}

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData.get(0));

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Field permissions set to Read Owner Write on Accounts - Full form edit of non-owned records cannot be edited
	 *
	 * @throws Exception
	 */
	@Test
	public void Roles_21212_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Click on Accounts module to display the list view.
		sugar().accounts.navToListView();

		// Click Account name on a record that is not owned by qauser.
		sugar().accounts.listView.clickRecord(1);

		// click the edit button
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();

		// verify that no field is in edit mode
		sugar().accounts.recordView.getDetailField("name").assertVisible(true);
		sugar().accounts.recordView.getDetailField("website").assertVisible(true);
		sugar().accounts.recordView.getDetailField("workPhone").assertVisible(true);
		sugar().accounts.recordView.getDetailField("fax").assertVisible(true);
		sugar().accounts.recordView.getDetailField("type").assertVisible(true);
		sugar().accounts.recordView.getDetailField("name").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}