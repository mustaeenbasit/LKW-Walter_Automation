package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Processes_28328 extends SugarTest {
	FieldSet qaUserData, devRole = new FieldSet();

	public void setup() throws Exception {
		devRole = testData.get(testName).get(0);
		qaUserData = sugar().users.qaUser;
		sugar().login();

		// TODO: VOOD-856
		// Create a Role : "Access type" = "Developer" for Accounts module.
		AdminModule.createRole(devRole);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "id", "ACLEditView_Access_Accounts_admin").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_admin select").
		set(devRole.get("devPermission"));

		// Save the Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();;
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign devRole to qaUser
		AdminModule.assignUserToRole(qaUserData);

		sugar().logout();
	}

	/**
	 * Verify user with developer access type role can access all process author modules
	 * @throws Exception
	 */
	@Test
	public void Processes_28328_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qaUser
		sugar().login(qaUserData);

		// Click the show more downArrow to open the dropdown
		sugar().navbar.showAllModules();

		// TODO VOOD-784
		new VoodooControl("li", "css", ".dropdown-menu li[data-module='pmse_Project']").assertVisible(true);
		new VoodooControl("li", "css", ".dropdown-menu li[data-module='pmse_Inbox']").assertVisible(true);
		new VoodooControl("li", "css", ".dropdown-menu li[data-module='pmse_Business_Rules']").assertVisible(true);
		new VoodooControl("li", "css", ".dropdown-menu li[data-module='pmse_Emails_Templates']").assertVisible(true);

		// Toggle the dropDown state (Close the drop down)
		sugar().navbar.showAllModules();

		// Navigate to the Processes module and click the drop down
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().processes);

		// Verify that Process Management and Unattended processes options are visible
		sugar().processes.menu.getControl("processManagement").assertVisible(true);
		sugar().processes.menu.getControl("unattendedProcesses").assertVisible(true);

		// Navigate to home page and edit the dashboard
		sugar().navbar.navToModule(sugar().home.moduleNameSingular);
		sugar().dashboard.edit();

		// Add the dashlet
		sugar().dashboard.addRow();
		sugar().dashboard.addDashlet(4, 1);

		// TODO: VOOD-960
		// Processes Dashlet is displayed
		new VoodooControl("input", "css", ".inline-drawer .search").set(devRole.get("processesString"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .single td:nth-child(1) a").
		assertEquals(devRole.get("processesString"), true);

		// Process Definitions Dashlet is displayed
		new VoodooControl("input", "css", ".inline-drawer .search").set(devRole.get("processDefString"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .single td:nth-child(1) a").
		assertEquals(devRole.get("processDefString"), true);

		// Process Business Rules dashlet is displayed
		new VoodooControl("input", "css", ".inline-drawer .search").set(devRole.get("processRuleString"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .single td:nth-child(1) a").
		assertEquals(devRole.get("processRuleString"), true);

		// Process Email Templates dashlet is displayed
		new VoodooControl("input", "css", ".inline-drawer .search").set(devRole.get("processEmailString"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .single td:nth-child(1) a").
		assertEquals(devRole.get("processEmailString"), true);

		// Click the cancel link i.e on the Add dashlet drawer
		new VoodooControl("a", "css", ".dashletselect-headerpane.fld_cancel_button a").click();

		// TODO: VOOD-1645
		// Click the cancel link on the Dashboard Edit page
		new VoodooControl("a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .btn-toolbar .fld_cancel_button a").click();
		VoodooUtils.waitForReady();

		// Logout from qaUser to avoid the cleanup from it because it has devRole
		sugar().logout();

		// Login as Admin
		sugar().login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
