package com.sugarcrm.test.forecasts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Forecasts_28827 extends SugarTest {
	FieldSet roleRecord;
	VoodooControl setAction, saveButton, activeDropdown, opportunityCtrl; 

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar.login();
		
		// Enable default Forecast settings
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);
		sugar.forecasts.setup.saveSettings();
		VoodooUtils.waitForReady();
		
		// Go to admin -> Roles and create a new role
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-580, VOOD-856
		opportunityCtrl = new VoodooControl("td", "css", "#ACLEditView_Access_Opportunities_access div:nth-of-type(2)");
		setAction = new VoodooControl("select", "css", "td#ACLEditView_Access_Opportunities_access div select");
		saveButton = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		activeDropdown = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		
		// Set Opportunities access to "Disabled" and Save
		opportunityCtrl.click();
		setAction.set(roleRecord.get("disabled"));
		saveButton.click();
		
		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		
		sugar.logout();
	}

	/**
	 * Opp ONLY:Verify that access to Forecast module is disabled if user has no access to Opportunities module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Forecasts_28827_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log in as qauser
		sugar.login(sugar.users.getQAUser());
		
		// Go to Forecasts module
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);
		
		// Verify- Error message: "Forecasts Access Error: You do not have access to the Forecasts module's records" displayed
		sugar.alerts.getError().assertContains(roleRecord.get("error"), true);
		sugar.alerts.getError().closeAlert();
		sugar.logout();
		
		// Login as admin. Go back to the role
		sugar.login();
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("rolesManagement").click();
		activeDropdown.click();
		new VoodooControl("a", "css", "[data-original-title="+roleRecord.get("roleName")+"]").click();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Change access to "Enabled"
		opportunityCtrl.click();
		setAction.set(roleRecord.get("enabled"));
		saveButton.click();
	    
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar.logout();
		
		// Log in as qauser
		sugar.login(sugar.users.getQAUser());
		
		// Verify that Forecasts module loads and functional
		// TODO: VOOD-929
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural); 
		new VoodooControl("span", "css", ".module-title").assertContains("qauser", true);	
		new VoodooControl("a", "css", "[name='commit_button']").assertVisible(true);
		new VoodooControl("span", "css", ".fld_save_draft_button").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}