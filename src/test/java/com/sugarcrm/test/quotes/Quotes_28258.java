package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.test.SugarTest;

public class Quotes_28258 extends SugarTest {
	AccountRecord myAccount;
	FieldSet myData = new FieldSet();

	public void setup() throws Exception {
		myData = testData.get(testName).get(0);
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();

		// Enable QLI
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);

		// Create custom Currency
		FieldSet currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", myData.get("rate"));
		currencyData.put("currencySymbol", myData.get("symbol"));
		sugar().admin.setCurrency(currencyData);
	}

	/**
	 * Verify that quote's subtotal is not changed when re-saving the Quote line items when using Nun-US
	 * currency in Quote
	 *
	 * @throws Exception
	 */
	@Test
	public void Quotes_28258_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Quote
		FieldSet quoteData = sugar().quotes.defaultData;
		quoteData.put("billingAccountName", myAccount.getRecordIdentifier());
		QuoteRecord myQuote = (QuoteRecord)sugar().quotes.create(quoteData);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Select currency EURO, Add Group and Product
		// TODO: VOOD-865 Need Lib support for add group, add row and product catalog selection
		new VoodooControl("select", "id", "currency_id").set(testName + " : " + myData.get("symbol"));
		new VoodooControl("input", "css", "input#add_group").click();
		new VoodooControl("input", "css", "input#bundle_name_group_0").set(myData.get("groupName"));
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("input", "css", "input#name_1").set(myData.get("productName"));
		new VoodooControl("input", "css", "input#discount_price_1").set(myData.get("unitPrice"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();

		// QLI module, edit and save without any changes
		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.clickRecord(1);
		sugar().quotedLineItems.recordView.edit();
		sugar().quotedLineItems.recordView.save();

		// Navigates to Quotes Module and verify that quote's subtotal is not changed
		// TODO: VOOD-1064
		myQuote.navToRecord();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", ".detail.view tr:nth-child(6) td:nth-child(6)").assertContains(myData.get("symbol")+myData.get("unitPrice"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}