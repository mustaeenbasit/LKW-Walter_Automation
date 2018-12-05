package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20883 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.productCatalog.api.create(ds.get(0));
		sugar.login();	
	}
	/**
	 * Verify that product catalog can be duplicated in detail view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20883_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.clickRecord(1);

		sugar.productCatalog.recordView.copy();
		// check data is copied over
		sugar.productCatalog.createDrawer.getEditField("name").assertEquals(ds.get(0).get("name"), true);
		sugar.productCatalog.createDrawer.getEditField("stockQuantity").assertEquals(ds.get(0).get("stockQuantity"),true);
		sugar.productCatalog.createDrawer.getEditField("webSite").assertEquals(ds.get(0).get("webSite"),true);
		sugar.productCatalog.createDrawer.getEditField("costPrice").assertContains(ds.get(0).get("costPrice"),true);
		sugar.productCatalog.createDrawer.getEditField("unitPrice").assertContains(ds.get(0).get("unitPrice"),true);
		sugar.productCatalog.createDrawer.getEditField("listPrice").assertContains(ds.get(0).get("listPrice"),true);

		// set data for the new record and cancel create
		sugar.productCatalog.createDrawer.getEditField("name").set(ds.get(1).get("name"));
		sugar.productCatalog.createDrawer.getEditField("webSite").set(ds.get(1).get("webSite"));
		sugar.productCatalog.createDrawer.getEditField("status").set(ds.get(1).get("status"));		
		sugar.productCatalog.createDrawer.getEditField("stockQuantity").set(ds.get(1).get("stockQuantity"));
		sugar.productCatalog.createDrawer.getEditField("costPrice").set(ds.get(1).get("costPrice"));
		sugar.productCatalog.createDrawer.getEditField("unitPrice").set(ds.get(1).get("unitPrice"));
		sugar.productCatalog.createDrawer.getEditField("listPrice").set(ds.get(1).get("listPrice"));
		sugar.productCatalog.createDrawer.cancel();

		// back to origin record's record view
		sugar.productCatalog.recordView.getDetailField("name").assertEquals(ds.get(0).get("name"),true);
		sugar.productCatalog.recordView.getDetailField("webSite").assertEquals(ds.get(0).get("webSite"),true);
		sugar.productCatalog.recordView.getDetailField("stockQuantity").assertEquals(ds.get(0).get("stockQuantity"),true);
		sugar.productCatalog.recordView.getDetailField("costPrice").assertContains(ds.get(0).get("costPrice"),true);
		sugar.productCatalog.recordView.getDetailField("unitPrice").assertContains(ds.get(0).get("unitPrice"),true);
		sugar.productCatalog.recordView.getDetailField("listPrice").assertContains(ds.get(0).get("listPrice"),true);

		// check no new record on list view
		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.getDetailField(1, "name").assertEquals(ds.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}