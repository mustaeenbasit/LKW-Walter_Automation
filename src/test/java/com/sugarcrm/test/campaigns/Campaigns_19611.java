package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Campaigns_19611 extends SugarTest {
	DataSource campaignsRecord;
	CampaignRecord myCampaign;
	
	public void setup() throws Exception {
		sugar.login();
		
		campaignsRecord = testData.get(testName);
		
		// Create Newsletter record
		FieldSet myData = new FieldSet();
		myData.put("type", campaignsRecord.get(0).get("type"));
		myCampaign = (CampaignRecord) sugar.campaigns.create(myData);		
	}

	/**
	 * Newsletter - View ROI_Verify that ROI report can be viewed.
	 * */
	@Test
	public void Campaigns_19611_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// All BWC modules need to make sure that focusDefault() is selected before navToRecord
		VoodooUtils.focusDefault();
		myCampaign.navToRecord();
		
		sugar.campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1072
		new VoodooControl("option", "css", "#campaign_type option[value='NewsLetter']").click();
		new VoodooControl("input", "id", "budget").set(campaignsRecord.get(0).get("budget"));
		new VoodooControl("input", "id", "expected_cost").set(campaignsRecord.get(0).get("expected_cost"));
		new VoodooControl("input", "id", "actual_cost").set(campaignsRecord.get(0).get("actual_cost"));
		new VoodooControl("input", "id", "expected_revenue").set(campaignsRecord.get(0).get("expected_revenue"));
		VoodooUtils.focusDefault();
		sugar.campaigns.editView.save();
		
		// Go to ROI report view.
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1072
		new VoodooControl("input", "id", "viewRoiButtonId").click();
				
		// Verify "Return On Investment" is displayed as ROI chart title.
		// TODO: VOOD-1072
		new VoodooControl("text", "css", "#d3_roi_details_chart .nv-title").assertContains(campaignsRecord.get(0).get("expected_result"), true);
				
		// Verify ROI report is displayed on the page.
		new VoodooControl("slot", "css", ".detail.view tr:nth-child(1) td:nth-child(2) slot").assertContains(sugar.campaigns.defaultData.get("name"), true);
		new VoodooControl("slot", "css", ".detail.view tr:nth-child(5) td:nth-child(2) slot").assertContains(campaignsRecord.get(0).get("type"), true);
		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}