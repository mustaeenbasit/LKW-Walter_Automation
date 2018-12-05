package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_17084 extends SugarTest {
	DataSource accountFields = new DataSource();
	FieldSet qauser = new FieldSet();

	public void setup() throws Exception {
		accountFields = testData.get(testName);
		sugar().accounts.api.create(accountFields);
		sugar().login();

		// Add "Assigned To" field in one record
		sugar().accounts.navToListView();
		// Sorting records to assigned To field to one specific record.
		sugar().accounts.listView.sortBy("headerName", true);
		FieldSet roleFields = testData.get("env_role_setup").get(0);
		sugar().accounts.listView.editRecord(2);
		qauser = sugar().users.getQAUser();
		sugar().accounts.listView.getEditField(2, "relAssignedTo").set(qauser.get("userName"));
		sugar().accounts.listView.saveRecord(2);

		// Set Accounts ACL to "Owner" for "View","Edit","Delete"
		AdminModule.createRole(roleFields);
		AdminModule.assignUserToRole(roleFields);

		// TODO: VOOD-856 Native lib for Role Management should be used here
		// Set Accounts ACL to "Owner" for "View","Edit","Delete"
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",	"td#ACLEditView_Access_Accounts_view div:nth-of-type(2)").click();
		FieldSet customData = testData.get(testName+"_roledata").get(0);
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_view div select").set(customData.get("permission"));

		new VoodooControl("div", "css",	"td#ACLEditView_Access_Accounts_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_edit div select").set(customData.get("permission"));

		new VoodooControl("div", "css",	"td#ACLEditView_Access_Accounts_delete div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_delete div select").set(customData.get("permission"));

		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify action list on list view + preview button - ACL View, Edit, Delete set to owner
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_17084_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().logout();
		sugar().login(qauser);
		sugar().accounts.navToListView();
		sugar().accounts.listView.sortBy("headerName", true);

		// Click "Preview" button on owned accounts.
		sugar().accounts.listView.previewRecord(2);
		sugar().previewPane.assertContains(accountFields.get(1).get("name"), true);

		// Click "Edit" button on drop down on owned accounts
		// The fields on the row should be editable
		sugar().accounts.listView.editRecord(2);
		sugar().accounts.getField("name").getListViewEditControl(2).set(testName);
		sugar().accounts.getField("name").getListViewEditControl(2).assertEquals(testName, true);
		sugar().accounts.listView.cancelRecord(2);

		// Click "Delete"
		sugar().accounts.listView.deleteRecord(2);
		sugar().accounts.listView.cancelDelete();

		// View non-owned records on the list view.
		// Verify that the "Preview", "Edit", and "Delete" buttons are not displayed.
		sugar().accounts.listView.getControl("dropdown01").assertVisible(false);
		sugar().accounts.listView.getControl("preview01").assertVisible(false);
		sugar().accounts.listView.getControl("edit01").assertVisible(false);
		sugar().accounts.listView.getControl("delete01").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}