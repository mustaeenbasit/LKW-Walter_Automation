package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_24738 extends SugarTest {
	FieldSet roleRecordData;
	VoodooControl roleNameDetailViewCtrl, accountsFieldCtrl, contactsFieldCtrl, notesFieldCtrl, quotesFieldCtrl, reportFieldCtrl;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName).get(0);
		sugar().login();

		// There is one existing customized role in Roles module
		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Update privileges for some modules and verify the same privileges for the duplicate record while verifying that the detail view of the new role is displayed same as the original one
		// TODO: VOOD-856, VOOD-580
		roleNameDetailViewCtrl = new VoodooControl("td", "css", ".detail.view tr:nth-child(1) > td:nth-child(2)");
		accountsFieldCtrl = new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_access div:nth-of-type(2)");
		contactsFieldCtrl = new VoodooControl("td", "css", "#ACLEditView_Access_Contacts_delete div:nth-of-type(2)");
		notesFieldCtrl = new VoodooControl("td", "css", "#ACLEditView_Access_Notes_edit div:nth-of-type(2)");
		quotesFieldCtrl = new VoodooControl("td", "css", "#ACLEditView_Access_Quotes_import div:nth-of-type(2)");
		reportFieldCtrl = new VoodooControl("td", "css", "#ACLEditView_Access_Reports_view div:nth-of-type(2)");
		VoodooControl userNameSubpanelCtrl = new VoodooControl("span", "css", "#whole_subpanel_users .oddListRowS1 span");

		// 'Disable' Access privilege of "Accounts" module
		accountsFieldCtrl.click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_access div select").set(roleRecordData.get("accessDisabled"));

		// Set to 'Owner' Delete privilege of "Contacts" module
		contactsFieldCtrl.click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_delete div select").set(roleRecordData.get("owner"));

		// Set to 'All' Edit privilege of "Notes" module
		notesFieldCtrl.click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Notes_edit div select").set(roleRecordData.get("all"));

		// Set to 'None' Import privilege of "Quotes" module
		quotesFieldCtrl.click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Quotes_import div select").set(roleRecordData.get("none"));

		// Set to 'Owner' View privilege of "Reports" module
		reportFieldCtrl.click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Reports_view div select").set(roleRecordData.get("owner"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);
		VoodooUtils.waitForReady(); // Need to wait until Role record getting saved
		VoodooUtils.focusFrame("bwc-frame");

		// Verify the name
		roleNameDetailViewCtrl.assertContains(roleRecordData.get("roleName"), true);

		// Verify the detail view of the new role
		accountsFieldCtrl.assertContains(roleRecordData.get("accessDisabled"), true);
		contactsFieldCtrl.assertContains(roleRecordData.get("owner"), true);
		notesFieldCtrl.assertContains(roleRecordData.get("all"), true);
		quotesFieldCtrl.assertContains(roleRecordData.get("none"), true);
		reportFieldCtrl.assertContains(roleRecordData.get("owner"), true);

		// Verify the assigned user into Role
		userNameSubpanelCtrl.assertContains(roleRecordData.get("userName"), true);
	}

	/**
	 * Role management_Verify that the customized role can be duplicated.
	 * @throws Exception
	 */
	@Test
	public void Roles_24738_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Duplicate" button
		// TODO: VOOD-856, VOOD-580
		new VoodooControl("span", "css", "#userEditActions li  span").click();
		new VoodooControl("a", "id", "ACLROLE_DUPLICATE_BUTTON").click();
		VoodooControl roleNameCtrl = new VoodooControl("input", "id", "name");

		// Change role name to another one and save.
		roleNameCtrl.waitForVisible();
		roleNameCtrl.set(roleRecordData.get("newRoleName"));
		new VoodooControl("input", "id", "save_button").click();

		// Verify that the detail view of the new role is displayed same as the original one
		roleNameDetailViewCtrl.waitForVisible();

		// Verify the name
		roleNameDetailViewCtrl.assertContains(roleRecordData.get("newRoleName"), true);

		// Verify the detail view of the new role is displayed same as the original one(assert the updated role permission in setup)
		accountsFieldCtrl.assertContains(roleRecordData.get("accessDisabled"), true);
		contactsFieldCtrl.assertContains(roleRecordData.get("owner"), true);
		notesFieldCtrl.assertContains(roleRecordData.get("all"), true);
		quotesFieldCtrl.assertContains(roleRecordData.get("none"), true);
		reportFieldCtrl.assertContains(roleRecordData.get("owner"), true);

		// verify that the user will not be duplicate in user subpanel
		new VoodooControl("em", "css", "#whole_subpanel_users .oddListRowS1 em").assertContains(roleRecordData.get("noData"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}