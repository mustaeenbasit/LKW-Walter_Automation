package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_29028 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().login();

		// Go to admin > role management > Create role > Name the role and save it
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Set "Edit" and "Delete" actions to NONE for accounts module
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
		for (int i = 0; i < 2; i++) {
			new VoodooControl("div", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child("+(i+3)+") div.aclNot.Set").click();
			if (!new VoodooControl("select", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child("+(i+3)+") select.aclNot.Set").queryVisible())
				new VoodooControl("div", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child("+(i+3)+") div.aclNot.Set").click();
			new VoodooControl("select", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child("+(i+3)+") select.aclNot.Set").set(roleRecord.get("action"));
		}
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		sugar().alerts.waitForLoadingExpiration();

		// Assign QAuser to the Role and logout
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Verify user is re-directed to respective employee's record view when clicked on any employee shown 
	 * in employees list view while a role is assigned to the current user.
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_29028_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		
		// Go to Employees module
		// TODO: VOOD-1041
		sugar().navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click on any employee (qauser) from the list view
		new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr.evenListRowS1 > td:nth-child(2) > span > b > a").click();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that user re-directe to the respective employee's record view
		new VoodooControl("h2", "css", "div.moduleTitle h2").assertContains(sugar().users.getQAUser().get("userName"), true);
		new VoodooControl("span", "id", "first_name").assertContains(sugar().users.getQAUser().get("userName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}