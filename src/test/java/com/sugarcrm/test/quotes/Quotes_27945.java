package com.sugarcrm.test.quotes;

import java.text.DecimalFormat;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_27945 extends SugarTest {
	FieldSet fs = new FieldSet();
	FieldSet currencyData = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		fs = testData.get(testName).get(0);

		// Create product catalog
		sugar().productCatalog.api.create();
		sugar().login();

		// Create custom Currency
		currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", fs.get("rate"));
		currencyData.put("currencySymbol", fs.get("symbol"));
		currencyData.put("ISOcode", fs.get("code"));
		sugar().admin.setCurrency(currencyData);
	}

	/**
	 * Verify that Quote is not updated after it was created
	 * @throws Exception
	 */
	@Test
	public void Quotes_27945_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-865
		// Create a new quote, choose custom currency, add one product and save
		sugar().navbar.clickModuleDropdown(sugar().quotes);
		sugar().navbar.selectMenuItem(sugar().quotes, "createQuote");
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("name").set(sugar().quotes.getDefaultData().get("name"));
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(sugar().quotes.getDefaultData().get("date_quote_expected_closed"));
		sugar().quotes.editView.getEditField("quoteStage").set(fs.get("quoteStage"));
		new VoodooControl("button", "id", "btn_billing_account_name").click();
		VoodooUtils.focusWindow(1);
		VoodooControl recordSelectCtrl = new VoodooControl("a", "css", "tr.oddListRowS1 td:nth-of-type(1) a");
		recordSelectCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Choose the currency that was created and add one product
		new VoodooControl("select", "id", "currency_id").set(String.format("%s : %s",testName, fs.get("symbol")));
		new VoodooControl("input", "id", "add_group").click();
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("button", "id", "product_name_select_1").click();
		VoodooUtils.focusWindow(1);
		recordSelectCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		VoodooUtils.waitForReady();
		double amount = Double.parseDouble(fs.get("rate")) * Double.parseDouble(sugar().productCatalog.getDefaultData().get("unitPrice"));
		DecimalFormat formatter = new DecimalFormat("##,###.00");
		String totalAmount = String.format("%s%s", fs.get("symbol"),formatter.format(amount));
		VoodooUtils.focusDefault();

		// Goto the currency module and update exchange rate to 0.80
		FieldSet currencyNewData1 = new FieldSet();
		currencyNewData1.put("conversionRate", fs.get("changedRate1"));
		sugar().admin.editCurrency(testName, currencyNewData1);

		// Edit Quote by changing the quote stage to "Closed Lost"
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("quoteStage").set(fs.get("quoteStage1"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		VoodooUtils.waitForReady();

		// Verify that 'Quote Total' and 'Grand Total' amounts are identical and not effected
		// TODO: VOOD-865
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl quoteTotal = new VoodooControl("td", "css", ".detail.view tr:nth-child(9) td:nth-child(4)");
		VoodooControl grandTotal = new VoodooControl("td", "css", ".detail.view tr:nth-child(15) td:nth-child(3)");
		quoteTotal.assertContains(totalAmount, true);
		grandTotal.assertContains(totalAmount, true);
		VoodooUtils.focusDefault();

		// Again update exchange rate in the currency module (e.g. 0.85)
		FieldSet currencyNewData2 = new FieldSet();
		currencyNewData2.put("conversionRate", fs.get("changedRate2"));
		sugar().admin.editCurrency(testName, currencyNewData2);

		// Edit quote by changing the quote stage to "Draft"
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("quoteStage").set(fs.get("quoteStage2"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		VoodooUtils.waitForReady();

		// Verify that 'Quote Total' and 'Grand Total' amounts are identical and not updated
		VoodooUtils.focusFrame("bwc-frame");
		quoteTotal.assertContains(totalAmount, true);
		grandTotal.assertContains(totalAmount, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}