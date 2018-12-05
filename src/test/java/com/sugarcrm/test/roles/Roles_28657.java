package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_28657 extends SugarTest {
	FieldSet roleRecordData;
	UserRecord myUser;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName).get(0);
		sugar().login();

		// Create user
		myUser = (UserRecord) sugar().users.create();
	}

	/**
	 * Verify PA modules and admin page are shown while modifying an existing role to developer role
	 * @throws Exception
	 */
	@Test
	public void Roles_28657_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a role
		AdminModule.createRole(roleRecordData);

		// Assign a user (Chris) into the Role
		FieldSet userName = new FieldSet();
		userName.put("userName", myUser.get("userName"));
		AdminModule.assignUserToRole(userName);
		userName.clear();

		// Logout from the Admin user
		sugar().logout();

		// Login as Chris User
		sugar().login(myUser);

		// Verify that Only Processes module is available and no admin link is shown
		sugar().navbar.showAllModules();

		// TODO: VOOD-784
		VoodooControl processDefinitions = new VoodooControl("a", "css", "li.dropdown.more li[data-module='pmse_Project'] a");
		VoodooControl processes = new VoodooControl("a", "css", "li.dropdown.more li[data-module='"+sugar().processes.moduleNamePlural+"'] a");
		VoodooControl processBusinessRules = new VoodooControl("a", "css", "li.dropdown.more li[data-module='pmse_Business_Rules'] a");
		VoodooControl processEmailTemplates = new VoodooControl("a", "css", "li.dropdown.more li[data-module='pmse_Emails_Templates'] a");
		processDefinitions.assertExists(false);
		processBusinessRules.assertExists(false);
		processEmailTemplates.assertExists(false);
		processes.assertVisible(true);
		processes.assertContains(roleRecordData.get("processes"), true);

		sugar().navbar.toggleUserActionsMenu();
		sugar().navbar.userAction.getControl(roleRecordData.get("admin")).assertExists(false);
		sugar().navbar.toggleUserActionsMenu();

		// Logout from the Chris user and Login as Admin
		sugar().logout();
		sugar().login();

		// Modify Chris's role
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("rolesManagement").click();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580
		new VoodooControl("input", "id", "name_basic").set(roleRecordData.get("roleName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-child(3) a").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Add Developer access to accounts
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_admin div select").set(roleRecordData.get("access"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Logout from the Admin user and Login as Chris
		sugar().logout();
		myUser.login();

		// Verify that all PA modules show for Chris. Admin and studio show for Chris
		sugar().navbar.showAllModules();
		processDefinitions.assertExists(true);
		processes.assertVisible(true);
		processBusinessRules.assertExists(true);
		processEmailTemplates.assertExists(true);
		processDefinitions.assertContains(roleRecordData.get("processDefinitions"), true);
		processes.assertContains(roleRecordData.get("processes"), true);
		processBusinessRules.assertContains(roleRecordData.get("processBusinessRules"), true);
		processEmailTemplates.assertContains(roleRecordData.get("processEmailTemplates"), true);

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").assertVisible(true);
		VoodooUtils.focusDefault();

		// Need to logout and login as Admin user
		sugar().logout();
		sugar().login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}