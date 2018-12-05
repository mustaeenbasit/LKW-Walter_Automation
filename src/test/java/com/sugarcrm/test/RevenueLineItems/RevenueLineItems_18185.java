package com.sugarcrm.test.RevenueLineItems;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18185 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().productCatalog.api.create();
		sugar().productCategories.api.create();
		sugar().login();

		// An Opportunity is created and linked to the created account
		sugar().opportunities.create();

		// TODO: VOOD-444 relate category to product
		sugar().productCatalog.navToListView();
		sugar().productCatalog.listView.editRecord(1);
		sugar().productCatalog.listView.getEditField(1, "productCategory").set(sugar().productCategories.getDefaultData().get("name"));
		sugar().productCatalog.listView.saveRecord(1);
	}

	/**
	 * Verify that product Category Name field is populated and becomes readonly as soon as a product is selected
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18185_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Revenue Line Item module and Click create
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// Fill all the required Fields and select
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));

		// Select the product from the product field.
		sugar().revLineItems.createDrawer.getEditField("product").set(sugar().productCatalog.getDefaultData().get("name"));

		// Verify that the product category field is populated with the value corresponding to the selected product
		sugar().revLineItems.createDrawer.getEditField("category").assertEquals(sugar().productCategories.getDefaultData().get("name"), true);

		// Verify that the Category field becomes readonly
		// TODO: VOOD-1445
		Assert.assertTrue("Category field is enabled.", new VoodooControl("span", "css", ".fld_category_name.edit").isDisabled());

		// Save the record and navigate to the record view of the created record 
		sugar().revLineItems.createDrawer.save();
		sugar().revLineItems.listView.clickRecord(1);

		// Verify that the product category is displayed on the record view
		sugar().revLineItems.recordView.getDetailField("category").assertEquals(sugar().productCategories.getDefaultData().get("name"), true);

		// Repeat test case in the record view mode(detail mode)
		// Create a new RLI record with default values and go to record view
		sugar().revLineItems.create();
		sugar().revLineItems.listView.clickRecord(1);

		// Add product by in-line edit
		// TODO: VOOD-854
		new VoodooControl ("span", "css", "span[data-voodoo-name='product_template_name']").hover();
		VoodooControl pencilIconCtrl = new VoodooControl ("i", "css", "span[data-name='product_template_name'] .fa.fa-pencil");
		pencilIconCtrl.click();
		new VoodooControl("input", "css", "#select2-drop input").set(sugar().productCatalog.getDefaultData().get("name"));
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#select2-drop > ul:nth-child(2) li:nth-child(1)").click();

		// TODO: VOOD-1445
		// Verify that the product category field is populated with the value corresponding to the selected product
		new VoodooControl("a", "css", ".fld_category_name a").assertEquals(sugar().productCategories.getDefaultData().get("name"), true);

		// Verify that the Category field becomes readonly(Verify that no pencil icon is visible on hovering Product Category field)
		new VoodooControl ("span", "css", "span[data-voodoo-name='category_name']").hover();
		pencilIconCtrl.assertVisible(false);

		// Save the record
		sugar().revLineItems.recordView.save();

		// Verify that the selected product category is displayed on the record view
		sugar().revLineItems.recordView.getDetailField("category").assertEquals(sugar().productCategories.getDefaultData().get("name"), true);

		// Repeat test case in the list view
		// Create a new RLI record and go to list view of the created record
		FieldSet rliName = new FieldSet();
		rliName.put("name", testName+"_"+1);
		sugar().revLineItems.create(rliName);
		rliName.clear();

		// Add product by in-line edit
		sugar().revLineItems.listView.editRecord(1);
		VoodooControl editCategoryInListview = sugar().revLineItems.listView.getEditField(1, "category");
		sugar().revLineItems.listView.getEditField(1, "likelyCase").scrollIntoView();
		sugar().revLineItems.listView.getEditField(1, "product").set(sugar().productCatalog.getDefaultData().get("name"));

		// Verify that the product category field is populated with the value corresponding to the selected product
		editCategoryInListview.assertEquals(sugar().productCategories.getDefaultData().get("name"), true);

		// Verify that the Category field becomes readonly
		Assert.assertTrue("Category field is enabled.", new VoodooControl("span", "css", ".fld_category_name.edit").isDisabled());
		//productCategory.assertAttribute("class", "disabled");

		// Save the record
		sugar().revLineItems.listView.saveRecord(1);

		// Verify that the selected product category is displayed on the list view
		sugar().revLineItems.listView.getDetailField(1, "category").assertEquals(sugar().productCategories.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}