package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21204 extends SugarTest {
	DataSource accountRecord;
	FieldSet roleData, testRecord;

	public void setup() throws Exception {
		testRecord = testData.get(testName).get(0);
		sugar().login();

		FieldSet firstRecord = new FieldSet();
		firstRecord.put("name", testName);
		firstRecord.put("relAssignedTo", sugar().users.getQAUser().get("userName"));
		firstRecord.put("relTeam", sugar().users.getQAUser().get("userName"));

		FieldSet secondRecord = new FieldSet();
		secondRecord.put("relTeam", testRecord.get("relAssignedTo"));

		accountRecord = new DataSource();
		accountRecord.add(firstRecord);
		accountRecord.add(secondRecord);
		// TODO: VOOD-444
		sugar().accounts.create(accountRecord);

		sugar().logout();
	}

	/**
	 *  Verify user can access to all the records in a module while the module access type is set to be Admin
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21204_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// login using qauser
		sugar().login(sugar().users.getQAUser());

		// navigate to accounts list view
		sugar().accounts.navToListView();

		int numberOfRecords = sugar().accounts.listView.countRows();

		// verify that only single record in the list that is assigned to qauser
		Assert.assertTrue("Record count not matched.", numberOfRecords == 1);

		// verify that record is assigned to "qauser" 
		sugar().accounts.listView.verifyField(1, "relAssignedTo", sugar().users.getQAUser().get("userName"));

		sugar().logout();

		// login through admin
		sugar().login();
		roleData = new FieldSet();
		roleData.put("roleName", testRecord.get("roleName"));
		roleData.put("roleDescription", testRecord.get("roleDescription"));
		roleData.put("userName", sugar().users.getQAUser().get("userName"));

		// Roles -> Create Role -> enter a role name -> Save
		AdminModule.createRole(roleData);

		// assign role to user
		AdminModule.assignUserToRole(roleData);

		// TODO VOOD-856
		// Click the Accounts module
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "id", "ACLEditView_Access_Accounts_admin").click();

		// Set the Access Type to be "Admin" for accounts module -> Save, and leave other settings as default.
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_admin select").set(testRecord.get("AccountType"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().logout();

		// re login as "qauser"
		sugar().login(sugar().users.getQAUser());

		// navigate to accounts list view
		sugar().accounts.navToListView();

		numberOfRecords = sugar().accounts.listView.countRows();

		// verify that all records are in the list assigned to "qauser" and "Admin"
		Assert.assertTrue("All records not available in the list", numberOfRecords == accountRecord.size());

		// verify that record is assigned to "Admin" 
		sugar().accounts.listView.verifyField(1, "relAssignedTo", testRecord.get("relAssignedTo"));
		// verify that record is assigned to "qauser" 
		sugar().accounts.listView.verifyField(2, "relAssignedTo", sugar().users.getQAUser().get("userName"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}