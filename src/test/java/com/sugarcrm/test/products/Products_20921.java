package com.sugarcrm.test.products;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Products_20921 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		sugar.login();			
		ds= testData.get(testName);

		// create product category
		sugar.productCategories.api.create(ds);
	}

	/**
	 * Verify that product category can be deleted from list view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20921_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCategories.navToListView();
		for(int i = 1; i <= ds.size(); i++) {
			sugar.productCategories.listView.checkRecord(i);
		}
		sugar.productCategories.listView.openActionDropdown();
		sugar.productCategories.listView.delete();
		sugar.alerts.confirmAllAlerts();
		
		// check record in list view
		sugar.productCategories.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}