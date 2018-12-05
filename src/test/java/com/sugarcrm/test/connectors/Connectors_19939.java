package com.sugarcrm.test.connectors;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Connectors_19939 extends SugarTest {
	VoodooControl disableDivCtrl,enableDivCtrl, contactsCtrl,
	enableConnectors,saveButton ;

	public void setup() throws Exception {
		sugar().login();
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("connector").click();
		VoodooUtils.waitForReady();
		// TODO - VOOD-637 LIB for Connectors
		enableConnectors = new VoodooControl("img", "css", ".edit.view.small [name='enableImage']");
		VoodooUtils.focusFrame("bwc-frame");
		enableConnectors.click();
		VoodooUtils.waitForReady(20000);
		disableDivCtrl = new VoodooControl("div", "css", ".disabled_module_workarea");
		enableDivCtrl = new VoodooControl("div", "css", ".enabled_module_workarea");
		contactsCtrl = new VoodooControl("li", "css", "#ext_rest_twitter\\:Contacts");
		contactsCtrl.dragNDrop(disableDivCtrl);
		saveButton = new VoodooControl("input", "id", "connectors_top_save");
		saveButton.click();
		VoodooUtils.waitForReady(25000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Drag items between Enable Datasource and Disable Datasource For Modules
	 * 
	 * @throws Exception
	 */
	@Test
	public void Connectors_19939_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin > Connector Settings
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("connector").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		enableConnectors.click();
		VoodooUtils.waitForReady();
		// Verify that Contacts module is displayed in 'disabled' column
		disableDivCtrl.assertContains(sugar().contacts.moduleNamePlural, true);
		contactsCtrl.dragNDrop(enableDivCtrl);
		saveButton.click();
		VoodooUtils.waitForReady(25000);
		enableConnectors.click();
		VoodooUtils.waitForReady();
		// Verify that Contacts module is displayed in 'enabled' column
		enableDivCtrl.assertContains(sugar().contacts.moduleNamePlural, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}