package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_29487 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		FieldSet currencyData = testData.get(testName+"_currency").get(0);
		sugar().login();

		// Create a new Currency
		sugar().admin.setCurrency(currencyData);

		// Enable QLI module
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * Verify that cancel QLI record works properly when currency of the record is changed before canceling
	 * 
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_29487_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to QLI module and create new QLI record with unit price $500.00 and discount $100.00.
		FieldSet customFS = testData.get(testName).get(0);
		sugar().quotedLineItems.create(customFS);

		// Open the created record in record view
		sugar().quotedLineItems.listView.clickRecord(1);
		VoodooUtils.waitForReady();

		// TODO: VOOD-983
		// Click on unit price field so it switches to edit mode
		// sugar().quotedLineItems.recordView.getDetailField("unitPrice").click();
		new VoodooControl("span", "css", "[data-name='discount_price'] .record-edit-link-wrapper").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-983
		// Change currency from USD to EUR
		new VoodooControl("span", "css", ".currency.edit.fld_currency_id").click();
		VoodooControl currencySelect = new VoodooControl("li", "css", "#select2-drop > ul > li:nth-child(2)");
		currencySelect.waitForVisible();
		currencySelect.click();

		// Cancel editing
		sugar().quotedLineItems.recordView.cancel();

		// Verify that the Unit price: $500.00.  Discount: $100.00
		sugar().quotedLineItems.recordView.getDetailField("unitPrice").assertContains(customFS.get("unitPrice"), true);
		sugar().quotedLineItems.recordView.getDetailField("discountAmount").assertContains(customFS.get("discountAmount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}