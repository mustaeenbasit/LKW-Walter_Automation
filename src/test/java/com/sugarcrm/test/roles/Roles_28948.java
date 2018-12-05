package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28948 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().login();
		
		for (int i = 0; i < 2; i++) {
			// Create 2 different roles having different permissions
			roleRecord.put("roleName", testName+"_"+i);
			AdminModule.createRole(roleRecord);
			VoodooUtils.focusFrame("bwc-frame");
			
			if (i== 0) {
				// TODO: VOOD-580, VOOD-856
				new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
				sugar().admin.studio.waitForAJAX();
			}
			else {
				new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().calls.moduleNamePlural+"')]").click();
				sugar().admin.studio.waitForAJAX();
				VoodooUtils.waitForReady();
			}
			
			// Set "Delete" and "Edit" permissions to "Not Set" and rest are set as "None"
			// TODO: VOOD-580, VOOD-856
			for (int j = 0; j < 5; j++) {
				new VoodooControl("div", "css", "#ACLEditView table:nth-child(11) td:nth-child("+(j+5)+") div.aclNot.Set").click();
				VoodooUtils.waitForReady();
				new VoodooControl("select", "css", "#ACLEditView table:nth-child(11) td:nth-child("+(j+5)+") div:nth-child(1) select").set(roleRecord.get("action"));
				VoodooUtils.waitForReady();
			}
			new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
			
			// Assign QAuser to the Role
			VoodooUtils.focusDefault();
			AdminModule.assignUserToRole(roleRecord);
			VoodooUtils.waitForReady();
		}
		sugar().logout();
	}

	/**
	 * Verify that user shouldn't get any error message while creating calls
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_28948_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Log in as qauser
		sugar().login(sugar().users.getQAUser());
		
		// Navigate to Calls module
		sugar().calls.navToListView();
		
		// Close alert
		sugar().alerts.closeAllAlerts();
		
		// Click on "Log Call"
		sugar().navbar.clickModuleDropdown(sugar().calls);
		sugar().calls.menu.getControl("createCall").click();

		// Create a Call and click on "Save" button
		sugar().calls.createDrawer.getEditField("name").set(sugar().calls.getDefaultData().get("name"));
		sugar().calls.createDrawer.save();
		
		// Verify that Success message shown instead of error message
		sugar().alerts.getSuccess().assertContains(roleRecord.get("successString")+" "+sugar().calls.getDefaultData().get("name"), true);
		sugar().alerts.getError().assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
