package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21295 extends SugarTest {
	FieldSet customFS;

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);

		// Create two Account records with different name
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create(fs);
		sugar().accounts.api.create();
		sugar().login();

		// Create a role
		AdminModule.createRole(customFS);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set its View role to be Owner
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_view div select").set(customFS.get("view"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) into this Role
		AdminModule.assignUserToRole(customFS);

		// Assign only two Accounts record to QAUser
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("relAssignedTo").set(sugar().users.qaUser.get("userName"));
		sugar().accounts.recordView.save();

		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify user can not access and see the not-owned records while the View role is set to Owner
	 * @throws Exception
	 */
	@Test
	public void Roles_21295_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts record listView
		sugar().accounts.navToListView();
		
		// Verify that first record in recordView
		sugar().accounts.listView.verifyField(1, "name", sugar().accounts.getDefaultData().get("name"));
		
		// Verify that second record, If a record not assigned to this user, then the record is not click-able and user can not go to the detail view page.
		VoodooControl clickRecordCtrl = sugar().accounts.listView.getDetailField(2, "name");
		clickRecordCtrl.assertExists(false);
		
		// Go to detail view of first record on listView
		sugar().accounts.listView.clickRecord(1);
		
		// Verify that the owned record is shown as link in the list view and the user can click the owned record to go to detail view page.
		sugar().accounts.recordView.getDetailField("name").assertEquals(sugar().accounts.getDefaultData().get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}