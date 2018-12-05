package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_30778 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		
		// Configuring the Forecasts setting
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
	}

	/**
	 * Verify that Forecast Worksheet "Commit" and "Save Draft" buttons not enabled when Forecast list ACL set to none
	 * @throws Exception
	 */
	@Test
	public void Roles_30778_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet roleRecordData = testData.get(testName).get(0);

		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Forecasts" module and set its 'List' role to be 'None'
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Forecasts_list div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Forecasts_list div select").set(roleRecordData.get("list"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) to the Role
		AdminModule.assignUserToRole(roleRecordData);

		// Logout as Admin
		sugar().logout();
		
		// Login as QAuser
		sugar().login(sugar().users.getQAUser());
		
		// Create a new Opportunity with a new RLI entry set as "Include"
		FieldSet oppData = new FieldSet();
		oppData.put("forecast", roleRecordData.get("forecast"));
		oppData.put("rli_expected_closed_date", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar().opportunities.create(oppData);
		
		// Navigate to Forecasts Module
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		
		// Verify Save Draft button is displayed as disabled on Forecasts module 
		// TODO: VOOD-929 
		Assert.assertTrue("Save Draft button is not disabled when it should!", new VoodooControl("a", "css", ".save-draft-button").isDisabled());
		
		// Verify Commit button is displayed as disabled on Forecasts module 
		Assert.assertTrue("Forecast Commit button is not disabled when it should!", new VoodooControl("a", "css", ".commit-button").isDisabled());
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}