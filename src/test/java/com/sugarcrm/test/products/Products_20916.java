package com.sugarcrm.test.products;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Products_20916 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();			
	}

	/**
	 * Verify that product category can be created with required fields entered.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20916_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);	

		// create product category
		for(int i=0;i<ds.size();i++) {
			sugar.productCategories.create(ds.get(i));
			
			//check record in list view
			sugar.productCategories.listView.verifyField(1, "name", ds.get(i).get("name"));
		}
					
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}