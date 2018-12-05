package com.sugarcrm.test.RevenueLineItems;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18183 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().productCategories.api.create();
		sugar().login();

		// An Opportunity is created and linked to the created account
		sugar().opportunities.create();
	}

	/**
	 * Verify that product Category field is present in the edit, record and detail view of revenue line item
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18183_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Revenue Line Item module and Click create
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// Verify that the Category field is present in the RLI edit view
		VoodooControl categoryField = sugar().revLineItems.createDrawer.getEditField("category");

		// Select product category   
		categoryField.set(sugar().productCategories.getDefaultData().get("name"));

		// Verify that the existing product category record is selected successfully
		categoryField.assertEquals(sugar().productCategories.getDefaultData().get("name"), true);

		// Fill all the required Fields and select Save
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().revLineItems.createDrawer.save();

		// Navigate to RLI record view of the created record
		sugar().revLineItems.listView.clickRecord(1);

		// Verify that the selected product category is displayed on the record view
		sugar().revLineItems.recordView.getDetailField("category").assertEquals(sugar().productCategories.getDefaultData().get("name"), true);

		// Go to RLI list view
		sugar().revLineItems.navToListView();

		// Verify that the Category column is present in the list view and populated with the category selected
		VoodooControl categoryFieldInListView = sugar().revLineItems.listView.getDetailField(1, "category");
		categoryFieldInListView.assertVisible(true);
		categoryFieldInListView.assertContains(sugar().productCategories.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}