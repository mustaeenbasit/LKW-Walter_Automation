package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20893 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.productCatalog.api.create(ds);
		sugar.login();	
	}

	/**
	 * Verify that mass product catalogs can be deleted in list view
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20893_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.toggleSelectAll();
		sugar.productCatalog.listView.openActionDropdown();
		sugar.productCatalog.listView.delete();
		sugar.alerts.confirmAllAlerts();
		// Verify that the selected records are deleted from product catalog list view.
		sugar.productCatalog.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}