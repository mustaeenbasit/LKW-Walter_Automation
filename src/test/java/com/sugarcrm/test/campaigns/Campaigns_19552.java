package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19552 extends SugarTest {
	CampaignRecord myCampaign;
	FieldSet fs, currencySetup, currencyData;
	VoodooControl defaultCurrencyCtrl;

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		currencySetup = testData.get(testName+"_currency").get(0);
		myCampaign = (CampaignRecord) sugar.campaigns.api.create(fs);
		sugar.login();

		// Create test Currency
		currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", currencySetup.get("rate"));
		currencyData.put("currencySymbol", currencySetup.get("symbol"));
		currencyData.put("ISOcode", currencySetup.get("code"));
		sugar.admin.setCurrency(currencyData);

		// Goto to Campaign record
		myCampaign.navToRecord();

		// Edit Campaign Record. Add Revenue and Cost values
		sugar.campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.campaigns.editView.getEditField("impressions").set(currencySetup.get("impressions"));
		sugar.campaigns.editView.getEditField("budget").set(currencySetup.get("budget"));
		sugar.campaigns.editView.getEditField("actualCost").set(currencySetup.get("actual_cost"));
		sugar.campaigns.editView.getEditField("expectedCost").set(currencySetup.get("expected_cost"));
		sugar.campaigns.editView.getEditField("expectedRevenue").set(currencySetup.get("expected_revenue"));
		
		// Select default currency - USD
		defaultCurrencyCtrl = new VoodooControl("select", "id", "currency_id_select");
		defaultCurrencyCtrl.set(currencySetup.get("default_currency_name") +" : "+currencySetup.get("default_currency_symbol"));

		// Save
		VoodooUtils.focusDefault();
		sugar.campaigns.editView.save();
		sugar.alerts.waitForLoadingExpiration();
	}

	/**
	 * Newsletter - Verify that value for cost or revenue in campaign is converted into the 
	 * selected currency when changing the "Currency" field in edit page of campaign
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19552_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit Campaign Record
		myCampaign.navToRecord();
		sugar.campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Select Test currency
		defaultCurrencyCtrl.set(testName +" : "+currencySetup.get("symbol"));

		// Check Expected result on edit view
		sugar.campaigns.editView.getEditField("budget").assertEquals(currencySetup.get("new_budget"), true);
		sugar.campaigns.editView.getEditField("actualCost").assertEquals(currencySetup.get("new_actual_cost"), true);
		sugar.campaigns.editView.getEditField("expectedCost").assertEquals(currencySetup.get("new_expected_cost"), true);
		sugar.campaigns.editView.getEditField("expectedRevenue").assertEquals(currencySetup.get("new_expected_revenue"), true);
		// Save
		VoodooUtils.focusDefault();
		sugar.campaigns.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.alerts.waitForLoadingExpiration();

		// XPath used to check Expected result on detail view
		new VoodooControl("td", "xpath", "//*[@id='LBL_CAMPAIGN_INFORMATION']/tbody/tr[contains(.,'Budget')]/td[1]").assertContains(currencySetup.get("symbol"), true);
		new VoodooControl("td", "xpath", "//*[@id='LBL_CAMPAIGN_INFORMATION']/tbody/tr[contains(.,'Expected Cost')]/td[1]").assertContains(currencySetup.get("symbol"), true);
		new VoodooControl("td", "xpath", "//*[@id='LBL_CAMPAIGN_INFORMATION']/tbody/tr[contains(.,'Actual Cost')]/td[1]").assertContains(currencySetup.get("symbol"), true);
		new VoodooControl("td", "xpath", "//*[@id='LBL_CAMPAIGN_INFORMATION']/tbody/tr[contains(.,'Expected Revenue')]/td[1]").assertContains(currencySetup.get("symbol"), true);

		// Verify the value is converted to selected currency.
		sugar.campaigns.detailView.getDetailField("budget").assertEquals(currencySetup.get("new_budget"), true);
		sugar.campaigns.detailView.getDetailField("actualCost").assertEquals(currencySetup.get("new_actual_cost"), true);
		sugar.campaigns.detailView.getDetailField("expectedCost").assertEquals(currencySetup.get("new_expected_cost"), true);
		sugar.campaigns.detailView.getDetailField("expectedRevenue").assertEquals(currencySetup.get("new_expected_revenue"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}