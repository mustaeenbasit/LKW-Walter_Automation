package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_17566 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify the Campaign Log Subpanel can be collapsed and expended in Leads Data View
	 * @throws Exception
	 */
	@Test
	public void Leads_17566_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource headerDS = testData.get(testName + "_headers");
		FieldSet customData = testData.get(testName).get(0);
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showDataView();

		// Open Related drop down, select All
		sugar().leads.recordView.setRelatedSubpanelFilter("All");

		// Expand Campaign Log Sub panel
		// TODO: VOOD-1344 and VOOD-1499
		VoodooControl campaignLogSubpanel = new VoodooControl("div", "css", "div[data-voodoo-name="
				+ "'CampaignLog'] .subpanel-header");
		campaignLogSubpanel.click();
		VoodooUtils.waitForReady();
		
		// Verify that subpanel is expanded
		VoodooControl subPanelStatus = new VoodooControl("li", "css", "div[data-voodoo-name="
				+ "'CampaignLog'] ul li");
		Boolean isExpanded = subPanelStatus.getAttribute("class").contains("closed");
		Assert.assertFalse("Subpanel is not expanded when it should", isExpanded);
				
		int size = headerDS.size();
		
		// Verify Subpanel column headers are shown
		// TODO: VOOD-1344
		for(int i = 0 ; i < size ; i++) 
			new VoodooControl("span", "css", ".layout_CampaignLog th.sorting.orderBy" + headerDS.get(i).get("data_field_name")
					+ " span").assertContains(headerDS.get(i).get("header_name"), true);
		
		// Collapse Campaign Log Sub panel
		campaignLogSubpanel.click();
		VoodooUtils.waitForReady();
		
		// Verify that subpanel is collapsed
		Boolean isCollapsed = subPanelStatus.getAttribute("class").contains("closed");
		Assert.assertTrue("Subpanel is not collapsed when it should", isCollapsed);
		
		// Verify that subpanel header is shown for 'Campaign Log'
		new VoodooControl("h4", "css", "div[data-voodoo-name='CampaignLog'] div h4").assertEquals
			(customData.get("subpanelHeaderText"), true);
		
		// Verify Subpanel column headers are not shown
		for(int i = 0 ; i < size ; i++) 
			new VoodooControl("span", "css", ".layout_CampaignLog th.sorting.orderBy" + headerDS.get(i).get("data_field_name")
					+ " span").assertContains(headerDS.get(i).get("header_name"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}