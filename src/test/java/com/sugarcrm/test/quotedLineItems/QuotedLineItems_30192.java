package com.sugarcrm.test.quotedLineItems;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;


public class QuotedLineItems_30192 extends SugarTest {
	FieldSet fs = new FieldSet();

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();

		// Enable the 'Quoted Line Items' subpanel from the 'Admin > Display Modules and Subpanels'.
		sugar().admin.enableSubpanelDisplayViaJs(sugar().quotedLineItems);

		// build a fieldset for currency record
		FieldSet currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", fs.get("conversionRate"));
		currencyData.put("currencySymbol", fs.get("currencySymbol"));
		currencyData.put("ISOcode", fs.get("ISOcode"));
		currencyData.put("status", fs.get("status"));
		// Create new EUR currency
		sugar().admin.setCurrency(currencyData);
	}

	/**
	 * Verify that Quoted Line Item subpanel shows Correct value for 'price' column
	 *
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_30192_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// create a new Product with the EUR currency.
		sugar().productCatalog.navToListView();
		sugar.productCatalog.listView.create();
		sugar.productCatalog.createDrawer.getEditField("name").set(fs.get("name"));
		// TODO: VOOD-983
		VoodooControl priceCtrl = new VoodooControl("div", "css", ".select2-results :nth-child(2) div");
		new VoodooControl("span", "css", "span[data-voodoo-name='cost_price'] span[data-voodoo-name='currency_id']").click();
		priceCtrl.click();
		// Set Cost/Unit Price/List Price: 100 EUR
		sugar.productCatalog.createDrawer.getEditField("costPrice").set(fs.get("cost"));
		sugar.productCatalog.createDrawer.getEditField("unitPrice").set(fs.get("unitPrice"));
		sugar.productCatalog.createDrawer.getEditField("listPrice").set(fs.get("listPrice"));
		sugar.productCatalog.createDrawer.save();

		// Open an existing opportunity record and create a new QLI by using the already created product.
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel qliSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().quotedLineItems.moduleNamePlural);
		FieldSet qliFs = new FieldSet();
		qliFs.put("productTemplate", fs.get("name"));
		qliSubPanel.create(qliFs);

		// Observe the value of the 'price' column in the QLI subpanel of opp record. the 'price' field should shows the value of E100 $111.11
		// TODO: VOOD-609
		new VoodooControl("span", "css", "[data-voodoo-name='discount_price']").assertContains(fs.get("discount_price1"), true);
		new VoodooControl("span", "css", "[data-voodoo-name='discount_price']").assertContains(fs.get("discount_price2"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}