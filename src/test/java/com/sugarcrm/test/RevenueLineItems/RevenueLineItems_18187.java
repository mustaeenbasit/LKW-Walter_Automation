package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.ProductCatalogRecord;
import com.sugarcrm.sugar.records.ProductCategoriesRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18187 extends SugarTest {
	OpportunityRecord myOppRecord; 
	RevLineItemRecord myRevLineItemRecord;
	ProductCatalogRecord myProductRecord;
	ProductCategoriesRecord myProductCategories;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myOppRecord = (OpportunityRecord)sugar().opportunities.api.create();
		myRevLineItemRecord = (RevLineItemRecord)sugar().revLineItems.api.create();
		myProductCategories = (ProductCategoriesRecord)sugar().productCategories.api.create();
		myProductRecord = (ProductCatalogRecord)sugar().productCatalog.api.create();
		sugar().login();

		FieldSet fs = new FieldSet();
		fs.put("relAccountName", myOppRecord.get("relAccountName"));
		// TODO: VOOD-444
		// Edit opportunity record to link the created account record
		myOppRecord.edit(fs);

		fs.clear();
		fs.put("productCategory", myProductCategories.get("name"));
		// TODO: VOOD-444
		// Edit productCatalog record to link the created productCategory record
		myProductRecord.edit(fs);

		fs.clear();
		fs.put("relOpportunityName", myRevLineItemRecord.get("relOpportunityName"));
		fs.put("product", myProductRecord.get("name"));
		// TODO: VOOD-444
		// Edit RevLineItemRecord record to link the created opportunity record, productCatalog record and productCategory record
		myRevLineItemRecord.edit(fs);
	}

	/**
	 * Verify that links to product and product category are functional in RLI list view. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18187_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Revenue Line Item module list view
		sugar().revLineItems.navToListView();

		sugar().revLineItems.listView.getDetailField(1, "product").scrollIntoViewIfNeeded(false);
		// Verify that the link to product is named after the product selected while creating the revenue line item in the setup. 
		sugar().revLineItems.listView.getDetailField(1, "product").assertEquals(myProductRecord.get("name"), true);

		// Verify that the link to the product category is named after product category associated with the selected product.
		sugar().revLineItems.listView.getDetailField(1, "category").assertEquals(myProductCategories.get("name"), true);

		// Click on the product link
		sugar().revLineItems.listView.getDetailField(1, "product").click();
		// Verify that the corresponding product record opens up 
		sugar().productCatalog.recordView.getDetailField("name").assertEquals(myProductRecord.get("name"), true);
		sugar().productCatalog.recordView.getDetailField("productCategory").assertEquals(myProductCategories.get("name"), true);

		//  Return back to RLI list view 
		sugar().revLineItems.navToListView();

		sugar().revLineItems.listView.getDetailField(1, "product").scrollIntoViewIfNeeded(false);
		// Click on the Category link.  
		sugar().revLineItems.listView.getDetailField(1, "category").click();
		// The corresponding product category record is brought up.
		sugar().productCatalog.recordView.getDetailField("name").assertEquals(myProductCategories.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}