package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28338 extends SugarTest {
	UserRecord userChris;
	FieldSet roleRecord = new FieldSet();
	
	public void setup() throws Exception {
		
		//Login with Admin
		sugar().accounts.api.create();
		sugar().login();
		
		// Create and Save a User and  Role 
		userChris= (UserRecord) sugar().users.create();
		roleRecord = testData.get(testName).get(0);
		roleRecord.put("userName", sugar().users.getDefaultData().get("userName"));
		AdminModule.createRole(roleRecord);
		
		// Set the RLI Access cell to Disabled
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("td", "css", 
			"#ACLEditView_Access_RevenueLineItems_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", 
			"td#ACLEditView_Access_RevenueLineItems_access div select").set(roleRecord.get("disabled"));
		
		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		
		// Assign role to default user
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		
		//Logout Admin
		sugar().logout();
	}

	/**
	 * Verify that Message to create RLI does not appear when there is no access to RLI module
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28338_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login with default user chris
		// TODO: VOOD-1727 Increase wait in login() method in UserRecord.java   // userChris.login();
		sugar().login(userChris);
		
		// Navigate to Opp module and create new Opp
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.listView.create();
		
		// Verify no warning/errors occur
		sugar().alerts.getError().assertExists(false);
		sugar().alerts.getWarning().assertExists(false);
		
		// Verify RLI part is not shown
		sugar().opportunities.createDrawer.showMore();
		sugar().opportunities.createDrawer.getEditField("rli_name").assertExists(false);
		
		// Set required data and save
		sugar().opportunities.createDrawer.getEditField("name").
			set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").
			set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.save();
		
		// Close success alert and verify no warning/errors occur
		sugar().alerts.getSuccess().closeAlert();
		sugar().alerts.getError().assertExists(false);
		sugar().alerts.getWarning().assertExists(false);
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}