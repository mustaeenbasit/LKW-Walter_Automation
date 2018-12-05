package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28955 extends SugarTest {
	DataSource roleRecordData;
	VoodooControl moduleCtrl, layoutBtnCtrl, listviewBtnCtrl, saveBtnCtrl;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();

		// Create a role myRole
		AdminModule.createRole(roleRecordData.get(0));
		VoodooUtils.focusFrame("bwc-frame");

		// For Accounts module, Set all field permissions to "Owner Read/Owner Write"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();
		for(int i = 0; i < roleRecordData.size(); i++) {
			// Need to click again on fieldCtrl if permissionsCtrl is not visible as some time script fails to click.
			VoodooControl fieldCtrl = new VoodooControl("div", "id", roleRecordData.get(i).get("fields"));
			VoodooControl permissionsCtrl = new VoodooControl("select", "id", roleRecordData.get(i).get("permissions"));
			fieldCtrl.click();
			if(!permissionsCtrl.queryVisible())
				fieldCtrl.click();
			if(i > roleRecordData.size() - 3)
				permissionsCtrl.set(roleRecordData.get(0).get("readAccess"));
			else
				permissionsCtrl.set(roleRecordData.get(0).get("noneAccess"));
		}

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData.get(0));
		VoodooUtils.waitForReady();

		// Enable Team column in Account's list view
		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Go to Accounts -> Layouts -> Enable the Teams column
		// TODO: VOOD-542
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		saveBtnCtrl = new VoodooControl("input", "id", "savebtn");
		moduleCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1507
		layoutBtnCtrl = new VoodooControl("td", "id", "layoutsBtn");
		listviewBtnCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		listviewBtnCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "li[data-name='team_name']").dragNDrop(new VoodooControl("td", "id", "Default"));
		VoodooUtils.waitForReady();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Verify that Accounts->List view, Team value should be displayed in specific scenario
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_28955_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Navigate to Accounts module to display list view
		sugar().accounts.navToListView();

		// Click edit button on owned/non owned record
		sugar().accounts.listView.editRecord(1);

		// Verify that the 'Team' and 'Name' value should be visible in read only mode rest shows "No access"
		// TODO: VOOD-1445
		String noAccessValue = roleRecordData.get(0).get("noAccess");
		new VoodooControl("a", "css", ".fld_name.detail a").assertContains(sugar().accounts.getDefaultData().get("name"), true);
		new VoodooControl("span", "css", ".fld_team_name.detail").assertContains(roleRecordData.get(0).get("relTeam"), true);
		new VoodooControl("span", "css", ".fld_billing_address_city.noaccess span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".fld_billing_address_country.noaccess span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".fld_phone_office.noaccess span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".fld_assigned_user_name.noaccess span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".fld_email.noaccess span").assertContains(noAccessValue, true);

		// Cancel the Edit View
		sugar().accounts.listView.cancelRecord(1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}