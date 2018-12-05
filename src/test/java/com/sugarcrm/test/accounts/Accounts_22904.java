package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22904 extends SugarTest {
	StandardSubpanel contractSubpanelCtrl;

	public void setup() throws Exception {
		// Create an Account record and a Contract record
		sugar().accounts.api.create();

		// Login as a valid user
		sugar().login();

		// Enable the Contract module and subpanel as well
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);

		// Link the Contract to the Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		contractSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().contracts.moduleNamePlural);
		contractSubpanelCtrl.scrollIntoViewIfNeeded(false);
		contractSubpanelCtrl.addRecord();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.editView.getEditField("name").set(testName);
		VoodooUtils.focusDefault();
		sugar().contracts.editView.save();
	}

	/**
	 * Account Detail - Contracts sub-panel_Verify that contracts related to this account is displayed in contracts detail view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22904_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigates to the record view of the created Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click a contract name link on 'CONTRACTS' sub-panel
		contractSubpanelCtrl.scrollIntoViewIfNeeded(false);
		contractSubpanelCtrl.expandSubpanel();
		contractSubpanelCtrl.scrollIntoView(); // Need to scroll twice as the last record of the last subpanel
		contractSubpanelCtrl.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Contracts related to this account is displayed in contracts Record view
		sugar().contracts.detailView.getDetailField("name").assertEquals(testName, true);
		sugar().contracts.detailView.getDetailField("account_name").assertEquals(sugar().accounts.getDefaultData().get("name"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}