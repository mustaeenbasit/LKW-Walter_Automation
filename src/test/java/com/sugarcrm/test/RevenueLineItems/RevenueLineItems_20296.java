package com.sugarcrm.test.RevenueLineItems;

import java.text.DecimalFormat;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_20296 extends SugarTest {
	FieldSet currencySetupData = new FieldSet();

	public void setup() throws Exception {
		currencySetupData = testData.get(testName+"_currency").get(0);
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();

		// create new currency i.e. EURO
		sugar().admin.setCurrency(currencySetupData);

		// TODO: VOOD-444 - Once resolved account relation with OPP via API
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.listView.saveRecord(1);
	}

	/**
	 * Verify that if currency marked as Inactive user is not able to create records using this currency
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_20296_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet currencyVal = testData.get(testName).get(0);
		sugar().navbar.selectMenuItem(sugar().revLineItems, "create"+sugar().revLineItems.moduleNameSingular);

		// Create RLI
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().revLineItems.getDefaultData().get("relOpportunityName"));

		// Verify EURO currency in currency dropdown field
		// TODO: VOOD-983
		VoodooControl likelyCurrencyDropdown = new VoodooControl("a", "css", "span[data-voodoo-name='likely_case'] .currency.edit.fld_currency_id div a");
		likelyCurrencyDropdown.click();
		VoodooControl dropdown = new VoodooControl("ul", "css", ".select2-drop-active ul");
		VoodooControl euroCurrency = new VoodooControl("div", "css", dropdown.getHookString()+" li:nth-of-type(2) div");
		euroCurrency.assertEquals(currencyVal.get("euro_currency"), true);
		euroCurrency.click();
		sugar().revLineItems.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		VoodooControl bestCase = sugar().revLineItems.listView.getDetailField(1, "bestCase");
		VoodooControl worstCase = sugar().revLineItems.listView.getDetailField(1, "worstCase");
		VoodooControl likelyCase = sugar().revLineItems.listView.getDetailField(1, "likelyCase");

		bestCase.scrollIntoViewIfNeeded(false);
		DecimalFormat formatter = new DecimalFormat("##,###.00");
		double calculatedEuroValue = Double.parseDouble(sugar().revLineItems.getDefaultData().get("likelyCase"))*Double.parseDouble(currencySetupData.get("conversionRate"));
		String calcRLIAmountEuroValue = String.format("%s%s", currencySetupData.get("currencySymbol"),formatter.format(calculatedEuroValue));

		// Verify EURO currency symbol with calculated value in currency fields (likely, worst, best)
		likelyCase.assertContains(calcRLIAmountEuroValue, true);
		worstCase.assertContains(calcRLIAmountEuroValue, true);
		bestCase.assertContains(calcRLIAmountEuroValue, true);

		// inactive currency i.e. EURO
		sugar().admin.inactiveCurrency(currencySetupData.get("currencyName"));
		sugar().navbar.selectMenuItem(sugar().revLineItems, "create"+sugar().revLineItems.moduleNameSingular);
		likelyCurrencyDropdown.click();

		// Verify only DOLLAR currency in currency dropdown field
		VoodooControl dollarCurrency = new VoodooControl("div", "css", dropdown.getHookString()+" li div");
		dollarCurrency.assertEquals(currencyVal.get("dollar_currency"), true);

		// Verify no EURO currency after inactive
		euroCurrency.assertVisible(false);
		dollarCurrency.click(); // to close dropdown
		sugar().revLineItems.createDrawer.cancel();

		bestCase.scrollIntoViewIfNeeded(false);

		// Verify no EURO currency symbol in currency fields (likely, worst, best)
		String calcRLIAmountDollarValue = String.format("%s%s", currencyVal.get("dollar_symbol"),formatter.format(calculatedEuroValue));
		likelyCase.assertContains(calcRLIAmountDollarValue, true);
		worstCase.assertContains(calcRLIAmountDollarValue, true);
		bestCase.assertContains(calcRLIAmountDollarValue, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}