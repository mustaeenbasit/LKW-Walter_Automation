package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20870 extends SugarTest {
	DataSource ds;
	public void setup() throws Exception {
		ds = testData.get(testName);	
		sugar.productCatalog.api.create(ds);
		sugar.login();			
	}

	/**
	 * Verify that product catalogs are displayed in list view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20870_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.verifyField(1, "name", ds.get(0).get("name"));
		sugar.productCatalog.listView.verifyField(1, "stockQuantity", ds.get(0).get("stockQuantity"));
		sugar.productCatalog.listView.verifyField(1, "costPrice", ds.get(0).get("costPrice"));
		sugar.productCatalog.listView.getDetailField(1, "unitPrice").scrollIntoViewIfNeeded(false);
		sugar.productCatalog.listView.verifyField(1, "unitPrice", ds.get(0).get("unitPrice"));
		sugar.productCatalog.listView.verifyField(1, "listPrice", ds.get(0).get("listPrice"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}