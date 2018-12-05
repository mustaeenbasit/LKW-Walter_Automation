package com.sugarcrm.test.products;

import java.text.DecimalFormat;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20935 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
		}

	/**
	 * Product Catalog -  Verify that price fields for a product catalog is displayed as entered in list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Products_20935_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to "Admin -> Product Catalog" view and create a product catalog item with all the prices entered
		sugar.productCatalog.create();
		
		DecimalFormat formatter = new DecimalFormat("##,###.00");
		String costPrice = String.format("%s%s", "$", formatter.format(Double.parseDouble(sugar.productCatalog.getDefaultData().get("costPrice"))));
		String unitPrice = String.format("%s%s", "$", formatter.format(Double.parseDouble(sugar.productCatalog.getDefaultData().get("unitPrice"))));
		String listPrice = String.format("%s%s", "$", formatter.format(Double.parseDouble(sugar.productCatalog.getDefaultData().get("listPrice"))));
		
		// Go to list view and verify that price fields are displayed as entered
		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.getDetailField(1, "costPrice").assertEquals(costPrice, true);
		sugar.productCatalog.listView.getDetailField(1, "unitPrice").assertEquals(unitPrice, true);
		sugar.productCatalog.listView.getDetailField(1, "listPrice").assertEquals(listPrice, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}