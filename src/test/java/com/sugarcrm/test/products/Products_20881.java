package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20881 extends SugarTest {
	DataSource ds;
	public void setup() throws Exception {
		ds = testData.get(testName);	
		sugar.productCatalog.api.create(ds.get(0));
		sugar.login();	
	}

	/**
	 * Verify that editing product catalog can be canceled.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20881_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.clickRecord(1);

		for(int i=1;i<ds.size();i++) {
			sugar.productCatalog.recordView.edit();
			sugar.productCatalog.recordView.getEditField("name").set(ds.get(i).get("name"));
			sugar.productCatalog.recordView.getEditField("stockQuantity").set(ds.get(i).get("stockQuantity"));
			sugar.productCatalog.recordView.getEditField("costPrice").set(ds.get(i).get("costPrice"));
			sugar.productCatalog.recordView.getEditField("unitPrice").set(ds.get(i).get("unitPrice"));
			sugar.productCatalog.recordView.getEditField("listPrice").set(ds.get(i).get("listPrice"));
			sugar.productCatalog.recordView.cancel();
			// check data on record view
			sugar.productCatalog.recordView.getDetailField("name").assertEquals(ds.get(0).get("name"), true);
			sugar.productCatalog.recordView.getDetailField("stockQuantity").assertEquals(ds.get(0).get("stockQuantity"), true);
			sugar.productCatalog.recordView.getDetailField("costPrice").assertContains(ds.get(0).get("costPrice"), true);
			sugar.productCatalog.recordView.getDetailField("unitPrice").assertContains(ds.get(0).get("unitPrice"), true);
			sugar.productCatalog.recordView.getDetailField("listPrice").assertContains(ds.get(0).get("listPrice"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}