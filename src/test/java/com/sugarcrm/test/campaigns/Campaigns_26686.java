package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Campaigns_26686 extends SugarTest {
	FieldSet campaignsRecord, currencyRecord, currencyData;
	CampaignRecord myCampaign;
	
	public void setup() throws Exception {
		campaignsRecord = testData.get(testName).get(0);
		currencyRecord = testData.get(testName+"_currency").get(0);
		myCampaign = (CampaignRecord) sugar.campaigns.api.create();
		sugar.login();

		// Add new Currency
		currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", currencyRecord.get("conversion_rate"));
		currencyData.put("currencySymbol", currencyRecord.get("currency_symbol"));
		currencyData.put("ISOcode", currencyRecord.get("iso_code"));
		sugar.admin.setCurrency(currencyData);
		
		// Set custom currency as default currency on profile
		FieldSet userPrefs = new FieldSet();
		userPrefs.put("advanced_preferedCurrency", testName +" : "+currencyRecord.get("currency_symbol"));
		sugar.users.setPrefs(userPrefs);
	}

	/**
	 * Verify that "View ROI" shows correct currency in "Cost Per Impression" & "Cost Per Click Through"
	 * */
	@Test
	public void Campaigns_26686_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Campaign
		myCampaign.navToRecord();
		sugar.campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1072
		sugar.campaigns.editView.getEditField("type").set(campaignsRecord.get("type"));
		new VoodooControl("select", "id", "currency_id_select").set(testName +" : "+currencyRecord.get("currency_symbol"));
		sugar.campaigns.editView.getEditField("budget").set(campaignsRecord.get("budget"));
		sugar.campaigns.editView.getEditField("expectedCost").set(campaignsRecord.get("expected_cost"));
		sugar.campaigns.editView.getEditField("actualCost").set(campaignsRecord.get("actual_cost"));
		sugar.campaigns.editView.getEditField("expectedRevenue").set(campaignsRecord.get("expected_revenue"));
		sugar.campaigns.editView.getEditField("impressions").set(campaignsRecord.get("impressions"));
		VoodooUtils.focusDefault();
		sugar.campaigns.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Go to ROI report view.		
		new VoodooControl("input", "id", "viewRoiButtonId").click();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that data is correct on the ROI page.
		// TODO: VOOD-1072
		VoodooUtils.voodoo.log.info(new VoodooControl("td", "css", "#contentTable tbody tr td table.detail.view tbody tr:nth-child(10) td:nth-child(3)").getText() + " complete.");
		new VoodooControl("td", "css", "table.detail.view tr:nth-child(10) > td:nth-child(3)").assertContains(campaignsRecord.get("res1"), true);
		new VoodooControl("td", "css", "table.detail.view tr:nth-child(10) > td:nth-child(4)").assertContains(campaignsRecord.get("res2"), true);
		new VoodooControl("td", "css", "table.detail.view tr:nth-child(11) > td:nth-child(3)").assertContains(campaignsRecord.get("res3"), true);
		new VoodooControl("td", "css", "table.detail.view tr:nth-child(11) > td:nth-child(4)").assertContains(campaignsRecord.get("res4"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");		
	}

	public void cleanup() throws Exception {}
}