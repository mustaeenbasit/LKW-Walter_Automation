package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22964 extends SugarTest {
	StandardSubpanel contractsSubpanel;

	public void setup() throws Exception {
		AccountRecord account = (AccountRecord) sugar().accounts.api.create();
		sugar().contracts.api.create();
		sugar().login();
		
		// Contracts subpanel is enabled for displaying
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);

		// Existing contract record related to the account.
		account.navToRecord();
		contractsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contracts.moduleNamePlural);
		contractsSubpanel.scrollIntoViewIfNeeded(false);
		contractsSubpanel.addRecord();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.editView.getEditField("name").set(testName);
		VoodooUtils.focusDefault();
		sugar().contracts.editView.save();
		VoodooUtils.waitForReady();
	}

	/**
	 * Edit_Verify that contract record related to the account can be edited by clicking "edit" icon on "CONTRACTS" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22964_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Edit" in-line button on the right edge of a contract record on "CONTRACTS" sub-panel. 
		contractsSubpanel.scrollIntoViewIfNeeded(false);
		contractsSubpanel.expandSubpanel();
		FieldSet fs = new FieldSet();
		fs.put("name", sugar().contracts.getDefaultData().get("name"));
		contractsSubpanel.scrollIntoView();
		contractsSubpanel.editRecord(1);
		
		// TODO: VOOD-1894
		// Modify values in all available fields and save
		new VoodooControl("input", "css", "[data-voodoo-name='Contracts'] .fld_name.edit input").set(testName);
		contractsSubpanel.saveAction(1);

		// TODO: VOOD-1424
		// Verify that the modified contract information is displayed on "CONTRACTS" sub-panel.
		fs.clear();
		fs.put("name", testName);
		contractsSubpanel.verify(1, fs, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}