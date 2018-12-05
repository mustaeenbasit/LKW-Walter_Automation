package com.sugarcrm.test.RevenueLineItems;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_27930 extends SugarTest {
	FieldSet currencyMngt,currencyData;

	public void setup() throws Exception {
		currencyMngt = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Create Currency custom currency 'Euro'
		currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", currencyMngt.get("rate"));
		currencyData.put("currencySymbol", currencyMngt.get("symbol"));
		sugar().admin.setCurrency(currencyData);
	}	

	/**
	 * Verify that user preferred currency (not system default) is used to generate RLI record when 
	 * create new Opportunity with RLI
	 * 
	 * @throws Exception
	 */
	@Ignore("10/15/2015 Ashish Raina: Product Bug TR-10788 surfaces *After* this script completes. "
			+ "Also affects QLI_26721")
	@Test
	public void RevenueLineItems_27930_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set 'Preferred Currency' to EURO
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("tab4").click();
		sugar().users.userPref.getControl("advanced_preferedCurrency").set((testName) +" : "+currencyMngt.get("symbol"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Create opportunity with one RLI and hit 'Save' and navigate to the created RLI record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().opportunities.createDrawer.save();
		sugar().revLineItems.listView.clickRecord(1);

		// Verify that 'likely' RLI amount is created using user preferred currency (EURO)
		sugar().opportunities.recordView.getDetailField("oppAmount").assertContains(currencyMngt.get("symbol")+""+currencyMngt.get("convertedValue"), true);

		// Verify that 'best' RLI amount is created using user preferred currency (EURO)
		sugar().opportunities.recordView.getDetailField("bestCase").assertContains(currencyMngt.get("symbol")+""+currencyMngt.get("convertedValue"), true);

		// Verify that 'worst' RLI amount is created using user preferred currency (EURO)
		sugar().opportunities.recordView.getDetailField("worstCase").assertContains(currencyMngt.get("symbol")+""+currencyMngt.get("convertedValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}