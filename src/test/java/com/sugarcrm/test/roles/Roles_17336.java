package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Roles_17336 extends SugarTest {
	FieldSet roleRecord = new FieldSet();
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		roleRecord = testData.get("env_role_setup").get(0);
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().cases.api.create();
		sugar().login();

		// Create role => Accounts => Edit=None
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580, VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_edit").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_edit div select").set(customData.get("edit"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Make sure menu is taking effect when Edit access is set to none in role
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_17336_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Go to accounts module
		sugar().navbar.clickModuleDropdown(sugar().accounts);

		// Verify that "Create Account", "Build A List" aren't displayed. But "Import Account" will still show.
		sugar().accounts.menu.getControl("importAccounts").assertVisible(true);
		sugar().accounts.menu.getControl("createAccount").assertVisible(false);

		// TODO: VOOD-1495
		new VoodooControl("a", "css", "li[data-module='Accounts'] ul[role='menu'] a[data-navbar-menu-item='LBL_BAL']").assertVisible(false);
		sugar().navbar.clickModuleDropdown(sugar().accounts); // close icon-caret menu dropdown

		// Verify that "Create Account" should not display from quick create
		sugar().navbar.openQuickCreateMenu();
		sugar().navbar.quickCreate.getControl(sugar().accounts.moduleNamePlural).assertVisible(false);
		sugar().navbar.quickCreate.getControl("quickCreateActions").click(); // to close quick create menu option

		// Verify No Edit related action is displayed in the list view action: eg. No Mass Update, No merge.
		sugar().accounts.navToListView();
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.getControl("massUpdateButton").assertVisible(false);

		// TODO: VOOD-657
		new VoodooControl("a", "css", ".fld_merge_button a").assertVisible(false);
		sugar().accounts.listView.getControl("actionDropdown").click(); // to close dropdown

		// Verify No Edit from the inline edit action should display
		sugar().accounts.listView.openRowActionDropdown(1);
		sugar().accounts.listView.getControl("edit01").assertVisible(false);
		sugar().accounts.listView.getControl("dropdown01").click(); // to close row action dropdown
		sugar().accounts.listView.toggleSelectAll(); // toggle on selection records

		// Verify No Edit related action is displayed in the record action: eg. No Edit, No Copy
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.getControl("editButton").assertVisible(false);
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		sugar().accounts.recordView.getControl("copyButton").assertVisible(false);
		sugar().accounts.recordView.getControl("primaryButtonDropdown").click(); // to close primary dropdown

		// TODO: VOOD-854
		// Verify no inline edit/pencil icon on fields
		sugar().accounts.recordView.getDetailField("name").hover();
		new VoodooControl ("i", "css", "span[data-name='name'] .fa.fa-pencil").assertVisible(false);
		sugar().accounts.recordView.getDetailField("website").hover();
		new VoodooControl ("i", "css", "span[data-name='website'] .fa.fa-pencil").assertVisible(false);
		sugar().accounts.recordView.getDetailField("relAssignedTo").hover();
		new VoodooControl ("i", "css", "span[data-name='assigned_user_name'] .fa.fa-pencil").assertVisible(false);
		sugar().accounts.recordView.getDetailField("industry").hover();
		new VoodooControl ("i", "css", "span[data-name='industry'] .fa.fa-pencil").assertVisible(false);
		sugar().accounts.recordView.getDetailField("type").hover();
		new VoodooControl ("i", "css", "span[data-name='account_type'] .fa.fa-pencil").assertVisible(false);
		sugar().accounts.recordView.getDetailField("workPhone").hover();
		new VoodooControl ("i", "css", "span[data-name='phone_office'] .fa.fa-pencil").assertVisible(false);

		// Verify create(+) button is disabled for subpanels
		StandardSubpanel memberOrganization = sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		memberOrganization.scrollIntoViewIfNeeded(false);
		Assert.assertTrue("+ icon is enabled", memberOrganization.getControl("addRecord").isDisabled());
		memberOrganization.clickLinkExisting();
		sugar().accounts.searchSelect.getControl("link").assertEquals(customData.get("add_text"), true);
		sugar().accounts.searchSelect.cancel();

		// Verify Create button is not displayed on SSV, through Sidecar
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.edit();
		VoodooSelect relAccountName = (VoodooSelect)sugar().cases.recordView.getEditField("relAccountName");
		relAccountName.clickSearchForMore();
		sugar().accounts.searchSelect.getControl("create").assertVisible(false);
		sugar().accounts.searchSelect.cancel();
		sugar().cases.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}