package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17786 extends SugarTest {
	String productCategories;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().productCatalog.api.create();

		// create product category
		sugar().productCategories.api.create();

		// Login as admin user
		sugar().login();
		sugar().opportunities.navToListView();
		// link created account to Opportunity
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		// save 
		sugar().opportunities.listView.saveRecord(1);

		// link product catalog to product category 
		// TODO: VOOD-444
		productCategories = sugar().productCategories.getDefaultData().get("name");
		sugar().productCatalog.navToListView();
		sugar().productCatalog.listView.editRecord(1);
		sugar().productCatalog.listView.getEditField(1, "productCategory").set(productCategories);
		// save 
		sugar().productCatalog.listView.saveRecord(1);
	}

	/**
	 * Verify that category field is readonly in revenue line item edit, detail and inline edit modes.
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17786_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		// create a RLI 
		sugar().revLineItems.listView.create();
		// fill the required fields
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().revLineItems.getDefaultData().get("relOpportunityName"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		// Select Product created in the setup
		sugar().revLineItems.createDrawer.getEditField("product").set(sugar().productCatalog.getDefaultData().get("name"));

		// TODO: VOOD-1445
		// Verify that Category field is readonly and populated with the correct value
		VoodooControl productCategory = new VoodooControl("span", "css", ".fld_category_name.edit");
		productCategory.assertAttribute("class", "disabled");
		productCategory.assertEquals(productCategories, true);
		sugar().revLineItems.createDrawer.save();

		// In the list view hit edit from the record's action dropdown
		sugar().revLineItems.listView.editRecord(1);
		// TODO: VOOD-1445
		// Verify that Category field is readonly
		new VoodooControl("span", "css", ".fld_category_name.edit").assertAttribute("class", "disabled");
		// Cancel editing and go to record's detail view
		sugar().revLineItems.listView.cancelRecord(1);
		sugar().revLineItems.listView.clickRecord(1);

		// TODO: VOOD-1445
		// Verify that Category field is readonly 
		new VoodooControl("span", "css", ".detail.fld_category_name").assertAttribute("class", "disabled");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}