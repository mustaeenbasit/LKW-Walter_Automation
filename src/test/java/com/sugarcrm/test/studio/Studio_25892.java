package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25892 extends SugarTest {
	VoodooControl moduleCtrl, layoutSubPanelCtrl, detailView, saveAndDeploy, emailFieldCtrl;

	public void setup() throws Exception {
		sugar().login();

		// Modify detail view layout of employees module
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-542 and VOOD-1506
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Employees");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		detailView = new VoodooControl("td", "id", "viewBtndetailview");
		detailView.click();
		VoodooUtils.waitForReady();

		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#toolbox #availablefields div.le_field.special");
		emailFieldCtrl = new VoodooControl("div", "css", "#panels div[data-name='email']");
		emailFieldCtrl.dragNDrop(moveToNewFilter);
		new VoodooControl("div", "css", "#toolbox div[data-name='email']").waitForVisible();
		saveAndDeploy = new VoodooControl("input", "id", "publishBtn");
		saveAndDeploy.click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();

		// Logout and login
		sugar().logout();
		sugar().login();
	}

	/**
	 * Restore default detail view layout of Employees module.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_25892_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Restore Default in Employees -> Layouts -> DetailView
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		detailView.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "historyDefault").click();
		VoodooUtils.waitForReady();

		// Verify the default detail view layout is shown up
		emailFieldCtrl.assertExists(true);
		saveAndDeploy.click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();

		// Verify detail view page of an employee record, the default detail view layout will be used.
		sugar().navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1041, Need Lib support for Employees module.
		new VoodooControl("a", "css", "#MassUpdate tr.oddListRowS1 td:nth-child(3) a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", "#DEFAULT #email_span").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}