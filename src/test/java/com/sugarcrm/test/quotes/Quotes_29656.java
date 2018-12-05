package com.sugarcrm.test.quotes;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Quotes_29656 extends SugarTest {
	Record linkedQuote;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().opportunities.api.create();
		sugar().documents.api.create();
		sugar().login();

		// Create a Quote related to Billing Account Name
		FieldSet fs = new FieldSet();
		fs.put("billingAccountName", sugar().accounts.getDefaultData().get("name"));
		linkedQuote = (QuoteRecord)sugar().quotes.create(fs);
	}

	/**
	 * Verify that quotes subpanel is present on related modules record views OOTB
	 * @throws Exception
	 */
	@Test
	public void Quotes_29656_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Linking Quote Record in Accounts
		StandardSubpanel quotesAccSubpanel = sugar().accounts.recordView.subpanels.get(sugar().quotes.moduleNamePlural);
		String defaultName = sugar().quotes.getDefaultData().get("name");

		// Verify Quotes Subpanel is visible under Account Record and have correct info
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		quotesAccSubpanel.expandSubpanel();

		// TODO: VOOD-1846
		VoodooControl quoteName = new VoodooControl("a", "css", ".layout_Quotes .fld_name a");
		quoteName.assertEquals(defaultName, true);

		// Define Controls for linking quote Record
		// TODO: VOOD-1000
		VoodooControl  quotesSubpanel = new VoodooControl("a", "css", ".layout_Quotes");
		VoodooControl linkDropDown = new VoodooControl("a", "css", ".layout_Quotes .btn.dropdown-toggle");
		VoodooControl linkExistingRecord = new VoodooControl("a", "css", ".dropdown-menu .panel-top.fld_select_button .rowaction");
		VoodooControl selectRecord = new VoodooControl("input", "css", ".single .list input");
		VoodooControl linkRecord = new VoodooControl("a", "css", "[name='link_button']");

		// Link Quote Record in Quote Subpanel of Contact Record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		quotesSubpanel.scrollIntoViewIfNeeded(false);
		linkDropDown.click();
		linkExistingRecord.click();
		selectRecord.click();
		linkRecord.click();
		VoodooUtils.waitForReady();

		// Verify Quotes Subpanel is visible under Contact Record and have correct info
		quoteName.assertEquals(defaultName, true);

		// Link Quote Record in Quote Subpanel of Opportunity Record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		quotesSubpanel.scrollIntoViewIfNeeded(false);
		linkDropDown.click();
		linkExistingRecord.click();
		selectRecord.click();
		linkRecord.click();
		VoodooUtils.waitForReady();

		// Verify Quotes Subpanel is visible under Opportunity Record and have correct info
		quoteName.assertEquals(defaultName, true);

		// Verify Quotes Subpanel is visible
		sugar().documents.navToListView();
		sugar().documents.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl quoteDocSubpanel= new VoodooControl("div", "id", "list_subpanel_quotes");
		quoteDocSubpanel.scrollIntoViewIfNeeded(false);
		quoteDocSubpanel.assertVisible(true);

		// Linking Quote record with Documents 
		// TODO: VOOD-1713
		new VoodooControl("a", "id", "documents_quotes_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);

		// Verify the info is correct in Quotes Subpanel
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#list_subpanel_quotes tr:nth-child(3) td a").assertEquals(defaultName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}