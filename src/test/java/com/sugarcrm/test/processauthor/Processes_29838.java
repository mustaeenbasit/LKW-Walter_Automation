package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Processes_29838 extends SugarTest {
	VoodooControl nameBasicCtrl, searchFormSubmitCtrl, firstRoleCtrl, accountAccessACL, accountAccessSaveBtn, accountAccessSelect;
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
		
		// TODO: VOOD-580
		// Choose "QAuser" in this Role "Customer Support Administrator"
		nameBasicCtrl = new VoodooControl("a", "id", "name_basic");
		searchFormSubmitCtrl = new VoodooControl("a", "id", "search_form_submit");
		firstRoleCtrl = new VoodooControl("a", "css", ".oddListRowS1 td:nth-of-type(3) a");

		// Assign "Customer Support Administrator" role to "QAUser"
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("rolesManagement").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		nameBasicCtrl.set(customData.get("roleName"));
		searchFormSubmitCtrl.click();
		VoodooUtils.waitForReady();
		firstRoleCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		FieldSet fs = new FieldSet();
		fs.put("roleName", customData.get("roleName"));
		fs.put("userName", customData.get("userName"));
		AdminModule.assignUserToRole(fs);
	}

	/**
	 * Verify the disabled module is not displayed in the Target Module list for PAs 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Processes_29838_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-580
		// Navigate to Admin -> Roles Management -> Customer Support Administrator -> Set access = Disable for Accounts module and Click on save.
		VoodooUtils.focusFrame("bwc-frame");
		accountAccessACL = new VoodooControl("td", "id", "ACLEditView_Access_Accounts_access");
		accountAccessACL.click();
		VoodooUtils.waitForReady();
		accountAccessSelect = new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_access select");
		accountAccessSelect.set(customData.get("accessTypeSet"));
		accountAccessSaveBtn = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		accountAccessSaveBtn.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		
		// Log out from Admin and Login as QAuser(User1)
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		
		// Navigate to all processDefinitions modules
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.create();
		
		// Verify that the disabled module is not displayed in the Target module list for processDefinitions modules.
		sugar().processDefinitions.createDrawer.getEditField("targetModule").assertContains(customData.get("verifyText"), false);
		
		// Navigate to all processDefinitions modules
		sugar().processBusinessRules.navToListView();
		sugar().processBusinessRules.listView.create();
		
		// Verify that the disabled module is not displayed in the Target module list for processBusinessRules modules.
		sugar().processBusinessRules.createDrawer.getEditField("targetModule").assertContains(customData.get("verifyText"), false);
		
		// Navigate to all processDefinitions modules
		sugar().processEmailTemplates.navToListView();
		sugar().processEmailTemplates.listView.create();
		
		// Verify that the disabled module is not displayed in the Target module list for processEmailTemplates modules.
		sugar().processEmailTemplates.createDrawer.getEditField("targetModule").assertContains(customData.get("verifyText"), false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}