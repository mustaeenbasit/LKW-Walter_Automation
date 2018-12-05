package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21231 extends SugarTest {
	public void setup() throws Exception {
		FieldSet roleRecord = testData.get(testName).get(0);

		FieldSet accountRecord = new FieldSet();
		accountRecord.put("name", testName);
		DataSource accountDataSet = new DataSource();
		accountDataSet.add(sugar().accounts.getDefaultData());
		accountDataSet.add(accountRecord);
		// Create two accounts records 
		sugar().accounts.api.create(accountDataSet);

		sugar().login();

		// Create a new Role 
		AdminModule.createRole(roleRecord);

		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-856
		// Admin -> Role management -> Select account module and set its View role to be "Owner"
		new VoodooControl("td", "id", "ACLEditView_Access_Accounts_view").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_view select").set(roleRecord.get("accessType"));
		VoodooUtils.waitForReady();
		// Click Save
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign role to "qauser"
		AdminModule.assignUserToRole(roleRecord);

		// Navigate to accounts listview
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		FieldSet fs = new FieldSet();
		fs.put("Assigned to", sugar().users.getQAUser().get("userName"));
		// Update "Assigned to" to "qauser" for one record 
		sugar().accounts.massUpdate.performMassUpdate(fs);

		sugar().logout();
	}

	/**
	 * Verify user can access and see the own records while the View role is set to Owner
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21231_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as "qauser"
		sugar().login(sugar().users.getQAUser());
		sugar().accounts.navToListView();
		
		// Verify If a record not assigned to Login user (qauser), then the record is not visible
		sugar().accounts.listView.getDetailField(2, "relAssignedTo").assertEquals(sugar().users.getQAUser().get("userName"), false);
		sugar().accounts.listView.getDetailField(2, "name").assertExists(false);

		// Verify If a record assigned to Login user (qauser), then the record is visible
		sugar().accounts.listView.getDetailField(1, "relAssignedTo").assertEquals(sugar().users.getQAUser().get("userName"), true);

		// Verify record is clickable if assigned to "qauser"
		sugar().accounts.listView.getDetailField(1, "name").assertExists(true);
		sugar().accounts.listView.clickRecord(1);

		// Verify record name on detail view page
		sugar().accounts.recordView.getDetailField("relAssignedTo").assertEquals(sugar().users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}