package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.views.CreateDrawer;
import com.sugarcrm.sugar.views.ListView;
import com.sugarcrm.test.SugarTest;

public class Roles_30065 extends SugarTest {

	public void setup() throws Exception {
		DataSource rolesData = testData.get(testName);

		// Login as an Admin
		sugar().login();

		// Create Chris User
		sugar().users.create();

		// Create 2 roles - qaUser Role and Chris Role
		AdminModule.createRole(rolesData.get(0));
		AdminModule.createRole(rolesData.get(1));

		// Admin goes to Studio->Accounts->Fields, creates a new drop-down type field and uses "account_type_dom" and save it   
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1504 - Support Studio Module Fields View
		VoodooControl studioAccountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		studioAccountsCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		// Create a new drop-down type field and uses "account_type_dom" and save it
		new VoodooControl("input", "css", "input[name='addfieldbtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "type").set(rolesData.get(0).get("dropDownTypeField"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(rolesData.get(0).get("fieldName"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name='fsavebtn']").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1506 - Support Studio Module RecordView Layouts View
		VoodooControl studioAccountsLayoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl studioAccountsRecordViewCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		VoodooControl selectRoleDropdown = new VoodooControl("select", "id", "roleList");
		VoodooControl saveAndDeployBtn = new VoodooControl("input", "id", "publishBtn");

		// Admin goes to Studio->Accounts->Layouts->Record View
		new VoodooControl("a", "css", "a.crumbLink:nth-child(4)").click();
		VoodooUtils.waitForReady();
		studioAccountsLayoutCtrl.click();
		studioAccountsRecordViewCtrl.click();

		String qaRole = rolesData.get(0).get("roleName");

		// Select qaUser Role, add this field, Save&Deploy
		selectRoleDropdown.set(qaRole);
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", ".le_row.special").dragNDrop(new VoodooControl("div", "css", ".le_panel .le_row:nth-of-type(2)"));
		new VoodooControl("div", "css", "div[data-name='customdropdown_c']").dragNDrop(new VoodooControl("span", "css", ".le_panel .le_row:nth-of-type(3) span"));
		saveAndDeployBtn.click();
		VoodooUtils.waitForReady();

		// Admin goes to DropDown Editor->account_type_dom, select qaUser Role
		sugar().admin.studio.clickDropdownEditor();

		// TODO: VOOD-781 - need lib support of dropdown editor functions
		new VoodooControl("a", "css", "#dropdowns tbody tr td a.mbLBLL").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "select[name='dropdown_role']").set(qaRole);
		VoodooUtils.waitForReady();

		// Only keep 3 values in the list(Analyst, Competitor and Customer), Save it
		new VoodooControl("input", "id", "select-none").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name='dropdown_keys[Analyst]'][type='checkbox']").click();
		new VoodooControl("input", "css", "input[name='dropdown_keys[Competitor]'][type='checkbox']").click();
		new VoodooControl("input", "css", "input[name='dropdown_keys[Customer]'][type='checkbox']").click();
		new VoodooControl("input", "id", "saveBtn").click();
		VoodooUtils.waitForReady();

		// Admin goes to Studio->Accounts->Layouts->Record View
		sugar().admin.studio.clickStudio();
		studioAccountsCtrl.click();
		VoodooUtils.waitForReady();
		studioAccountsLayoutCtrl.click();
		studioAccountsRecordViewCtrl.click();

		// Select Chris Role 
		selectRoleDropdown.set(rolesData.get(1).get("roleName"));
		VoodooUtils.waitForReady();

		// Copy from qaUser Role
		// TODO: VOOD-1506 - Support Studio Module RecordView Layouts View
		new VoodooControl("input", "id", "copyBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("button", "css", "#copy-from-dialog .ft button").click();
		saveAndDeployBtn.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// logout from Admin
		sugar().logout();
	}

	/**
	 * Verify that Roles module should display in Module tab.
	 * @throws Exception
	 */
	@Test
	public void Roles_30065_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String accountModuleNamePlural = sugar().accounts.moduleNamePlural;
		CreateDrawer accountCreateDrawer = sugar().accounts.createDrawer;
		VoodooControl accountNameEditField = accountCreateDrawer.getEditField("name");
		ListView accountListView = sugar().accounts.listView;

		// Log-In as qaUser and create an Account record
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.navToModule(accountModuleNamePlural);
		accountListView.create();
		accountNameEditField.set(sugar().accounts.getDefaultData().get("name"));
		accountCreateDrawer.save();
		// Log-out from qaUser
		sugar().logout();

		// Log-In as Chris User and create an Account record
		sugar().login(sugar().users.getDefaultData());
		sugar().navbar.navToModule(accountModuleNamePlural);
		accountListView.create();
		accountNameEditField.set(testName);
		accountCreateDrawer.save();
		// Log-out from Chris User
		sugar().logout();

		// Log-In as Admin again
		sugar().login();

		// Navigate to Admin page and click Roles Management link
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("rolesManagement").click();
		VoodooUtils.focusDefault();

		// Roles management module should display in Module tab
		// TODO: VOOD-827 - Need lib support to verify active module in mega menu
		new VoodooControl("a", "css", "li.active[data-module='ACLRoles'] a[aria-label='Roles']").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}