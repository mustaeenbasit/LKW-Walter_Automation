package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29604 extends SugarTest {

	public void setup() throws Exception {
		FieldSet roleData = testData.get(testName).get(0);

		// Log-In as an Admin
		sugar().login();

		// Enable Knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a Role for KB with permission "Access Type"=Developer&Admin 
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-580 - Create a Roles (ACL) Module LIB
		new VoodooControl("td", "id", "ACLEditView_Access_KBContents_admin").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_KBContents_admin select").set(roleData.get("accessTypeAdminAndDev"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.focusDefault();

		// Assign the role created above to qaUser
		AdminModule.assignUserToRole(sugar().users.qaUser);

		// Log-out from admin user
		sugar().logout();
	}

	/**
	 * Verify user with Developer&Admin access type can create KB templates
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29604_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log-in as qaUser
		sugar().login(sugar().users.qaUser);

		// qaUser should be able to create a template like any other Developer&Admin user.
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createTemplate");
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.save();

		// Navigate to the KB Templates list view and assert the Template created
		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);
		sugar().knowledgeBase.menu.getControl("viewTemplates").click();
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}