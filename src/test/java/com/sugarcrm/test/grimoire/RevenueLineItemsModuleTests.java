package com.sugarcrm.test.grimoire;
import java.text.DecimalFormat;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for menu dropdown items, listview headers, sortBy, edit/detail list & detail hook values,
 * preview pane & subpanels on record view.
 *
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItemsModuleTests extends SugarTest{
	FieldSet revDefaultvalue = new FieldSet(), oppDefaultvalue  = new FieldSet(), productDefaultValue = new FieldSet();
	String currencyVal = "";
	DecimalFormat formatter = new DecimalFormat("##,###.00");

	public void setup() throws Exception {
		productDefaultValue = sugar().productCatalog.getDefaultData();
		revDefaultvalue = sugar().revLineItems.getDefaultData();
		oppDefaultvalue = sugar().opportunities.getDefaultData();
		currencyVal = String.format("%s%s", "$", formatter.format(Double.parseDouble(revDefaultvalue.get("likelyCase"))));
		sugar().login();	
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().revLineItems.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().revLineItems);

		// Verify menu items
		sugar().revLineItems.menu.getControl("createRevenueLineItem").assertVisible(true);
		sugar().revLineItems.menu.getControl("viewRevenueLineItems").assertVisible(true);
		sugar().revLineItems.menu.getControl("importRevenueLineItems").assertVisible(true);
		sugar().navbar.clickModuleDropdown(sugar().revLineItems); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		sugar().revLineItems.navToListView();

		// Verify all headers in listview
		for(String header : sugar().revLineItems.listView.getHeaders()){
			// commit_stage available if "forecasts" enabled
			if(!header.equals("commit_stage")) {
				sugar().revLineItems.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
			}
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() test complete.");
	}

	@Test
	public void sortOrderBySalesStage() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderBySalesStage()...");

		String newSalesStage = "Value Proposition";
		// 2 RLIs having 1 with default data and another with custom data (sales stage)
		sugar().revLineItems.api.create();
		FieldSet customTargetList = new FieldSet();
		customTargetList.put("salesStage", newSalesStage);
		sugar().revLineItems.api.create(customTargetList);

		sugar().revLineItems.navToListView(); // to reload data, somehow refresh page is not working on CI

		// Verify records after sort by 'sales stage' in descending and ascending order
		sugar().revLineItems.listView.sortBy("headerSalesstage", false);
		sugar().revLineItems.listView.verifyField(1, "salesStage", newSalesStage);
		sugar().revLineItems.listView.verifyField(2, "salesStage", sugar().revLineItems.getDefaultData().get("salesStage"));

		sugar().revLineItems.listView.sortBy("headerSalesstage", true);
		sugar().revLineItems.listView.verifyField(1, "salesStage", sugar().revLineItems.getDefaultData().get("salesStage"));
		sugar().revLineItems.listView.verifyField(2, "salesStage", newSalesStage);

		VoodooUtils.voodoo.log.info("sortOrderBySalesStage() test complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		// TODO: VOOD-444 - Once resolved all dependencies should be via API
		// Create Dependencies
		sugar().opportunities.api.create();
		sugar().campaigns.api.create();
		sugar().productCategories.api.create();
		sugar().accounts.api.create();

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.listView.saveRecord(1);

		sugar().revLineItems.api.create();
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.editRecord(1);

		// Verify inline edit fields
		// name
		sugar().revLineItems.listView.getEditField(1, "name").assertEquals(revDefaultvalue.get("name"), true);

		// opp-name
		VoodooSelect oppName = (VoodooSelect)sugar().revLineItems.listView.getEditField(1, "relOpportunityName");
		oppName.set(oppDefaultvalue.get("name"));
		oppName.assertEquals(oppDefaultvalue.get("name"), true);

		// account name
		sugar().revLineItems.listView.getEditField(1, "relAccountName").assertVisible(true);

		// sales stage
		sugar().revLineItems.listView.getEditField(1, "salesStage").assertEquals(revDefaultvalue.get("salesStage"), true);

		// Probability (disabled)
		VoodooControl probability = sugar().revLineItems.listView.getEditField(1, "probability");
		Assert.assertTrue("Probability field is enabled", probability.isDisabled());
		probability.assertEquals("10", true);

		// expected close date
		VoodooControl expectedCloseDate = sugar().revLineItems.listView.getEditField(1, "date_closed");
		expectedCloseDate.scrollIntoViewIfNeeded(false);
		expectedCloseDate.assertAttribute("value", revDefaultvalue.get("date_closed"), true);

		// product
		VoodooControl product = sugar().revLineItems.listView.getEditField(1, "product");
		product.scrollIntoViewIfNeeded(false);
		product.assertVisible(true);

		// category
		VoodooControl category = sugar().revLineItems.listView.getEditField(1, "category");
		category.scrollIntoViewIfNeeded(false);
		category.assertVisible(true);
		category.set(sugar().productCategories.getDefaultData().get("name"));

		// quantity
		VoodooControl quantity = sugar().revLineItems.listView.getEditField(1, "quantity");
		quantity.scrollIntoViewIfNeeded(false);
		quantity.assertVisible(true);

		// likelyCase
		VoodooControl likelyCase = sugar().revLineItems.listView.getEditField(1, "likelyCase");
		likelyCase.scrollIntoViewIfNeeded(false);
		likelyCase.assertVisible(true);

		// bestCase
		VoodooControl bestCase = sugar().revLineItems.listView.getEditField(1, "bestCase");
		bestCase.scrollIntoViewIfNeeded(false);
		bestCase.assertVisible(true);

		// worstCase
		VoodooControl worstCase = sugar().revLineItems.listView.getEditField(1, "worstCase");
		worstCase.scrollIntoViewIfNeeded(false);
		worstCase.assertVisible(true);

		// assigned
		sugar().revLineItems.listView.getEditField(1, "relAssignedTo").assertVisible(true);

		// Date Created (read only)
		VoodooControl dateCreated = sugar().revLineItems.listView.getEditField(1, "date_created_date");
		dateCreated.assertExists(true);
		dateCreated.assertAttribute("class", "edit", false);

		// Date modified (read only)
		VoodooControl dateModified = sugar().revLineItems.listView.getEditField(1, "date_modified_date");
		dateModified.assertExists(true);
		dateModified.assertAttribute("class", "edit", false);

		// save the record
		sugar().revLineItems.listView.saveRecord(1);
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// verify list fields
		// name
		sugar().revLineItems.listView.getDetailField(1, "name").assertEquals(revDefaultvalue.get("name"), true);

		// opp-name
		sugar().revLineItems.listView.getDetailField(1, "relOpportunityName").assertEquals(oppDefaultvalue.get("name"), true);

		// account name
		sugar().revLineItems.listView.getDetailField(1, "relAccountName").assertVisible(true);

		// sales stage
		sugar().revLineItems.listView.getDetailField(1, "salesStage").assertEquals(revDefaultvalue.get("salesStage"), true);

		// Probability
		sugar().revLineItems.listView.getDetailField(1, "probability").assertVisible(true);

		// expected close date
		VoodooControl closeDate = sugar().revLineItems.listView.getDetailField(1, "date_closed");
		closeDate.scrollIntoViewIfNeeded(false);
		closeDate.assertEquals(revDefaultvalue.get("date_closed"), true);

		// product
		product = sugar().revLineItems.listView.getDetailField(1, "product");
		product.scrollIntoViewIfNeeded(false);
		product.assertExists(true);

		// category
		category = sugar().revLineItems.listView.getDetailField(1, "category");
		category.scrollIntoViewIfNeeded(false);
		category.assertExists(true);

		// quantity
		quantity = sugar().revLineItems.listView.getDetailField(1, "quantity");
		quantity.scrollIntoViewIfNeeded(false);
		quantity.assertVisible(true);

		currencyVal = String.format("%s%s", "$", formatter.format(Double.parseDouble(revDefaultvalue.get("likelyCase"))));

		// likelyCase
		likelyCase = sugar().revLineItems.listView.getDetailField(1, "likelyCase");
		likelyCase.scrollIntoViewIfNeeded(false);
		likelyCase.assertEquals(currencyVal, true);

		// bestCase
		bestCase = sugar().revLineItems.listView.getDetailField(1, "bestCase");
		bestCase.scrollIntoViewIfNeeded(false);
		bestCase.assertEquals(currencyVal, true);

		// worstCase
		worstCase = sugar().revLineItems.listView.getDetailField(1, "worstCase");
		worstCase.scrollIntoViewIfNeeded(false);
		worstCase.assertEquals(currencyVal, true);

		// assigned
		sugar().revLineItems.listView.getDetailField(1, "relAssignedTo").assertVisible(true);

		// Date Created
		sugar().revLineItems.listView.getDetailField(1, "date_created_date").assertVisible(true);

		// Date modified
		sugar().revLineItems.listView.getDetailField(1, "date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		currencyVal = String.format("%s%s", "$", formatter.format(Double.parseDouble(productDefaultValue.get("unitPrice"))));

		// TODO: VOOD-444 - Once resolved it should create via API
		// Create dependencies
		sugar().productCatalog.api.create();
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().campaigns.api.create();

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.listView.saveRecord(1);

		sugar().revLineItems.api.create();
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.showMore();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(oppDefaultvalue.get("name"));
		sugar().revLineItems.recordView.getEditField("product").set(sugar().productCatalog.getDefaultData().get("name"));
		sugar().revLineItems.recordView.getEditField("relCampaign").set(sugar().campaigns.getDefaultData().get("name"));
		sugar().revLineItems.recordView.save();
		sugar().revLineItems.navToListView();

		// preview record
		sugar().revLineItems.listView.previewRecord(1);

		// preview pane show more
		sugar().previewPane.showMore();

		// Verify preview field values
		// name
		sugar().previewPane.getPreviewPaneField("name").assertEquals(revDefaultvalue.get("name"), true);

		// opportunity name
		sugar().previewPane.getPreviewPaneField("relOpportunityName").assertVisible(true);

		// account name
		sugar().previewPane.getPreviewPaneField("relAccountName").assertVisible(true);

		// sales stage
		sugar().previewPane.getPreviewPaneField("salesStage").assertEquals(revDefaultvalue.get("salesStage"), true);

		// probability
		sugar().previewPane.getPreviewPaneField("probability").assertEquals("10", true);

		// forecast
		sugar().previewPane.getPreviewPaneField("forecast").assertVisible(true);

		// date closed
		sugar().previewPane.getPreviewPaneField("date_closed").assertEquals(revDefaultvalue.get("date_closed"), true);

		// product
		sugar().previewPane.getPreviewPaneField("product").assertVisible(true);

		// quantity
		sugar().previewPane.getPreviewPaneField("quantity").assertVisible(true);

		// unit price
		sugar().previewPane.getPreviewPaneField("unitPrice").assertEquals(currencyVal, true);

		// total discount amount
		sugar().previewPane.getPreviewPaneField("discountPrice").assertVisible(true);

		// calculated RLI
		sugar().previewPane.getPreviewPaneField("calcRLIAmount").assertEquals(currencyVal, true);

		currencyVal = String.format("%s%s", "$", formatter.format(Double.parseDouble(revDefaultvalue.get("likelyCase"))));

		// Likely
		sugar().previewPane.getPreviewPaneField("likelyCase").assertEquals(currencyVal, true);

		// show more fields
		sugar().previewPane.showMore();

		// Best
		sugar().previewPane.getPreviewPaneField("bestCase").assertEquals(currencyVal, true);

		// Worst
		sugar().previewPane.getPreviewPaneField("worstCase").assertEquals(currencyVal, true);

		// Next Step
		sugar().previewPane.getPreviewPaneField("nextStep").assertVisible(true);

		// type
		sugar().previewPane.getPreviewPaneField("type").assertVisible(true);

		// Lead Source
		sugar().previewPane.getPreviewPaneField("leadSource").assertVisible(true);

		// Campaign
		sugar().previewPane.getPreviewPaneField("relCampaign").assertVisible(true);

		// Assigned
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertVisible(true);

		// Teams
		sugar().previewPane.getPreviewPaneField("relTeam").assertVisible(true);

		// Description
		sugar().previewPane.getPreviewPaneField("description").assertVisible(true);

		// list price
		VoodooControl listPrice = sugar().previewPane.getPreviewPaneField("listPrice");
		listPrice.scrollIntoView();
		listPrice.assertVisible(true);

		// cost
		sugar().previewPane.getPreviewPaneField("costPrice").assertVisible(true);

		// tax class
		sugar().previewPane.getPreviewPaneField("taxClass").assertVisible(true);

		// date modified
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);

		// date created
		sugar().previewPane.getPreviewPaneField("date_created_date").assertVisible(true);

		// tags
		sugar().previewPane.getPreviewPaneField("tags").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().revLineItems.api.create();
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);

		// Verify subpanels on record view
		sugar().revLineItems.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(true);
		sugar().revLineItems.recordView.subpanels.get(sugar().meetings.moduleNamePlural).assertVisible(true);
		sugar().revLineItems.recordView.subpanels.get(sugar().tasks.moduleNamePlural).assertVisible(true);
		sugar().revLineItems.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);
		sugar().revLineItems.recordView.subpanels.get(sugar().emails.moduleNamePlural).assertVisible(true);
		sugar().revLineItems.recordView.subpanels.get(sugar().documents.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		DecimalFormat formatter = new DecimalFormat("##,###.00");
		currencyVal = String.format("%s%s", "$", formatter.format(Double.parseDouble(productDefaultValue.get("unitPrice"))));

		// TODO: VOOD-444
		// Create Dependencies
		sugar().opportunities.api.create();
		sugar().campaigns.api.create();
		sugar().productCatalog.api.create();
		sugar().accounts.api.create();

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.listView.saveRecord(1);

		sugar().revLineItems.api.create();
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();

		// Verify edit record fields
		// name
		sugar().revLineItems.recordView.getEditField("name").assertEquals(revDefaultvalue.get("name"), true);

		// opp name
		VoodooControl oppName = sugar().revLineItems.recordView.getEditField("relOpportunityName");
		oppName.set(oppDefaultvalue.get("name"));
		oppName.assertEquals(oppDefaultvalue.get("name"), true);

		// account name
		sugar().revLineItems.recordView.getEditField("relAccountName").assertVisible(true);

		// sales stage
		sugar().revLineItems.recordView.getEditField("salesStage").assertEquals(revDefaultvalue.get("salesStage"), true);

		// probability (read only)
		VoodooControl probability = sugar().revLineItems.recordView.getEditField("probability");
		Assert.assertTrue("Probability field is enabled.", probability.isDisabled());
		probability.assertEquals("10", true);

		// expected close date
		sugar().revLineItems.recordView.getEditField("date_closed").assertEquals(revDefaultvalue.get("date_closed"), true);

		// Product
		VoodooControl editProduct = sugar().revLineItems.recordView.getEditField("product");
		editProduct.set(sugar().productCatalog.getDefaultData().get("name"));
		editProduct.assertVisible(true);

		// Product Category
		sugar().revLineItems.recordView.getEditField("category").assertVisible(true);

		// quantity
		sugar().revLineItems.recordView.getEditField("quantity").assertVisible(true);

		// unit price
		sugar().revLineItems.recordView.getEditField("unitPrice").assertVisible(true);

		// total discount amount
		sugar().revLineItems.recordView.getEditField("discountPrice").assertVisible(true);

		// calc RLI amount (read-only)
		VoodooControl calcRLIAmount = sugar().revLineItems.recordView.getEditField("calcRLIAmount");
		calcRLIAmount.assertAttribute("class", "edit", false);
		calcRLIAmount.assertVisible(true);

		// Likely
		sugar().revLineItems.recordView.getEditField("likelyCase").assertVisible(true);

		// show more fields
		sugar().revLineItems.recordView.showMore();

		// best
		sugar().revLineItems.recordView.getEditField("bestCase").assertVisible(true);

		// worst
		sugar().revLineItems.recordView.getEditField("worstCase").assertVisible(true);

		// next step
		sugar().revLineItems.recordView.getEditField("nextStep").assertVisible(true);

		// type
		sugar().revLineItems.recordView.getEditField("type").assertVisible(true);

		// lead source
		sugar().revLineItems.recordView.getEditField("leadSource").assertVisible(true);

		// campaign
		VoodooControl editCampaign = sugar().revLineItems.recordView.getEditField("relCampaign");
		editCampaign.set(sugar().campaigns.getDefaultData().get("name"));
		editCampaign.assertVisible(true);

		// assigned To
		sugar().revLineItems.recordView.getEditField("relAssignedTo").assertVisible(true);

		// teams
		sugar().revLineItems.recordView.getEditField("relTeam").assertVisible(true);

		// description
		sugar().revLineItems.recordView.getEditField("description").assertEquals(revDefaultvalue.get("description"), true);

		// list price (read-only)
		VoodooControl listPrice = sugar().revLineItems.recordView.getEditField("listPrice");
		listPrice.assertAttribute("class", "edit", false);
		listPrice.assertVisible(true);

		// cost price (read-only)
		VoodooControl costPrice = sugar().revLineItems.recordView.getEditField("costPrice");
		costPrice.assertAttribute("class", "edit", false);
		costPrice.assertVisible(true);

		// tax class
		sugar().revLineItems.recordView.getEditField("taxClass").assertVisible(true);

		// date created
		sugar().revLineItems.recordView.getEditField("date_created_date").assertVisible(true);

		// date modified
		sugar().revLineItems.recordView.getEditField("date_modified_date").assertVisible(true);

		// tags
		sugar().revLineItems.recordView.getEditField("tags").assertVisible(true);

		sugar().revLineItems.recordView.save();

		// Associated quote
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("generateQuote").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set("12/31/2015");
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("quoteName").assertVisible(true);

		// cancel record
		sugar().revLineItems.recordView.cancel();

		// Verify detail fields
		// name
		sugar().revLineItems.recordView.getDetailField("name").assertEquals(revDefaultvalue.get("name"), true);

		// opp name
		sugar().revLineItems.recordView.getDetailField("relOpportunityName").assertVisible(true);

		// account name
		sugar().revLineItems.recordView.getDetailField("relAccountName").assertVisible(true);

		// sales stage
		sugar().revLineItems.recordView.getDetailField("salesStage").assertEquals(revDefaultvalue.get("salesStage"), true);

		// probability
		sugar().revLineItems.recordView.getDetailField("probability").assertEquals("10", true);

		// expected close date
		sugar().revLineItems.recordView.getDetailField("date_closed").assertEquals(revDefaultvalue.get("date_closed"), true);

		// Product
		sugar().revLineItems.recordView.getDetailField("product").assertVisible(true);

		// quantity
		sugar().revLineItems.recordView.getDetailField("quantity").assertVisible(true);

		// unit price
		sugar().revLineItems.recordView.getDetailField("unitPrice").assertEquals(currencyVal, true);

		// total discount amount
		sugar().revLineItems.recordView.getDetailField("discountPrice").assertVisible(true);

		// calc RLI amount
		sugar().revLineItems.recordView.getDetailField("calcRLIAmount").assertEquals(currencyVal, true);

		currencyVal = String.format("%s%s", "$", formatter.format(Double.parseDouble(productDefaultValue.get("unitPrice")) * 10));

		// Likely
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertEquals(currencyVal, true);

		// Associated quote
		sugar().revLineItems.recordView.getDetailField("quoteName").assertVisible(true);

		// best
		sugar().revLineItems.recordView.getDetailField("bestCase").assertEquals(currencyVal, true);

		// worst
		sugar().revLineItems.recordView.getDetailField("worstCase").assertEquals(currencyVal, true);

		// next step
		sugar().revLineItems.recordView.getDetailField("nextStep").assertVisible(true);

		// type
		sugar().revLineItems.recordView.getDetailField("type").assertVisible(true);

		// lead source
		sugar().revLineItems.recordView.getDetailField("leadSource").assertVisible(true);

		// campaign
		sugar().revLineItems.recordView.getDetailField("relCampaign").assertVisible(true);

		// assigned To
		sugar().revLineItems.recordView.getDetailField("relAssignedTo").assertEquals("Administrator", true);

		// teams
		sugar().revLineItems.recordView.getDetailField("relTeam").assertVisible(true);

		// description
		sugar().revLineItems.recordView.getDetailField("description").assertEquals(revDefaultvalue.get("description"), true);

		// list price
		sugar().revLineItems.recordView.getDetailField("listPrice").assertVisible(true);

		// cost
		sugar().revLineItems.recordView.getDetailField("costPrice").assertVisible(true);

		// tax class
		sugar().revLineItems.recordView.getDetailField("taxClass").assertVisible(true);

		// date created
		sugar().revLineItems.recordView.getDetailField("date_created_date").assertVisible(true);

		// date modified
		sugar().revLineItems.recordView.getDetailField("date_modified_date").assertVisible(true);

		// tags
		sugar().revLineItems.recordView.getDetailField("tags").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}