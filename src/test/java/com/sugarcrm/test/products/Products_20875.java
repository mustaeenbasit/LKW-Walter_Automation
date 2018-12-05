package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Products_20875 extends SugarTest {
	int quantity;

	public void setup() throws Exception {
		quantity = Integer.parseInt(sugar.productCatalog.getDefaultData().get("stockQuantity")) * 2; // double of default quantity stock

		// 2 catalogs via API with default data and with custom data
		sugar.productCatalog.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		fs.put("stockQuantity", Integer.toString(quantity));
		sugar.productCatalog.api.create(fs);

		sugar.login();			
	}
	/**
	 * Verify that product catalog can be sorted by clicking name, quantity stock column title in list view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20875_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCatalog.navToListView();

		// Verify records by 'name' column in descending order
		sugar.productCatalog.listView.sortBy("headerName", false);
		sugar.productCatalog.listView.verifyField(1, "name", testName);
		sugar.productCatalog.listView.verifyField(2, "name", sugar.productCatalog.getDefaultData().get("name"));

		// Verify records by 'name' column in ascending order
		sugar.productCatalog.listView.sortBy("headerName", true);
		sugar.productCatalog.listView.verifyField(1, "name", sugar.productCatalog.getDefaultData().get("name"));
		sugar.productCatalog.listView.verifyField(2, "name", testName);

		// Verify records by 'stock Quantity' column in descending order
		sugar.productCatalog.listView.sortBy("headerQtyinstock", false);
		sugar.productCatalog.listView.verifyField(1, "stockQuantity", Integer.toString(quantity));
		sugar.productCatalog.listView.verifyField(2, "stockQuantity", sugar.productCatalog.getDefaultData().get("stockQuantity"));

		// Verify records by 'stock Quantity' column in ascending order
		sugar.productCatalog.listView.sortBy("headerQtyinstock", true);
		sugar.productCatalog.listView.verifyField(1, "stockQuantity", sugar.productCatalog.getDefaultData().get("stockQuantity"));
		sugar.productCatalog.listView.verifyField(2, "stockQuantity", Integer.toString(quantity));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}