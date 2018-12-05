package com.sugarcrm.test.processauthor;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29902 extends SugarTest {
	VoodooControl nameBasicCtrl, searchFormSubmitCtrl, firstRoleCtrl, accountAccessACL, accountAccessSaveBtn, accountAccessSelect;
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();

		// Choose "QAuser" in this Role "Customer Support Administrator"
		// TODO: VOOD-580
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
		
		// Logout as Admin
		sugar().logout();
	}

	/**
	 * Verify that user should not be able to link records in sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessDefinition_29902_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAuser
		sugar().login(sugar().users.getQAUser());
		
		// Import Process Definition (Go to Process Definitions module and create a definition.  eg.  Account start event (new record)> Activity (static assignment to record owner) > End event)
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		
		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
		
		// Log out from QAuser(User1) and Login as Admin
		sugar().logout();
		sugar().login();
		
		// Navigate to Admin -> Roles Management -> Customer Support Administrator -> Set access = Disable for Accounts module and Click on save.
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
		
		// Navigate to all processes module listView
		sugar().processes.navToListView();
		int listViewRowCount = sugar().processes.listView.countRows();
		
		// Verifying that complete data is populated in list view
		Assert.assertTrue("Processes records are not equals to 0 in the processes listView", listViewRowCount == 0);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}