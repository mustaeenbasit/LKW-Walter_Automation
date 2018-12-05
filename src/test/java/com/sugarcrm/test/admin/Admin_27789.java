package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Admin_27789 extends SugarTest {
	FieldSet customData;
	VoodooControl opportunityManagementCtrl, moduleTitleCtrl, bwcFrameCtrl,
	opportunitiesCtrl, revenueLineItemsCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that access to Admin -> Opportunities is granted only for System Admin and Opportunities Developer roles
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_27789_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin -> Opportunities
		// TODO: VOOD-1356
		sugar().navbar.navToAdminTools();
		bwcFrameCtrl = new VoodooControl("iframe", "css", "#bwc-frame");
		bwcFrameCtrl.waitForVisible(20000);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that Admin has access to Opportunities settings under admin
		opportunityManagementCtrl = sugar().admin.adminTools.getControl("opportunityManagement");
		opportunityManagementCtrl.assertVisible(true);
		opportunityManagementCtrl.click();
		VoodooUtils.focusDefault();
		moduleTitleCtrl = new VoodooControl("span", "css", ".module-title");
		moduleTitleCtrl.assertVisible(true);
		sugar().alerts.waitForLoadingExpiration();

		opportunitiesCtrl = new VoodooControl("input", "css", "input[value=Opportunities]");
		revenueLineItemsCtrl = new VoodooControl("input", "css", "input[value=RevenueLineItems]");
		opportunitiesCtrl.assertVisible(true);
		revenueLineItemsCtrl.assertVisible(true);

		// Create a new role
		AdminModule.createRole(customData);

		// Set access for Opportunities module as developer and save.
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-856
		new VoodooControl("div", "css", "#ACLEditView_Access_Opportunities_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Opportunities_access div select").set(customData.get("access"));
		new VoodooControl("div", "css", "#ACLEditView_Access_Opportunities_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Opportunities_admin div select").set(customData.get("access_type"));
		VoodooUtils.waitForReady();

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(customData);
		VoodooUtils.focusDefault();
		sugar().logout();

		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.toggleUserActionsMenu();
		// Verify that Admin link is present for qauser
		sugar().navbar.userAction.getControl("admin").assertVisible(true);
		sugar().navbar.click();

		sugar().navbar.navToAdminTools();

		bwcFrameCtrl.waitForVisible(30000);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that User has access to Opportunities settings under admin
		opportunityManagementCtrl.assertVisible(true);
		opportunityManagementCtrl.click();
		VoodooUtils.focusDefault();
		moduleTitleCtrl.assertVisible(true);
		sugar().alerts.waitForLoadingExpiration();
		opportunitiesCtrl.assertVisible(true);
		revenueLineItemsCtrl.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}