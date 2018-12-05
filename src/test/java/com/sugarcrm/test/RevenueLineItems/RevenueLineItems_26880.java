package com.sugarcrm.test.RevenueLineItems;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26880 extends SugarTest {
	FieldSet customDataFS, multiPurposeData;

	public void setup() throws Exception {
		customDataFS = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Create an Opportunity record(Create Opportunity from UI so that linked to the account record and Revenue Line Item exists linked to the Opportunity record as well)
		sugar().opportunities.create();

		// Euro(testName) currency is setup in admin -> Locale ->System Currencies
		multiPurposeData = new FieldSet();
		multiPurposeData.put("currencyName", testName);
		multiPurposeData.put("conversionRate", customDataFS.get("conversionRate"));
		multiPurposeData.put("currencySymbol", customDataFS.get("currencySymbol"));
		sugar().admin.setCurrency(multiPurposeData);
		multiPurposeData.clear();
	}

	/**
	 * Verify that Total discount amount is displayed correctly in RLI record view when change thousand sep to "." and decimal to ","  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26880_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  Click username > Profile in top right. Navigate to Advanced tab. Set '1000s separator:' to '.' Set 'Decimal Symbol:' to ','and Save
		multiPurposeData.put("advanced_grouping_seperator", customDataFS.get("advanced_grouping_seperator"));
		multiPurposeData.put("advanced_decimal_separator", customDataFS.get("advanced_decimal_separator"));
		sugar().users.setPrefs(multiPurposeData);

		// Go to RLI module and click Create
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// Change currency to Euro (testName)
		// TODO: VOOD-983
		new VoodooControl("span", "css", "div[data-name='discount_price'] .currency.edit.fld_currency_id").click();
		new VoodooControl("li", "css", "#select2-drop > ul > li:nth-child(2)").click();

		// Enter 1000 for quantity and 1 for unit price
		sugar().revLineItems.createDrawer.getEditField("unitPrice").set(customDataFS.get("discountPrice"));
		sugar().revLineItems.createDrawer.getEditField("quantity").set(customDataFS.get("quantity"));

		// Enter the rest of the required fields and click Save and navigate to the created RLI record (Keep discount amount field is blank)
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(customDataFS.get("discountPrice"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.save();
		sugar().revLineItems.listView.clickRecord(1);

		VoodooControl detailViewDiscountPrice = sugar().revLineItems.recordView.getDetailField("discountPrice");
		VoodooControl editViewDiscountPrice = sugar().revLineItems.recordView.getEditField("discountPrice");

		// Verify that the Total discount amount If discount amount field is blank it remains blank
		detailViewDiscountPrice.assertEquals("", true);

		// Edit the RLI record and enter discount amount = 0
		sugar().revLineItems.recordView.edit();
		editViewDiscountPrice.set(customDataFS.get("zeroDiscountPrice"));
		sugar().revLineItems.recordView.save();

		// Verify that the Total discount amount shows 0 in both Euro and Dollars.
		detailViewDiscountPrice.assertContains(customDataFS.get("assertZeroInEuro"),true);
		detailViewDiscountPrice.assertContains(customDataFS.get("assertZeroInDollar"),true);

		// Edit the RLI record and enter discount amount > 0
		sugar().revLineItems.recordView.edit();
		editViewDiscountPrice.set(customDataFS.get("greaterThanZeroDiscountPrice"));
		sugar().revLineItems.recordView.save();

		// Verify that the Total discount amount shows values in both Euro and Dollars.
		detailViewDiscountPrice.assertContains(customDataFS.get("assertGreaterThanZeroInEuro"),true);
		detailViewDiscountPrice.assertContains(customDataFS.get("assertGreaterThanZeroInDollar"),true);

		// Edit the RLI record and enter discount amount < 0
		sugar().revLineItems.recordView.edit();
		editViewDiscountPrice.set(customDataFS.get("lessThanZeroDiscountPrice"));
		sugar().revLineItems.recordView.save();

		// Verify that the Total discount amount shows values in both Euro and Dollars.
		detailViewDiscountPrice.assertContains(customDataFS.get("assertLessThanZeroInEuro"),true);
		detailViewDiscountPrice.assertContains(customDataFS.get("assertLessThanZeroInDollar"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}