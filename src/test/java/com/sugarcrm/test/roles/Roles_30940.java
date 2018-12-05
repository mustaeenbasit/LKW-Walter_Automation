package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_30940 extends SugarTest {

	public void setup() throws Exception {

		// Creating account which is assigned to Admin
		sugar().accounts.api.create();
		FieldSet roleRecord = testData.get("env_role_setup").get(0);

		// Login as admin
		sugar().login();

		// Admin -> Role management -> Creating Role
		AdminModule.createRole(roleRecord);

		// Select "Account" module
		// TODO: VOOD-856
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".edit.view tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-856 VOOD-580 -Create a Roles (ACL) Module LIB,
		// Set Industry as "Owner Read/Owner Write"
		new VoodooControl("div", "css", "#industrylink").scrollIntoViewIfNeeded(true);
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#flc_guidindustry").set(roleRecord.get("roleOwnerRead/OwnerWrite"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();

		// Logout
		sugar().logout();
	}

	/**
	 *  Verify Industry field is in writable mode.
	 * @throws Exception
	 */
	@Test
	public void Roles_30940_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Navigate to account record which is assigned to admin
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// TODO: VOOD:854
		// Verifying pencil icon is not shown
		VoodooControl industryDetailFieldCtrl = sugar().accounts.recordView.getDetailField("industry");
		VoodooControl industryFieldPencilIcon = new VoodooControl("i", "css", "[data-name='industry'] .fa.fa-pencil");
		industryDetailFieldCtrl.hover();
		industryFieldPencilIcon.assertVisible(false);

		// Verifying No access is appearing on 'Industry' field.
		industryDetailFieldCtrl.assertEquals(customData.get("permission"), true);

		// Assign this account record to qauser
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("relAssignedTo").set(customData.get("relAssignedTo"));
		sugar().accounts.recordView.save();

		// Verifying pencil icon is shown after assigning record to qauser
		industryDetailFieldCtrl.hover();
		industryFieldPencilIcon.assertVisible(true);

		// Verifying No access is not appearing on 'Industry' field.
		industryDetailFieldCtrl.assertEquals(customData.get("permission"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}