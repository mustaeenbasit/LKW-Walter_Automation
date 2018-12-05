package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20877 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();			
	}

	/**
	 * Verify that product catalog cannot be created when leaving required field empty.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20877_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);	
		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.create();
		sugar.productCatalog.createDrawer.getEditField("stockQuantity").set("quantity");
		sugar.productCatalog.createDrawer.save();
		sugar.alerts.getError().assertContains(ds.get(0).get("assert1"), true);
		sugar.alerts.closeAllError();
		// TODO: VOOD-1292
		// check warning message about required fields	
		new VoodooControl("i", "css", "span.fld_name.edit.error i.fa-exclamation-circle").assertVisible(true);	
		new VoodooControl("i", "xpath", "//input[@name='name']/../span/i").hover();
		new VoodooControl("div", "css", "div.tooltip.fade.top.in").assertElementContains(ds.get(0).get("assert2"), true);
		
		new VoodooControl("i", "xpath", "//input[@name='cost_price']/../span/i").assertVisible(true);
		new VoodooControl("i", "xpath", "//input[@name='cost_price']/../span/i").hover();
		new VoodooControl("div", "css", "div.tooltip.fade.top.in").assertElementContains(ds.get(0).get("assert2"), true);
		
		new VoodooControl("i", "xpath", "//input[@name='discount_price']/../span/i").assertVisible(true);
		new VoodooControl("i", "xpath", "//input[@name='discount_price']/../span/i").hover();
		new VoodooControl("div", "css", "div.tooltip.fade.top.in").assertElementContains(ds.get(0).get("assert2"), true);	
		
		new VoodooControl("i", "xpath", "//input[@name='list_price']/../span/i").assertVisible(true);
		new VoodooControl("i", "xpath", "//input[@name='list_price']/../span/i").hover();
		new VoodooControl("div", "css", "div.tooltip.fade.top.in").assertElementContains(ds.get(0).get("assert2"), true);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}