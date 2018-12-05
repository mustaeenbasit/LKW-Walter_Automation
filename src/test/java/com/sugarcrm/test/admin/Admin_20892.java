package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20892 extends SugarTest {
	DataSource prodCatalogDS, massUpdateDS;

	public void setup() throws Exception {
		prodCatalogDS = testData.get(testName);
		massUpdateDS = testData.get(testName + "_1");
		sugar().productCatalog.api.create(prodCatalogDS);
		sugar().login();
	}

	/**
	 * Test Case 20892: Product Catalog - Mass Update_Verify that product catalog can be edited by "Mass Update"
	 *
	 * @throws Exception
	 */
	@Test
	public void Admin_20892_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Product Catalog page
		sugar().productCatalog.navToListView();
		// select record in list view
		sugar().productCatalog.listView.toggleSelectAll();
		// perform mass update to update "Availability" field value 
		sugar().productCatalog.massUpdate.performMassUpdate(massUpdateDS.get(0));
		VoodooUtils.refresh();
		// Verify that selected records are correctly modified by Mass Update 
		sugar().productCatalog.listView.verifyField(1, "status", massUpdateDS.get(0).get("Availability"));
		sugar().productCatalog.listView.verifyField(2, "status", massUpdateDS.get(0).get("Availability"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}