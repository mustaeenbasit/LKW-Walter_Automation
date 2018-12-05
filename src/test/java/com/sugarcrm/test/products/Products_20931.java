package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20931 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();			
	}

	/**
	 * Verify that product type can be created with required fields entered.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20931_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource productTypesData = testData.get(testName);	

		// Go to "Admin -> Product Types" view -> Click "Create" button -> Enter all required fields -> Click "Save" button
		sugar.productTypes.create(productTypesData);

		// Verify that the newly created record is displayed in product type list view
		sugar.productTypes.navToListView();
		for(int i = 0; i < productTypesData.size(); i++) {
			sugar.productTypes.listView.verifyField(i+1, "name", productTypesData.get(i).get("name"));
			sugar.productTypes.listView.verifyField(i+1, "order", productTypesData.get(i).get("order"));
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}