package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21239 extends SugarTest {
	DataSource roleRecord = new DataSource();
	VoodooControl casesModuleCtrl, selectAccessRole, saveRoleButton, editLink, unfollowLink, deleteLink, checkbox;

	public void setup() throws Exception {
		roleRecord = testData.get(testName);
		sugar().cases.api.create();
		sugar().login();

		// Admin -> Role management -> Select a module (Cases) -> Set Access Role to be Enabled
		AdminModule.createRole(roleRecord.get(0));
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		casesModuleCtrl = new VoodooControl("div", "css", "td#ACLEditView_Access_Cases_access div:nth-of-type(2)");
		selectAccessRole = new VoodooControl("select", "css", "td#ACLEditView_Access_Cases_access div select");
		saveRoleButton = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		
		casesModuleCtrl.click();
		selectAccessRole.set(roleRecord.get(0).get("access"));
		saveRoleButton.click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord.get(0));
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Verify user can access to a module while access role is set to Enabled/Not Set
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21239_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Click module from navigation bar
		sugar().cases.navToListView();
		
		// Verify that it should show the full actions from the navigation bar
		sugar().cases.listView.verifyModuleTitle(sugar().cases.moduleNamePlural);
		
		// Select a record and check drop down action menu in the list view
		checkbox = sugar().cases.listView.getControl("checkbox01");
		checkbox.click();
		sugar().cases.listView.openRowActionDropdown(1);
		
		// Verify that full action lists from the drop down menu displayed
		editLink = sugar().cases.listView.getControl("edit01");
		unfollowLink = sugar().cases.listView.getControl("unfollow01");
		deleteLink = sugar().cases.listView.getControl("delete01");
		editLink.assertVisible(true);
		unfollowLink.assertVisible(true);
		deleteLink.assertVisible(true);
		sugar().cases.listView.openRowActionDropdown(1);
		
		// Open action dropdown
		sugar().cases.listView.openActionDropdown();
		
		// TODO: VOOD-689
		VoodooControl massUpdateButton = sugar().cases.listView.getControl("massUpdateButton");
		VoodooControl deleteButton = sugar().cases.listView.getControl("deleteButton");
		VoodooControl exportButton = sugar().cases.listView.getControl("exportButton");
		
		// Verify that full action lists from the drop down menu displayed on the listview page
		VoodooControl mergeButton = new VoodooControl("a", "name", "merge_button");
		massUpdateButton.assertVisible(true);
		deleteButton.assertVisible(true);
		exportButton.assertVisible(true);
		mergeButton.assertVisible(true);
		
		// Click a record and check the drop down action menu in the detail view
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-695
		VoodooControl editButton = sugar().cases.recordView.getControl("editButton");
		VoodooControl deleteButtonInRecordView = sugar().cases.recordView.getControl("deleteButton");
		VoodooControl copyButton = sugar().cases.recordView.getControl("copyButton");
		VoodooControl shareLink = new VoodooControl("a", "css", ".fld_share.detail a");
		VoodooControl createArticle = new VoodooControl("a", "css", ".dropdown-menu .fld_create_button a");
		VoodooControl findDuplicates = new VoodooControl("a", "css", ".fld_find_duplicates_button");
		VoodooControl historicalSummary = new VoodooControl("a", "css", ".fld_historical_summary_button");
		VoodooControl viewChangeLog = new VoodooControl("a", "css", ".fld_audit_button");
		
		// Verify the action list in the record view
		editButton.assertVisible(true);
		deleteButtonInRecordView.assertVisible(true);
		copyButton.assertVisible(true);
		shareLink.assertVisible(true);
		createArticle.assertVisible(true);
		findDuplicates.assertVisible(true);
		historicalSummary.assertVisible(true);
		viewChangeLog.assertVisible(true);
		sugar().cases.recordView.openPrimaryButtonDropdown(); // Close primary button
		
		// Click Edit button to the edit view page
		sugar().cases.recordView.edit();
		
		// Verify that edit view page displayed
		VoodooControl cancelButton = sugar().cases.recordView.getControl("cancelButton");
		VoodooControl saveButton = sugar().cases.recordView.getControl("saveButton");
		VoodooControl nameField = sugar().cases.createDrawer.getEditField("name");
		cancelButton.assertVisible(true);
		saveButton.assertVisible(true);
		nameField.assertVisible(true);
		
		// Logout Qauser and login as Admin
		sugar().logout();
		sugar().login();
		
		// Admin -> Role management -> Select a module (Cases) -> Set Access Role to be 'Not Set'
		AdminModule.createRole(roleRecord.get(1));
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		casesModuleCtrl.click();
		selectAccessRole.set(roleRecord.get(1).get("access"));
		saveRoleButton.click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord.get(1));
		VoodooUtils.waitForReady();
		sugar().logout();
		
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
		
		// Click module from navigation bar
		sugar().cases.navToListView();
		
		// Verify that it should show the full actions from the navigation bar
		sugar().cases.listView.verifyModuleTitle(sugar().cases.moduleNamePlural);
		
		// Select a record and check drop down action menu in the list view
		checkbox.click();
		sugar().cases.listView.openRowActionDropdown(1);
		
		// Verify that full action lists from the drop down menu displayed
		editLink.assertVisible(true);
		unfollowLink.assertVisible(true);
		deleteLink.assertVisible(true);
		sugar().cases.listView.openRowActionDropdown(1);
		
		// Open action dropdown
		sugar().cases.listView.openActionDropdown();
		
		// Verify that full action lists from the drop down menu displayed on the listview page
		massUpdateButton.assertVisible(true);
		deleteButton.assertVisible(true);
		exportButton.assertVisible(true);
		mergeButton.assertVisible(true);
		
		// Click a record and check the drop down action menu in the detail view
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.openPrimaryButtonDropdown();
		
		// Verify the action list in the record view
		editButton.assertVisible(true);
		deleteButtonInRecordView.assertVisible(true);
		copyButton.assertVisible(true);
		shareLink.assertVisible(true);
		createArticle.assertVisible(true);
		findDuplicates.assertVisible(true);
		historicalSummary.assertVisible(true);
		viewChangeLog.assertVisible(true);
		sugar().cases.recordView.openPrimaryButtonDropdown(); // Close primary button
		
		// Click Edit button to the edit view page
		sugar().cases.recordView.edit();
		
		// Verify that edit view page displayed
		cancelButton.assertVisible(true);
		saveButton.assertVisible(true);
		nameField.assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}