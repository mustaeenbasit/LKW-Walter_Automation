package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_29038 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that ACLRoles action "EditRole" honors option hidden_to_role_assignment
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_29038_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to admin > role management > Create role > Name the role and save it
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Set Accounts module "Access" Level to "Disabled"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
		new VoodooControl("div", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(1) div.aclNot.Set").click();
		if (!new VoodooControl("select", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(1) select.aclNot.Set").queryVisible())
			new VoodooControl("div", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(1) div.aclNot.Set").click();
		new VoodooControl("select", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(1) select.aclNot.Set").set(roleRecord.get("action"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();		
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		
		// Now click on Contacts module
		sugar().navbar.navToModule(sugar().contacts.moduleNamePlural);
		
		// Verify that no Warning Message should be displayed
		sugar().alerts.getWarning().assertExists(false);
		
		// Verify that it is navigating to selected (Contacts) module
		sugar().contacts.listView.assertExists(true);
		sugar().contacts.listView.getControl("moduleTitle").assertContains(sugar().contacts.moduleNamePlural, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}