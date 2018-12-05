package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_18357 extends SugarTest {
	DataSource campaignInfo = new DataSource();
	FieldSet relatedFilter = new FieldSet();
	LeadRecord leadRec;

	public void setup() throws Exception {
		campaignInfo = testData.get(testName + "_createCampaign");

		// Create Target List record
		TargetListRecord targetList = (TargetListRecord) sugar().targetlists.api.create();

		// Create Lead Record
		leadRec = (LeadRecord)sugar().leads.api.create();

		// Create 2 campaign records
		sugar().campaigns.api.create(campaignInfo);

		sugar().login();

		targetList.navToRecord();

		// Linking the Lead record to the TargetList record
		sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural).linkExistingRecord(leadRec);
		sugar().alerts.getSuccess().closeAlert();
		VoodooControl markAsSentButton = new VoodooControl("a", "id", "mark_as_sent_button");

		// Generate records in the Campaign log subpanel
		for(int rec = 1 ; rec <= 2 ; rec++){
			sugar().campaigns.navToListView();
			sugar().campaigns.listView.clickRecord(rec);

			VoodooUtils.focusFrame("bwc-frame");

			// Link Existing Record to select the target list in Campaign 1.
			// TODO: VOOD-1028 - Need library support to Link Existing Record in Campaign detail view
			new VoodooControl("div", "id", "list_subpanel_prospectlists").scrollIntoViewIfNeeded(false);
			new VoodooControl("span", "css", "#list_subpanel_prospectlists .ab").click();
			new VoodooControl("a", "id","prospect_list_campaigns_select_button").click();

			// Moving focus to pop-up window to select target lists i.e to be linked with campaign
			VoodooUtils.focusWindow(1);
			new VoodooControl("input", "id", "massall_top").click();
			new VoodooControl("input", "id", "MassUpdate_select_button").click();

			// Bringing the focus back to the campaign detail view
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusFrame("bwc-frame");
			VoodooUtils.waitForReady();
			sugar().campaigns.detailView.openPrimaryButtonDropdown();

			markAsSentButton.click();
			VoodooUtils.waitForReady();

			VoodooUtils.focusDefault();
		}
	}

	/**
	 * Verify only filter search is allowed in a bwc sub panel  
	 * @throws Exception
	 */
	@Test
	public void Leads_18357_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		relatedFilter = testData.get(testName + "_filterData").get(0);
		leadRec.navToRecord();
		
		// Expand Campaign Log Sub panel
		// TODO: VOOD-1344 and VOOD-1499
		VoodooControl campaignLogSubpanel = new VoodooControl("div", "css", "div[data-voodoo-name="
				+ "'CampaignLog'] .subpanel-header");
		campaignLogSubpanel.click();
		VoodooUtils.waitForReady();
		
		// Verifying that records are visible in Campaign Log Sub panel
		Boolean recordsInSubpanel = new VoodooControl("li", "css", "div[data-voodoo-name='CampaignLog']"
				+ " ul li").getAttribute("class").contains("empty");
		Assert.assertFalse("No records in campaign log subpanel when it should", recordsInSubpanel);
		
		// Set the value of Related filter to "Campaign Log"
		sugar().leads.recordView.setRelatedSubpanelFilter(relatedFilter.get("relatedFilterCampaignLog"));
		VoodooUtils.waitForReady();

		// TODO: VOOD-486 - need defined control for filter dropdown on the record view
		new VoodooControl("span", "css", "span[data-voodoo-name='filter-filter-dropdown'] .select2-"
				+ "choice-type").click();
		VoodooUtils.waitForReady();

		VoodooControl defaultCampaignLogFilter = new VoodooControl("li", "css", ".select2-results"
				+ " li:nth-child(1)");

		// Assert that the default filter option for Campaign log filter is "All Campaign Log"
		defaultCampaignLogFilter.assertVisible(true);
		defaultCampaignLogFilter.assertContains(relatedFilter.get("relatedFilterOption"), true);

		// Assert that there is only one filter option for Campaign log filter
		new VoodooControl("li", "css", ".select2-results li:nth-child(2)").assertExists(false);

		// Click on the Option "All Campaign Log" to close the filter
		new VoodooControl("div", "css", ".select2-results li:nth-child(1) div[data-id='all_records']").click();

		// Assert that only Quick Text Search in the Campaign log works
		for (int i = 0 ; i <= 1 ; i++){	
			sugar().leads.recordView.setSearchString(campaignInfo.get(i).get("name"));
			VoodooUtils.waitForReady();

			new VoodooControl("td", "css", ".layout_CampaignLog .single:nth-child(1) td:nth-child(2)")
				.assertContains(campaignInfo.get(i).get("name"), true);
			new VoodooControl("i", "css", ".filter-view .fa-times").click();
			VoodooUtils.waitForReady();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}