package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_16988 extends SugarTest {
	AccountRecord ownerAccount, nonOwnerAccount;
	DataSource accountFields, roleFields;
		
	public void setup() throws Exception {
		sugar().login();
		
		accountFields = testData.get("Roles_16988");
		roleFields = testData.get("Roles_16988_roledata");
		
		ownerAccount = (AccountRecord)sugar().accounts.api.create(accountFields.get(0));
		nonOwnerAccount = (AccountRecord)sugar().accounts.api.create();
		FieldSet fs = new FieldSet();
		fs.put("relAssignedTo",accountFields.get(0).get("relAssignedTo") );
		ownerAccount.edit(fs);
	}

	/**
	 * 16988 Edit inline record - Field permission Read/Owner Write 
	 * @throws Exception
	 */
	@Test
	public void Roles_16988_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.createRole(roleFields.get(0));
		sugar().admin.assignUserToRole(roleFields.get(0));

		// TODO VOOD-858
		// assignUserToRole does not wait until user is really added
		VoodooUtils.pause(9000);
		
		// TODO VOOD-856
		// Click the Accounts module
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".edit.view tr:nth-of-type(2) a").click();
				
		// Set the field access for "Assigned to" to Read/Owner Write
		new VoodooControl("div", "id", "assigned_user_namelink").click();
		new VoodooControl("select", "id", "flc_guidassigned_user_name").set("Read/Owner Write");
		
		// Save the Role
		// Pause is required here
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		sugar().accounts.listView.editRecord(2);
		
		// Verify that relAssignedTo field with "Administrator" text is not editable, i.e. has ".fld_assigned_user_name.detail" class
		// Field with text "qauser" should be editable, i.e. have ".fld_assigned_user_name.edit" class
		new VoodooControl("span", "css", ".fld_assigned_user_name.detail").assertEquals("Administrator", true);
		new VoodooControl("span", "css", ".fld_assigned_user_name.edit")
			.assertEquals(roleFields.get(0).get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}