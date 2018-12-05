package com.sugarcrm.test.tasks;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Tasks_31225 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
	}

	/**
	 * Tasks : Verify that "Related to" field should not be disabled for other enabled module
	 * @throws Exception
	 */
	@Test
	public void Tasks_31225_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet roleRecord = testData.get("env_role_setup").get(0);
		DataSource moduleNames = testData.get(testName);
		VoodooControl relatedToParentTypeCtrl = sugar().tasks.createDrawer.getEditField("relRelatedToParentType");
		VoodooControl relatedToParentCtrl = sugar().tasks.createDrawer.getEditField("relRelatedToParent");
		FieldSet qaUser = sugar().users.getQAUser();

		// Login as QAUser
		sugar().login(qaUser);

		// Create a new Task
		sugar().navbar.navToModule(sugar().tasks.moduleNamePlural);
		sugar().tasks.listView.create();
		sugar().tasks.createDrawer.getEditField("subject").set(testName);
		relatedToParentTypeCtrl.set(sugar().accounts.moduleNameSingular);
		relatedToParentCtrl.set(sugar().accounts.getDefaultData().get("name"));
		sugar().tasks.createDrawer.save();
		sugar().logout();

		// Create role with Account access type disabled
		sugar().login();
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-580 Create a Roles (ACL) Module LIB 
		// TODO: VOOD-856 Lib is needed for Roles Management
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_access").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_access div select").set(moduleNames.get(0).get("access"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();

		// Login as qauser and click on edit
		sugar().login(qaUser);
		sugar().navbar.navToModule(sugar().tasks.moduleNamePlural);
		sugar().tasks.listView.clickRecord(1);
		sugar().tasks.recordView.edit();

		// Verify that in the "Related to" field, accounts module present and "Related type" value is disabled
		// TODO: VOOD-1445 Need lib support for enhanced disabled check in parent controls of a child
		relatedToParentTypeCtrl.set(sugar().accounts.moduleNameSingular);
		Assert.assertTrue("RelatedTo value is not disabled", new VoodooControl("div", "class", "select2-container-disabled").isDisabled());

		// Verify that "Related type" value is enabled for other modules
		for (int i = 0; i < moduleNames.size(); i++) {
			relatedToParentTypeCtrl.set(moduleNames.get(i).get("modules"));
			relatedToParentCtrl.assertVisible(true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}