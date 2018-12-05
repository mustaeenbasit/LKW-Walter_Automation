package com.sugarcrm.test.RevenueLineItems;

import java.text.DecimalFormat;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17861 extends SugarTest {
	FieldSet productData;

	public void setup() throws Exception {
		productData = testData.get(testName).get(0);
		FieldSet currencySet = testData.get(testName+"_currency").get(0);

		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		// create product catalog
		sugar().productCatalog.api.create();

		sugar().login();
		// Create a new currency i.e Euro
		sugar().admin.setCurrency(currencySet);
		sugar().opportunities.navToListView();

		// TODO: VOOD:444 
		// link created account to Opportunity
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		// save 
		sugar().opportunities.listView.saveRecord(1);
		sugar().logout();
	}

	/**
	 * Verify that preview pane shows both currencies when a revenue line item is created using the currency different from base. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17861_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		// TODO: VOOD:444
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().revLineItems.getDefaultData().get("relOpportunityName"));
		// Select Product created in the setup
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("product").set(sugar().productCatalog.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("discountPrice").set(productData.get("discountPrice"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(productData.get("likelyCase"));

		// TODO: VOOD-983 Need lib support for currency dropdowns in RLI
		new VoodooSelect("span", "css", "[data-voodoo-name='currency_id'] .select2-container.select2 .select2-chosen").set(productData.get("displayCurrency"));
		sugar().revLineItems.createDrawer.save();

		// Go to opportunity record and in RLI subpanel
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel rliSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubPanel.scrollIntoView();
		// click on preview button 
		rliSubPanel.clickPreview(1);
		sugar().previewPane.showMore();
		double amount = Double.parseDouble(productData.get("likelyCase"));
		DecimalFormat formatter = new DecimalFormat("##,###.00");
		String convertedAmount	 = formatter.format(amount);
		sugar().previewPane.setModule(sugar().revLineItems);
		VoodooControl unitPrice =  sugar().previewPane.getPreviewPaneField("unitPrice");
		// Verify that unit price amount shows values in both Euro and Dollars.
		unitPrice.assertContains(convertedAmount, true);
		unitPrice.assertContains(productData.get("changedCurrency"), true);

		VoodooControl totalDiscountAmount =  sugar().previewPane.getPreviewPaneField("discountPrice");
		// Verify that total discount amount shows values in both Euro and Dollars.
		totalDiscountAmount.assertContains(productData.get("discountPrice"), true);

		VoodooControl calculatedAmount = sugar().previewPane.getPreviewPaneField("calcRLIAmount");
		// Verify that calculated RLI amount shows values in both Euro and Dollars.
		calculatedAmount.assertContains(convertedAmount, true);
		calculatedAmount.assertContains(productData.get("changedCurrency"), true);

		// Verify that likely amount shows values in both Euro and Dollars.
		VoodooControl likelyAmount = sugar().previewPane.getPreviewPaneField("likelyCase");
		likelyAmount.scrollIntoView();
		likelyAmount.assertContains(convertedAmount, true);
		likelyAmount.assertContains(productData.get("changedCurrency"), true);

		// Verify that best amount shows values in both Euro and Dollars.
		VoodooControl bestAmount = sugar().previewPane.getPreviewPaneField("bestCase");
		bestAmount.assertContains(convertedAmount, true);
		bestAmount.assertContains(productData.get("changedCurrency"), true);

		// Verify that worst amount shows values in both Euro and Dollars.
		VoodooControl worstAmount = sugar().previewPane.getPreviewPaneField("worstCase");
		worstAmount.assertContains(convertedAmount, true);
		worstAmount.assertContains(productData.get("changedCurrency"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}