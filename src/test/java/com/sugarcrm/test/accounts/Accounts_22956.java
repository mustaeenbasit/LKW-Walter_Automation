package com.sugarcrm.test.accounts;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

import org.junit.Assert;
import org.junit.Test;

public class Accounts_22956 extends SugarTest {
	AccountRecord account;

	public void setup() throws Exception {
		account = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
	}

	/**
	 *	Verify that creating new contract related to the account is cancelled on "Contracts" sub-panel.
	 *	@throws Exception
	 */
	@Test
	public void Accounts_22956_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		account.navToRecord();
		StandardSubpanel contractsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contracts.moduleNamePlural);
		contractsSubpanel.scrollIntoViewIfNeeded(false);
		contractsSubpanel.addRecord();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.editView.getEditField("name").set(testName);
		VoodooUtils.focusDefault();
		sugar().contracts.editView.cancel();
		VoodooUtils.waitForReady();

		// Verify that record is not created
		contractsSubpanel.scrollIntoViewIfNeeded(false);
		Assert.assertTrue("The subpanel is not empty", contractsSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}