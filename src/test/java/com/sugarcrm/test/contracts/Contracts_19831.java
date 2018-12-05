package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19831 extends SugarTest {

	public void setup() throws Exception {
		sugar.contracts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);

		// Go to contracts module and create a note record to this Contract
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
	}

	/**
	 * Edit Note_Verify that editing a note can be canceled for Contract.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19831_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Click the Note title in the Note subpanel.
		VoodooControl noteCreateCtrl = new VoodooControl("a", "css", "#whole_subpanel_history tr:nth-child(3) td:nth-child(2) a");
		noteCreateCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that User should be navigated to the Note detail page.
		VoodooControl subjectCtrl = sugar.notes.recordView.getDetailField("subject");
		subjectCtrl.assertContains(testName, true);

		// Click on edit button
		sugar.notes.recordView.edit();

		// Do not edit the the Note, cancel edit recordView
		sugar.notes.recordView.cancel();

		// Go back to the Contract recordView
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Click a link of contract name in "Contract" list view.
		noteCreateCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that The Note title should be same as before.
		subjectCtrl.assertContains(testName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}