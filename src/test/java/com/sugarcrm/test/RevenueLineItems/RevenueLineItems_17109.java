package com.sugarcrm.test.RevenueLineItems;

import java.text.DecimalFormat;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_17109 extends SugarTest {
	FieldSet currencySetupData, customData;

	public void setup() throws Exception {
		currencySetupData = testData.get(testName+"_currency").get(0);
		customData = testData.get(testName).get(0);
		sugar().opportunities.api.create();
		sugar().login();

		// create new currency i.e. EURO
		sugar().admin.setCurrency(currencySetupData);
	}

	/**
	 * Check currency fields on create drawer & record edit page
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17109_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create RLI record
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.showMore();

		// TODO: VOOD-983
		// Verify currency dropdown for currency fields i.e unit, total amount, likely, best, worst
		new VoodooControl("span", "css", ".fld_discount_price.edit .currency.edit.fld_currency_id div.select2 a span.select2-chosen").assertEquals(customData.get("dollar_currency_label"), true);
		new VoodooControl("span", "css", ".fld_discount_amount.edit .currency.edit.fld_currency_id div.select2 a span.select2-chosen").assertEquals(customData.get("dollar_currency_label"), true);
		new VoodooControl("span", "css", ".fld_likely_case.edit .currency.edit.fld_currency_id div.select2 a span.select2-chosen").assertEquals(customData.get("dollar_currency_label"), true);
		new VoodooControl("span", "css", ".fld_best_case.edit .currency.edit.fld_currency_id div.select2 a span.select2-chosen").assertEquals(customData.get("dollar_currency_label"), true);
		new VoodooControl("span", "css", ".fld_worst_case.edit .currency.edit.fld_currency_id div.select2 a span.select2-chosen").assertEquals(customData.get("dollar_currency_label"), true);

		VoodooControl quantity = sugar().revLineItems.createDrawer.getEditField("quantity");
		VoodooControl unitPrice = sugar().revLineItems.createDrawer.getEditField("unitPrice");
		VoodooControl calcRLIAmount = sugar().revLineItems.createDrawer.getEditField("calcRLIAmount");

		DecimalFormat formatter = new DecimalFormat("##,###.00");
		double quantityValue = Integer.parseInt(customData.get("quantity"));
		double unitDollarValue = Integer.parseInt(customData.get("unit_price1"));
		String calcRLIAmountDollarValue = String.format("%s%s", customData.get("dollar_symbol"),formatter.format(unitDollarValue));

		// Verify other currency fields after entering unit price
		unitPrice.set(customData.get("unit_price1"));
		quantity.click(); // to populate fields for other currency
		unitPrice.assertEquals(formatter.format(unitDollarValue), true);
		sugar().revLineItems.createDrawer.getEditField("likelyCase").assertEquals(formatter.format(unitDollarValue), true);
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertEquals(formatter.format(unitDollarValue), true);
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertEquals(formatter.format(unitDollarValue), true);

		// Verify calculate rli amount field is read-only
		calcRLIAmount.assertAttribute("class", "edit", false);
		calcRLIAmount.assertEquals(calcRLIAmountDollarValue, true);

		// Setting quantity & discount amount
		quantity.set(customData.get("quantity"));
		sugar().revLineItems.createDrawer.getEditField("discountPrice").set(customData.get("discount_amount"));

		// required fields, rli, opportunity name & expected closed date with save
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.save();

		// Click on the created RLI in list view
		sugar().revLineItems.listView.clickRecord(1);

		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify currency fields on detail view
		double discountDollarValue = Integer.parseInt(customData.get("discount_amount"));
		double calculatedDollarValue = (unitDollarValue*quantityValue)-discountDollarValue;
		String currencyCalValue = String.format("%s%s", customData.get("dollar_symbol"),formatter.format(calculatedDollarValue));
		String discountPriceDollarValue = String.format("%s%s", customData.get("dollar_symbol"),formatter.format(discountDollarValue));
		sugar().revLineItems.recordView.getDetailField("unitPrice").assertEquals(calcRLIAmountDollarValue, true);
		sugar().revLineItems.recordView.getDetailField("discountPrice").assertEquals(discountPriceDollarValue, true);
		sugar().revLineItems.recordView.getDetailField("calcRLIAmount").assertEquals(currencyCalValue, true);

		// Edit record
		sugar().revLineItems.recordView.edit();

		// Click on currency field to open drop down field, switch to EUR from USD.
		// TODO: VOOD-983
		new VoodooSelect("span", "css", "[data-fieldname='discount_amount'] .fld_currency_id .select2-chosen").set(currencySetupData.get("currencySymbol") +" ("+ currencySetupData.get("ISOcode") + ")" );
		unitPrice.set(customData.get("unit_price2"));
		quantity.click(); // to populate fields for other currency
		double discountEuroValue = discountDollarValue*Double.parseDouble(currencySetupData.get("conversionRate"));						
		double unitEuroValue = Double.parseDouble(customData.get("unit_price2"));
		double calculatedEuroValue = (unitEuroValue*quantityValue)-discountEuroValue;
		String calcRLIAmountEuroValue = String.format("%s%s", currencySetupData.get("currencySymbol"),formatter.format(calculatedEuroValue));

		// Verify currency fields with Euro conversion
		VoodooControl editUnitPrice = sugar().revLineItems.recordView.getEditField("unitPrice");
		VoodooControl editDiscountPrice = sugar().revLineItems.recordView.getEditField("discountPrice");
		VoodooControl editLikely = sugar().revLineItems.recordView.getEditField("likelyCase");
		VoodooControl editBest = sugar().revLineItems.recordView.getEditField("bestCase");
		VoodooControl editWorst = sugar().revLineItems.recordView.getEditField("worstCase");

		editUnitPrice.assertEquals(formatter.format(unitEuroValue), true);
		editDiscountPrice.assertEquals(formatter.format(discountEuroValue), true);

		// Verify calculate rli amount field is read-only
		VoodooControl calRLIAmount = sugar().revLineItems.recordView.getEditField("calcRLIAmount");
		calRLIAmount.assertAttribute("class", "edit", false);

		// Verify calculated rli amount field values (original{EURO} & converted{DOLLAR})
		// TODO: VOOD-1477
		calRLIAmount.getChildElement("label", "css", ".original").assertEquals(calcRLIAmountEuroValue, true);
		String convertedAmountDollarValue = String.format("%s%.2f", customData.get("dollar_symbol"),calculatedEuroValue/Double.parseDouble(currencySetupData.get("conversionRate")));
		calRLIAmount.getChildElement("label", "css", ".converted").assertEquals(convertedAmountDollarValue, true);

		// New values for currency fields
		editUnitPrice.set(customData.get("unit_price1"));
		editDiscountPrice.set(customData.get("discount_amount"));
		sugar().revLineItems.recordView.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify new currency fields on detail page in Euro {E} && Dollar {$}
		String finalUnitEuroVal = String.format("%s%s", currencySetupData.get("currencySymbol"),formatter.format(unitDollarValue));
		String finalUnitDollarVal = String.format("%s%.2f", customData.get("dollar_symbol"),unitDollarValue/Double.parseDouble(currencySetupData.get("conversionRate")));
		String finalDiscountAmountEuroVal = String.format("%s%s", currencySetupData.get("currencySymbol"),formatter.format(discountDollarValue));
		String finalDiscountAmountDollarVal = String.format("%s%.2f", customData.get("dollar_symbol"),discountDollarValue/Double.parseDouble(currencySetupData.get("conversionRate")));		
		String finalCalculatedAmountEuroVal = String.format("%s%s", currencySetupData.get("currencySymbol"),formatter.format(calculatedDollarValue));
		String finalCalculatedAmountDollarVal = String.format("%s%.2f", customData.get("dollar_symbol"),calculatedDollarValue/Double.parseDouble(currencySetupData.get("conversionRate")));		

		// TODO: VOOD-1477, Once resolved below code replaced by getChildElement of sugar-fields defined in CSV
		new VoodooControl("label", "css", ".fld_discount_price.detail label.original").assertEquals(finalUnitEuroVal, true);
		new VoodooControl("div", "css", ".fld_discount_price.detail div.converted").assertEquals(finalUnitDollarVal, true);

		new VoodooControl("label", "css", ".fld_discount_amount.detail label.original").assertEquals(finalDiscountAmountEuroVal, true);
		new VoodooControl("div", "css", ".fld_discount_amount.detail div.converted").assertEquals(finalDiscountAmountDollarVal, true);

		new VoodooControl("label", "css", ".fld_total_amount.detail label.original").assertEquals(finalCalculatedAmountEuroVal, true);
		new VoodooControl("div", "css", ".fld_total_amount.detail div.converted").assertEquals(finalCalculatedAmountDollarVal, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}