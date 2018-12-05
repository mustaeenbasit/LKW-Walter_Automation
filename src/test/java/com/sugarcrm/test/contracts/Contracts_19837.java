package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19837 extends SugarTest {

	public void setup() throws Exception {
		sugar.contracts.api.create();
		sugar.contacts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Select Contact_Verify that selecting a Contact can be cancelled for Contract.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19828_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Go to contracts module
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1713
		// Click "Select" button in "Contacts" sub-panel.
		new VoodooControl("a", "id", "contracts_contacts_select_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);

		// Click the check boxes of the records in "Contact" list view of pop-up window.
		new VoodooControl("input", "css", ".list.view .oddListRowS1 .checkbox").click();
		
		// Close the pop-up window.
		VoodooUtils.closeWindow();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame"); // Focus BWC frame again

		// Verify that the canceled selected records are not displayed in "Contacts" sub-panel.
		new VoodooControl("a", "css", "#whole_subpanel_contacts tr:nth-child(3) td:nth-child(1) a").assertExists(false);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}