package com.sugarcrm.test.products;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Products_20922 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		sugar.login();	
		ds= testData.get(testName);

		// create product category
		sugar.productCategories.api.create(ds);
	}

	/**
	 * Verify that deleting product category can be canceled.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20922_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCategories.navToListView();
		for(int i = 1; i <= ds.size(); i++) {
			sugar.productCategories.listView.checkRecord(i);
		}
		sugar.productCategories.listView.openActionDropdown();
		sugar.productCategories.listView.delete();
		sugar.alerts.cancelAllAlerts();

		// check record in list view
		for(int i = ds.size(); i < 1; i--) {
			sugar.productCategories.listView.verifyField(i, "name", ds.get(i).get("name"));
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}