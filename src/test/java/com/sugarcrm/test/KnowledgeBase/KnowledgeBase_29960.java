package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29960 extends SugarTest {
	public void setup() throws Exception {
		FieldSet roleData = testData.get("env_role_setup").get(0);

		// Log-In as an Admin
		sugar().login();

		// Enable Knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a Role for KB with permission "Access Type"=Owner Read/Owner Write 
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-856 & VOOD-580
		// Used xpath to click on the field level controls for Knowledge Base
		new VoodooControl("a", "xpath", "//*[contains(@class,'edit')]//tr[contains(.,'Knowledge')]//td/a").click();
		VoodooUtils.waitForReady();

		// Click on Name
		new VoodooControl("div", "id", "namelink").click();
		VoodooUtils.waitForReady();

		// Set Name to as Owner Read/Owner Write
		new VoodooControl("select", "id", "flc_guidname").set(roleData.get("roleOwnerRead/OwnerWrite"));
		VoodooUtils.waitForReady();

		// Save the Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign the role created above to qaUser
		AdminModule.assignUserToRole(sugar().users.getQAUser());

		// Log-out from admin user
		sugar().logout();
	}

	/**
	 * Verify that record name should be shown to regular user.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29960_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login with qauser
		sugar().login(sugar().users.getQAUser());

		// Creating a KB User
		sugar().knowledgeBase.create();

		// Click first record from list view
		sugar().knowledgeBase.listView.clickRecord(1);

		// Select 'Copy' from action dropdown in record view
		sugar().knowledgeBase.recordView.copy();
		sugar().knowledgeBase.createDrawer.save();

		// Verify the record name on KB record view
		VoodooControl nameCtrl = sugar().knowledgeBase.recordView.getDetailField("name");
		nameCtrl.assertVisible(true);
		nameCtrl.assertEquals(sugar().knowledgeBase.getDefaultData().get("name"), true);

		// Updating the KB record name
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.getEditField("name").set(testName);
		sugar().knowledgeBase.recordView.save();

		// Verify the record name on KB record view
		nameCtrl.assertVisible(true);
		nameCtrl.assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}