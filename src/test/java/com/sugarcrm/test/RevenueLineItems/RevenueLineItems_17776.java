package com.sugarcrm.test.RevenueLineItems;

import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */
public class RevenueLineItems_17776 extends SugarTest {
	public void setup() throws Exception {
		sugar().productCatalog.api.create();
		sugar().productCategories.api.create();
		sugar().login();

		// TODO: VOOD-444 relate category to product
		sugar().productCatalog.navToListView();
		sugar().productCatalog.listView.editRecord(1);
		sugar().productCatalog.listView.getEditField(1, "productCategory").set(sugar().productCategories.getDefaultData().get("name"));
		sugar().productCatalog.listView.saveRecord(1);
	}

	/**
	 * Verify that corresponding fields are auto populated when select product in the RLI edit mode
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17776_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
		Double costPrice = Double.parseDouble(sugar().productCatalog.getDefaultData().get("costPrice"));
		Double unitPrice = Double.parseDouble(sugar().productCatalog.getDefaultData().get("unitPrice"));
		Double listPrice = Double.parseDouble(sugar().productCatalog.getDefaultData().get("listPrice"));

		// Navigate to RLI module and open the created RLI
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.showMore();
		sugar().revLineItems.createDrawer.getEditField("product").set(sugar().productCatalog.getDefaultData().get("name"));

		// Verify the product data is correctly auto populated
		sugar().revLineItems.createDrawer.getEditField("category").assertContains(sugar().productCategories.getDefaultData().get("name"), true);
		sugar().revLineItems.createDrawer.getEditField("unitPrice").assertContains(currencyFormat.format(unitPrice), true);
		sugar().revLineItems.createDrawer.getEditField("calcRLIAmount").assertContains(currencyFormat.format(unitPrice), true);
		
		// TODO: VOOD-1349 
		new VoodooControl("span", "css", "span.fld_list_price.nodata").assertContains(currencyFormat.format(listPrice), true);
		new VoodooControl("span", "css", "span.fld_cost_price.nodata").assertContains(currencyFormat.format(costPrice), true);
		sugar().revLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}