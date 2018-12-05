package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20882 extends SugarTest {
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
	public void Products_20882_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCatalog.navToListView();

		for(int i=1;i<ds.size();i++) {
			sugar.productCatalog.listView.clickRecord(i);
			sugar.productCatalog.recordView.copy();
			// check data is copied over
			sugar.productCatalog.createDrawer.getEditField("name").assertEquals(ds.get(0).get("name"), true);
			sugar.productCatalog.createDrawer.getEditField("stockQuantity").assertEquals(ds.get(0).get("stockQuantity"),true);
			sugar.productCatalog.createDrawer.getEditField("webSite").assertEquals(ds.get(0).get("webSite"),true);
			sugar.productCatalog.createDrawer.getEditField("costPrice").assertContains(ds.get(0).get("costPrice"),true);
			sugar.productCatalog.createDrawer.getEditField("unitPrice").assertContains(ds.get(0).get("unitPrice"),true);
			sugar.productCatalog.createDrawer.getEditField("listPrice").assertContains(ds.get(0).get("listPrice"),true);

			// set data for the new record
			sugar.productCatalog.createDrawer.getEditField("name").set(ds.get(i).get("name"));
			sugar.productCatalog.createDrawer.getEditField("webSite").set(ds.get(i).get("webSite"));
			sugar.productCatalog.createDrawer.getEditField("status").set(ds.get(i).get("status"));		
			sugar.productCatalog.createDrawer.getEditField("stockQuantity").set(ds.get(i).get("stockQuantity"));
			sugar.productCatalog.createDrawer.getEditField("costPrice").set(ds.get(i).get("costPrice"));
			sugar.productCatalog.createDrawer.getEditField("unitPrice").set(ds.get(i).get("unitPrice"));
			sugar.productCatalog.createDrawer.getEditField("listPrice").set(ds.get(i).get("listPrice"));
			sugar.productCatalog.createDrawer.save();

			sugar.productCatalog.recordView.getDetailField("name").assertEquals(ds.get(i).get("name"),true);
			sugar.productCatalog.recordView.getDetailField("webSite").assertEquals(ds.get(i).get("webSite"),true);
			sugar.productCatalog.recordView.getDetailField("status").assertEquals(ds.get(i).get("status"),true);
			sugar.productCatalog.recordView.getDetailField("stockQuantity").assertEquals(ds.get(i).get("stockQuantity"),true);
			sugar.productCatalog.recordView.getDetailField("costPrice").assertContains(ds.get(i).get("costPrice"),true);
			sugar.productCatalog.recordView.getDetailField("unitPrice").assertContains(ds.get(i).get("unitPrice"),true);
			sugar.productCatalog.recordView.getDetailField("listPrice").assertContains(ds.get(i).get("listPrice"),true);

			sugar.productCatalog.navToListView();
			sugar.productCatalog.listView.getDetailField(1,"name").assertEquals(ds.get(i).get("name"), true);
			sugar.productCatalog.listView.getDetailField(1,"status").assertEquals(ds.get(i).get("status"), true);
			sugar.productCatalog.listView.getDetailField(1,"stockQuantity").assertEquals(ds.get(i).get("stockQuantity"), true);
			sugar.productCatalog.listView.getDetailField(1,"costPrice").assertContains(ds.get(i).get("costPrice"), true);
			sugar.productCatalog.listView.getDetailField(1,"unitPrice").assertContains(ds.get(i).get("unitPrice"), true);
			sugar.productCatalog.listView.getDetailField(1,"listPrice").assertContains(ds.get(i).get("listPrice"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}