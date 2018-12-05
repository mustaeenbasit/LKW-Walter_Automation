package com.sugarcrm.test.contracts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19824 extends SugarTest {

	public void setup() throws Exception {
		sugar().contracts.api.create();
		sugar().login();
		// enabled  Contract module in Admin->Display Modules and Subpanels 
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
	}

	/**
	 * Create Document_Verify that creating a document can be canceled for Contract.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19824_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Contracts" tab on top navigation bar.
		sugar().navbar.navToModule(sugar().contracts.moduleNamePlural);

		// Click a link of contract name in "Contract" list view.
		sugar().contracts.listView.clickRecord(1);

		// Click "Create" button in "documents" sub-panel.
		// TODO: VOOD-1948 Documents Subpanel failing on Contracts recordView
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#contracts_documents_create_button").click();
		VoodooUtils.waitForReady(20000);
		sugar().documents.editView.getEditField("documentName").assertVisible(true);
		VoodooUtils.focusDefault();
		// Cancel creating the document for Contract.
		sugar().documents.editView.cancel();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-972
		// Verify that canceled creating Document is not displayed on Contract detail view page. 
		new VoodooControl("tr", "css", "#list_subpanel_contracts_documents tr.oddListRowS1").assertContains("No data", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}