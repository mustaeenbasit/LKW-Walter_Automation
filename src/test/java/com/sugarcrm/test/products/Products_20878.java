package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20878 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();			
	}

	/**
	 * Verify that product catalog cannot be created when entering invalid data in edit view
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20878_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet fs = testData.get(testName).get(0);	
		
		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.create();
		sugar.productCatalog.createDrawer.getEditField("date_available").set(fs.get("date_available"));
		sugar.productCatalog.createDrawer.getEditField("name").set(fs.get("name"));
		sugar.productCatalog.createDrawer.getEditField("date_available").assertEquals("", true);
		sugar.productCatalog.createDrawer.getEditField("stockQuantity").set(fs.get("stockQuantity"));
		sugar.productCatalog.createDrawer.getEditField("costPrice").set(fs.get("costPrice"));
		sugar.productCatalog.createDrawer.getEditField("listPrice").set(fs.get("listPrice"));
		sugar.productCatalog.createDrawer.getEditField("unitPrice").set(fs.get("unitPrice"));
		sugar.productCatalog.createDrawer.save();
		
		// TODO: VOOD-1292
		sugar.alerts.getError().assertContains(fs.get("assert1"), true);
		sugar.alerts.closeAllError();
		
		VoodooUtils.waitForReady();
		
		// check warning message about invalid input	
		new VoodooControl("i", "css", "span.fld_qty_in_stock.edit.error i.fa-exclamation-circle").assertVisible(true);
		new VoodooControl("i", "css", "span.fld_qty_in_stock.edit.error i.fa-exclamation-circle").hover();
		new VoodooControl("div", "css", "div.tooltip.fade.top.in").assertElementContains(fs.get("assert2"), true);
		
		new VoodooControl("i", "xpath", "//input[@name='cost_price']/../span/i").assertVisible(true);
		new VoodooControl("i", "xpath", "//input[@name='cost_price']/../span/i").hover();
		new VoodooControl("div", "css", "div.tooltip.fade.top.in").assertElementContains(fs.get("assert2"), true);
		
		new VoodooControl("i", "xpath", "//input[@name='discount_price']/../span/i").assertVisible(true);
		new VoodooControl("i", "xpath", "//input[@name='discount_price']/../span/i").hover();
		new VoodooControl("div", "css", "div.tooltip.fade.top.in").assertElementContains(fs.get("assert2"), true);	
		
		new VoodooControl("i", "xpath", "//input[@name='list_price']/../span/i").assertVisible(true);
		new VoodooControl("i", "xpath", "//input[@name='list_price']/../span/i").hover();
		new VoodooControl("div", "css", "div.tooltip.fade.top.in").assertElementContains(fs.get("assert2"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}