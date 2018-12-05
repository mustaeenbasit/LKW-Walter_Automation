package com.sugarcrm.test.RevenueLineItems;

import java.text.DecimalFormat;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_17699 extends SugarTest {
	FieldSet currencySetupData, customData;

	public void setup() throws Exception {
		currencySetupData = testData.get(testName+"_currency").get(0);
		customData = testData.get(testName).get(0);
		sugar().login();

		// create new currency i.e. EURO
		sugar().admin.setCurrency(currencySetupData);
	}

	/**
	 * Verify that currency for readonly fields is updated after another currency is selected in one of the editable fields in Revenue Line Items create view. 
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17699_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		DecimalFormat formatter = new DecimalFormat("##,###.00");

		// Create RLI and enter some currency values
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.showMore();

		VoodooControl quantity = sugar().revLineItems.createDrawer.getEditField("quantity");
		VoodooControl unitPrice = sugar().revLineItems.createDrawer.getEditField("unitPrice");
		VoodooControl discountPrice = sugar().revLineItems.createDrawer.getEditField("discountPrice");
		VoodooControl likelyCase = sugar().revLineItems.createDrawer.getEditField("likelyCase");
		VoodooControl bestCase = sugar().revLineItems.createDrawer.getEditField("bestCase");
		VoodooControl worstCase = sugar().revLineItems.createDrawer.getEditField("worstCase");
		VoodooControl calcRLIAmount = sugar().revLineItems.createDrawer.getEditField("calcRLIAmount");

		double quantityValue = Integer.parseInt(customData.get("quantity"));
		double discountDollarValue = Integer.parseInt(customData.get("discountPrice"));
		double unitDollarValue = Integer.parseInt(customData.get("unitPrice"));
		double calculatedDollarValue = (unitDollarValue*quantityValue)-discountDollarValue;
		String calcRLIAmountDollarValue = String.format("%s%s", customData.get("dollar_symbol"),formatter.format(calculatedDollarValue));

		// Verify all fields USD currency
		quantity.set(customData.get("quantity"));
		discountPrice.set(customData.get("discountPrice"));
		unitPrice.set(customData.get("unitPrice"));
		likelyCase.click();
		likelyCase.assertEquals(formatter.format(calculatedDollarValue), true);
		unitPrice.assertEquals(formatter.format(unitDollarValue), true);
		bestCase.assertEquals(formatter.format(calculatedDollarValue), true);
		worstCase.assertEquals(formatter.format(calculatedDollarValue), true);

		// Verify calculate rli amount field is read-only
		calcRLIAmount.assertAttribute("class", "edit", false);
		calcRLIAmount.assertEquals(calcRLIAmountDollarValue, true);

		// Click on currency field to open drop down field, switch to EUR from USD.
		// VOOD-983
		new VoodooSelect("span", "css", "[data-fieldname='discount_amount'] .fld_currency_id .select2-chosen").set(currencySetupData.get("currencySymbol") +" ("+ currencySetupData.get("ISOcode") + ")" );

		double discountEuroValue = discountDollarValue*Double.parseDouble(currencySetupData.get("conversionRate"));		
		double calculatedEuroValue = (((unitDollarValue*quantityValue)-discountDollarValue)*Double.parseDouble(currencySetupData.get("conversionRate")));		
		double unitEuroValue = (unitDollarValue*Double.parseDouble(currencySetupData.get("conversionRate")));		
		String calcRLIAmountEuroValue = String.format("%s%s", currencySetupData.get("currencySymbol"),formatter.format(calculatedEuroValue));

		// Verify currency values updated accordingly
		discountPrice.assertEquals(formatter.format(discountEuroValue), true);
		likelyCase.assertEquals(formatter.format(calculatedEuroValue), true);
		bestCase.assertEquals(formatter.format(calculatedEuroValue), true);
		worstCase.assertEquals(formatter.format(calculatedEuroValue), true);
		unitPrice.assertEquals(formatter.format(unitEuroValue), true);

		// Verify calculated rli amount field values (original{EURO} & converted{DOLLAR})
		// TODO: VOOD-1477
		calcRLIAmount.getChildElement("label", "css", ".original").assertEquals(calcRLIAmountEuroValue, true);
		calcRLIAmount.getChildElement("label", "css", ".converted").assertEquals(calcRLIAmountDollarValue, true);

		sugar().revLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}