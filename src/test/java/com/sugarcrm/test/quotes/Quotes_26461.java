package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_26461 extends SugarTest {
	FieldSet fs, currencyData;
	
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.quotes.api.create();
		fs = testData.get(testName).get(0);
		sugar.login();	

		// Create product catalog
		sugar.productCatalog.create();
		
		// Create test Currency
		currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", fs.get("rate"));
		currencyData.put("currencySymbol", fs.get("symbol"));
		currencyData.put("ISOcode", fs.get("code"));
		sugar.admin.setCurrency(currencyData);
	}

	/**
	 * Verify that discount percentage does not change when quote is edited.
	 * @throws Exception
	 */
	@Test
	public void Quotes_26461_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-865
		// Quote record link with product catalog (Add Group)
		sugar.quotes.navToListView();
		sugar.quotes.listView.clickRecord(1);
		sugar.quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "btn_billing_account_name").click();
		VoodooUtils.focusWindow(1);
		VoodooControl recordSelectCtrl = new VoodooControl("a", "css", "tr.oddListRowS1 td:nth-of-type(1) a");
		recordSelectCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("select", "id", "currency_id").set(String.format("%s : %s",testName, fs.get("symbol")));
		new VoodooControl("input", "id", "add_group").click();
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("button", "id", "product_name_select_1").click();
		VoodooUtils.focusWindow(1);
		recordSelectCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl discountCtrl = new VoodooControl("input", "id", "discount_amount_1");
		discountCtrl.set(fs.get("discount_amount"));
		VoodooUtils.focusDefault();
		sugar.quotes.editView.save();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Verify the discount percentage
		sugar.quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		discountCtrl.assertEquals(fs.get("discount_amount"), true);
		VoodooUtils.focusDefault();
		sugar.quotes.editView.cancel();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}