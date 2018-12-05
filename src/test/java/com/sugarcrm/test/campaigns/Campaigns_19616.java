package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.candybean.datasource.DataSource;

public class Campaigns_19616 extends SugarTest {
	DataSource campaignsRecord;
	CampaignRecord myCampaign;
	
	public void setup() throws Exception {
		sugar.login();
		campaignsRecord = testData.get(testName);
	}

	/**
	 * ROI report_Verify that Actual cost, expected revenue, budget are displayed in "Campaign Return On Investment" chart of campaign
	 * */
	@Test
	public void Campaigns_19616_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Campaign
		myCampaign = (CampaignRecord) sugar.campaigns.api.create();
		myCampaign.navToRecord();
		
		sugar.campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1072
		new VoodooControl("option", "css", "#campaign_type option[value='NewsLetter']").click();
		sugar.campaigns.editView.getEditField("budget").set(campaignsRecord.get(0).get("budget"));
		sugar.campaigns.editView.getEditField("expectedCost").set(campaignsRecord.get(0).get("expected_cost"));
		sugar.campaigns.editView.getEditField("actualCost").set(campaignsRecord.get(0).get("actual_cost"));
		sugar.campaigns.editView.getEditField("expectedRevenue").set(campaignsRecord.get(0).get("expected_revenue"));
		VoodooUtils.focusDefault();
		sugar.campaigns.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Go to ROI report view.		
		// TODO: VOOD-1072
		new VoodooControl("input", "id", "viewRoiButtonId").click();				
		// Verify Actual cost, expected revenue, budget are displayed in "Campaign Return On Investment" ROI chart.
		// TODO: VOOD-1072
		new VoodooControl("text", "css", "#d3_roi_details_chart .nv-title").assertContains(campaignsRecord.get(0).get("expected_result"), true);
		sugar.campaigns.detailView.assertContains(campaignsRecord.get(1).get("budget"), true);
		sugar.campaigns.detailView.assertContains(campaignsRecord.get(1).get("actual_cost"), true);
		sugar.campaigns.detailView.assertContains(campaignsRecord.get(1).get("expected_revenue"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
