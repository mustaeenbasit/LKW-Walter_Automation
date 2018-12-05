package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.DocumentRecord;
import com.sugarcrm.test.SugarTest;


public class Contracts_19823 extends SugarTest {
	DocumentRecord myDocumentRecord;

	public void setup() throws Exception {
		sugar.contracts.api.create();
		myDocumentRecord = (DocumentRecord) sugar.documents.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Create Document_Verify that Document can be created for Contract.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19823_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Go to contracts module
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1713
		new VoodooControl("span", "css", "#list_subpanel_contracts_documents table tr.pagination td table tr td:nth-child(1) ul li span").click();
		new VoodooControl("a", "id", "contracts_documents_select_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "[name='document_name']").set(myDocumentRecord.getRecordIdentifier());
		new VoodooControl("input", "id", "search_form_submit").click();

		// Select Document record
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-child(1) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the created Document is displayed correctly on Contract detail view page and the count for added documents is shown correctly in list of sub-panel.
		new VoodooControl("a", "css", "#list_subpanel_contracts_documents tr:nth-child(3) td:nth-child(2) a").assertEquals(myDocumentRecord.getRecordIdentifier(), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}