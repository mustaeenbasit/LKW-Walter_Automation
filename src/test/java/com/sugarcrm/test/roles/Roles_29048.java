package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_29048 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().login();

		// Go to admin > role management > Create role > Name the role and save it
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Set "Access" and "Access type" actions to "Disabled" and "Admin & Developer" for contacts module
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[contains(@class,'edit')][contains(@class,'view')]//tr[contains(.,'Contacts')]//td/a").click();
		VoodooUtils.waitForReady();
		
		// Set Access
		new VoodooControl("div", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(1) div.aclNot.Set").click();
		new VoodooControl("select", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(1) select.aclNot.Set").set(roleRecord.get("set_access"));
		
		// Set Access Type
		new VoodooControl("div", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(2) div.aclNot.Set").click();
		new VoodooControl("select", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(2) select.aclNot.Set").set(roleRecord.get("set_access_type"));
		
		// Save changes
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Assign QAuser to the Role and logout
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Verify user is not able to create a report for disabled module when its Access Type is set to "Admin & Developer".
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_29048_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		
		// TODO: VOOD-822
		// Go to Reports -> Create reports -> Rows and Columns -> Accounts 
		sugar().reports.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "img[name='rowsColsImg']").click();
		
		// Verify that Contacts module should NOT be available in the "select module" page
		// since the Access is set to "Disabled" irrespective of its Access Type which is "Admin & Developer".
		new VoodooControl("table", "id", "Contacts").assertExists(false);
		VoodooUtils.focusDefault();
		
		// TODO: VOOD-1543
		sugar().logout();
		sugar().login();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}