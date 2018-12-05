package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Studio_30018 extends SugarTest {
	FieldSet customData = new FieldSet();
	VoodooControl nameBasicCtrl, searchFormSubmitCtrl, firstRoleCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		// Login
		sugar().login();

		// Define Controls
		// TODO: VOOD-580
		nameBasicCtrl = new VoodooControl("a", "id", "name_basic");
		searchFormSubmitCtrl = new VoodooControl("a", "id", "search_form_submit");
		firstRoleCtrl = new VoodooControl("a", "css", ".oddListRowS1 td:nth-of-type(3) a");

		// Assign any role to "QAUser" say "Customer Support Administrator"
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
		AdminModule.assignUserToRole(customData);

		// Logout from Admin user and Login to Sugar as 'QAUser'
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that user should not get any error message while drag and deploy module from Hidden to Default in Studio.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_30018_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define Controls
		// TODO: VOOD-542 and VOOD-1511
		VoodooControl moduleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl subpanelsBtnCtrl = new VoodooControl("td", "id", "subpanelsBtn");
		VoodooControl callsSubpanelCtrl = new VoodooControl("td", "xpath", "//*[@id='Buttons']/table/tbody/tr[contains(.,'" + sugar().calls.moduleNamePlural + "')]/td[contains(.,'" + sugar().calls.moduleNamePlural + "')]");
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		VoodooControl defaultFieldCtrl = new VoodooControl("li", "css", "#Default .draggable[data-name='created_by_name']");
		VoodooControl hiddenFieldCtrl = new VoodooControl("li", "css", "#Hidden .draggable[data-name='created_by_name']");
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		VoodooControl errorMessageCtrl = new VoodooControl("a", "css", "#sugarMsgWindow .container-close");

		// Navigate to Admin -> Studio -> Accounts -> Sub-panels -> Calls
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		subpanelsBtnCtrl.click();
		VoodooUtils.waitForReady();
		callsSubpanelCtrl.click();
		VoodooUtils.waitForReady();

		// Use try and finally, otherwise in clean up again need to login as QAUser and reset the sub-panel layout
		try {
			// Drag and drop any field from Hidden to Default
			hiddenFieldCtrl.dragNDrop(defaultSubPanelCtrl);
			VoodooUtils.waitForReady();

			// Click on "Save & Deploy" button
			saveBtnCtrl.click();
			VoodooUtils.waitForReady(30000);

			// Verify that the User should not get any error message while drag and deploy module from Hidden to Default in Studio
			errorMessageCtrl.assertExists(false);

			// Verify that the field is displayed in Default column
			defaultFieldCtrl.assertExists(true);
			hiddenFieldCtrl.assertExists(false);
		} finally {
			// Restore Default Layout
			new VoodooControl("input", "id", "historyRestoreDefaultLayout").click();
			VoodooUtils.waitForReady();

			// Save 
			saveBtnCtrl.click();
			VoodooUtils.waitForReady(30000);
			VoodooUtils.focusDefault();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}