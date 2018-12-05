package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19828 extends SugarTest {

	public void setup() throws Exception {
		sugar.contracts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Create Note_Verify that Note can be created for Contract.
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
		// Click "Create Note or Attachment" button in "Notes" sub-panel.
		new VoodooControl("a", "css", "#whole_subpanel_history table tr.pagination td table tr td:nth-child(1) ul li form a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		new VoodooControl("input", "css", ".fld_name.edit input").set(testName);
		new VoodooControl("a", "css", ".create.fld_save_button a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that the created Note is displayed correctly on Contract detail view page.
		new VoodooControl("a", "css", "#whole_subpanel_history tr:nth-child(3) td:nth-child(2) a").assertEquals(testName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}