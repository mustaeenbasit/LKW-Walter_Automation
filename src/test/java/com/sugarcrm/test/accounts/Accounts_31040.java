package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_31040 extends SugarTest {
	DataSource quotesData = new DataSource();
	
	public void setup() throws Exception {
		quotesData = testData.get(testName);
		sugar().accounts.api.create();

		// Creating 7 quotes in the quotes module
		sugar().quotes.api.create(quotesData);
		sugar().login();

		// Link all created quotes to account by mass update
		sugar().quotes.navToListView();
		sugar().quotes.listView.toggleSelectAll();

		// Clicking the mass update option from the Delete dropdown
		// TODO: VOOD-1723 - Need to add lib support for 'Mass Update' action in BWC modules
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", ".sugar_action_button .ab").click();
		new VoodooControl("a", "css", ".subnav.ddopen #massupdate_listview_top").click();
		new VoodooControl("button", "css", "#mass_update_table tr:nth-child(3) td:nth-child(4) .button").click();
		VoodooUtils.waitForReady();

		// Select first account from 'Shipping Account Name'
		VoodooUtils.focusWindow(1);
		VoodooControl firstRecord = new VoodooControl("a", "css", ".oddListRowS1 td a");
		firstRecord.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "css", "#mass_update_table tr:nth-child(4) td:nth-child(5) .button").click();
		VoodooUtils.focusWindow(1);

		// Select first account from 'Billing Account Name'
		firstRecord.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Click the Update button 
		new VoodooControl("input", "id", "update_button").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that quotes subpanels do not display duplicate records
	 */
	@Test
	public void Accounts_31040_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Going to the subpannel Quotes(Bill-To) of the account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel quotesBillToSubpanel = sugar().accounts.recordView.subpanels.get(sugar().quotes.moduleNamePlural);

		// Expanding the quotes subpannel and click the more quotes link
		quotesBillToSubpanel.expandSubpanel();
		quotesBillToSubpanel.showMore();

		// Click on the subject header of the quotes(Bill-to) subpannel to sort in desc order and then click the more quotes link again
		// TODO: VOOD-2105 - Need library support of column headers for Quotes (Bill To) & Quotes (Ship To) subpanels
		new VoodooControl("th", "css", "[data-subpanel-link='quotes'] .sorting.orderByname").click();
		quotesBillToSubpanel.showMore();
		VoodooUtils.waitForReady();
		quotesBillToSubpanel.scrollIntoView();

		// Verifying 7 quotes record are appearing in 'Quotes (Bill-To) subpanel
		Assert.assertTrue(quotesBillToSubpanel.countRows() == quotesData.size());

		// Verify all quotes that we have created should be available there without any duplicate quote
		// TODO: VOOD-1846 - ClassCastException when verifying Contracts subpanel fields in Sidecar
		for (int i = 0; i < quotesData.size(); i++) {
			new VoodooControl("tr", "css", "[data-subpanel-link='quotes'] tr:nth-child(" + (quotesData.size() - i) + ").single").assertContains(quotesData.get(i).get("name"), true);
		}

		// Go to the Quotes(Ship-To) sub-pannel of the account record and expand the subpanel
		StandardSubpanel quotesShipToSubpanel = sugar().accounts.recordView.subpanels.get("Quotes (Ship To)");

		// Expanding the quotes subpannel and click the more quotes link
		quotesShipToSubpanel.expandSubpanel();
		quotesShipToSubpanel.showMore();

		// Click on the subject header of the quotes(Ship-To) subpannel to sort in desc order and then click the more quotes link again
		// TODO: VOOD-2105 - Need library support of column headers for Quotes (Bill To) & Quotes (Ship To) subpanels
		new VoodooControl("th", "css", "[data-subpanel-link='quotes_shipto'] .sorting.orderByname").click();
		quotesShipToSubpanel.showMore();
		VoodooUtils.waitForReady();
		quotesShipToSubpanel.scrollIntoViewIfNeeded(false);

		// Verifying 7 quotes record are appearing in 'Quotes (Ship-to) subpanel
		// TODO: VOOD-2106 - Quotes (Ship To) Subpanel countRows() method is also counting Quotes (Bill To) records
		Assert.assertTrue(new VoodooControl("table", "css", "[data-subpanel-link='quotes_shipto'] .single").count()== quotesData.size());

		// Verify all quotes that we have created should be available there without any duplicate quote
		// TODO: VOOD-1846 - ClassCastException when verifying Contracts subpanel fields in Sidecar.
		for (int i = 0; i < quotesData.size(); i++) {
			new VoodooControl("div", "css", "[data-subpanel-link='quotes_shipto'] tr:nth-child(" + (quotesData.size() - i) + ").single").assertContains(quotesData.get(i).get("name"), true);
		}
	}

	public void cleanup() throws Exception {}
}
