package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_29040 extends SugarTest {
	public void setup() throws Exception {
		sugar.contracts.api.create();
		sugar.quotes.api.create();
		sugar.login();
		// enabled  Contract module in Admin->Display Modules and Subpanels 
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Verify that Valid Until value should be displayed in related column in Quotes sub panel of Contracts record
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_29040_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Contracts" tab on top navigation bar.
		sugar.navbar.navToModule(sugar.contracts.moduleNamePlural);
		
		// Click a link of contract name in "Contract" list view.
		sugar.contracts.listView.clickRecord(1);

		// Click "Select" button in "Quotes" sub-panel.
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1193
		new VoodooControl("a", "id", "contracts_quotes_select_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		// Click a link of a record name in "Quotes" list view of popup window.
		new VoodooControl("input", "id", "name_advanced").set(sugar.quotes.getDefaultData().get("name"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("input", "css", ".checkbox.massall").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		// Verify that valid Until value should be displayed on "Quotes" sub-panel of Contract detail view page.
		new VoodooControl("span", "css", "#list_subpanel_quotes .list.view [sugar='slot4b']").assertContains(sugar.quotes.getDefaultData().get("date_quote_expected_closed"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}