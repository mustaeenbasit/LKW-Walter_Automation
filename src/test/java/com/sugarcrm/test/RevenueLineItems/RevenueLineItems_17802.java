package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_17802 extends SugarTest {
	public void setup() throws Exception {
		sugar().productCatalog.api.create();
		sugar().opportunities.api.create();
		sugar().revLineItems.api.create();
		sugar().login();
	}

	/**
	 * Verify products can be modified successfully in an existing Revenue Line Items 
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17802_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);

		// Inline edit (Product)
		// TODO: VOOD-854
		new VoodooControl("i", "css", "span[data-name='product_template_name']").hover();
		new VoodooControl("i", "css", "span[data-name='product_template_name'] .fa.fa-pencil").click();
		VoodooSelect productEdit = (VoodooSelect)sugar().revLineItems.recordView.getEditField("product");
		productEdit.clickSearchForMore();
		sugar().productCatalog.searchSelect.selectRecord(1);
		VoodooUtils.waitForReady();

		// Verify product catalog name after SSV
		productEdit.assertEquals(sugar().productCatalog.getDefaultData().get("name"), true);

		// Inline Edit, required to complete RLI record save (Opp name)
		// TODO: VOOD-854
		new VoodooControl("i", "css", "span[data-name='opportunity_name']").hover();
		new VoodooControl("i", "css", "span[data-name='opportunity_name'] .fa.fa-pencil").click();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.recordView.save();

		// Verify product catalog name after RLI save
		sugar().revLineItems.recordView.getDetailField("product").assertEquals(sugar().productCatalog.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}