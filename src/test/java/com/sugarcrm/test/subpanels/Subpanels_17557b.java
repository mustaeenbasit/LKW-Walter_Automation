package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17557b extends SugarTest {
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * 17557b: Verify user can create related records (for an account) from subpanels --b. Quotes
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17557b_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts Record view
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// Click "+" in quotes sub-panel to create new quote
		StandardSubpanel quotesSubpanel=
				(StandardSubpanel) sugar.accounts.recordView.subpanels.get(sugar.quotes.moduleNamePlural);
		quotesSubpanel.addRecord();

		// Verify account fields are populated by default on quotes create page
		// TODO:VOOD-930
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "billing_account_name")
			.assertEquals(sugar.accounts.getDefaultData().get("name"), true);
		new VoodooControl("input", "id", "shipping_account_name")
			.assertEquals(sugar.accounts.getDefaultData().get("name"), true);

		// Complete all required information for quotes
		sugar.quotes.editView.getEditField("name")
			.set(sugar.quotes.getDefaultData().get("name"));
		sugar.quotes.editView.getEditField("date_quote_expected_closed")
			.set(sugar.quotes.getDefaultData().get("date_quote_expected_closed"));
		
		//Save quote
		VoodooUtils.focusDefault(); 
		sugar.quotes.editView.save();

		// Verify that created quotes record is displayed in quotes sub-panel
		// TODO: VOOD-1480 scroll into view show be 
		quotesSubpanel.scrollIntoViewIfNeeded(false);
		quotesSubpanel.expandSubpanel();
		
		// Verify that created quote is displayed in "Quotes" sub-panel
		// TODO: VOOD-1000
		new VoodooControl("a", "css", ".layout_Quotes .list.fld_name")
			.assertContains(sugar.quotes.getDefaultData().get("name"), true);
		new VoodooControl("div", "css", ".layout_Quotes .list.fld_date_quote_expected_closed")
			.assertContains(sugar.quotes.getDefaultData().get("date_quote_expected_closed"), true);
			
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}