package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19844 extends SugarTest {
	DataSource quoteRecord = new DataSource();

	public void setup() throws Exception {
		quoteRecord = testData.get(testName);
		sugar().contracts.api.create();
		sugar().quotes.api.create(quoteRecord);
		sugar().login();
		// enabled  Contract module in Admin->Display Modules and Subpanels 
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
	}

	/**
	 * Select Quote_Verify that Quote can be searched for Contract.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19844_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Contracts" tab on top navigation bar.
		sugar().navbar.navToModule(sugar().contracts.moduleNamePlural);

		// Click a link of contract name in "Contract" list view.
		sugar().contracts.listView.clickRecord(1);

		// Click "Select" button in "Quotes" sub-panel.
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1193
		new VoodooControl("a", "id", "contracts_quotes_select_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		// Verify that all the quotes records are shown in pop-up window.
		for (int i = quoteRecord.size(); i <= 1; i--) {
			new VoodooControl("a", "css", ".list.view tr:nth-child("+(6-i)+") > td:nth-child(4) > a").assertContains(quoteRecord.get(i-1).get("name"), true);
		}

		new VoodooControl("input", "id", "name_advanced").set(quoteRecord.get(0).get("name"));
		new VoodooControl("input", "id", "search_form_submit").click();
		// Verify that the searched record is listed in "Quote" list view.
		new VoodooControl("a", "css", ".list.view tr:nth-child(3) > td:nth-child(4) > a").assertContains(quoteRecord.get(0).get("name"), true);
		VoodooUtils.focusDefault();
		VoodooUtils.closeWindow();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}