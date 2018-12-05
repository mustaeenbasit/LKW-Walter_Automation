package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_18358 extends SugarTest {
	FieldSet relatedFilter = new FieldSet();

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify user can't switch default filter in Campaign Log subpanel of the Leads record 
	 * @throws Exception
	 */
	@Test
	public void Leads_18358_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		relatedFilter = testData.get(testName).get(0);
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Set the value of Related filter to "Campaign Log"
		// TODO: VOOD-468 - need defined control for the related modules dropdown list on record view for filter subpanels
		new VoodooSelect("span", "css", ".related-filter .select2-choice-type").set(relatedFilter.get("relatedFilterCampaignLog"));
		VoodooUtils.waitForReady();
		new VoodooControl("span", "css", "span[data-voodoo-name='filter-filter-dropdown'] .select2-choice-type").click();
		VoodooUtils.waitForReady();

		VoodooControl defaultCampaignLogFilter = new VoodooControl("li", "css", ".select2-results li:nth-child(1)");

		// Assert that the default filter option for Campaign log filter is "All Campaign Log"
		defaultCampaignLogFilter.assertVisible(true);
		defaultCampaignLogFilter.assertContains(relatedFilter.get("filterByCampaign"), true);

		// Assert that there is only one filter option for Campaign log filter
		new VoodooControl("li", "css", ".select2-results li:nth-child(2)").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}