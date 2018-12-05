package com.sugarcrm.test.leads;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Leads_17567 extends SugarTest {
	LeadRecord myLead;
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myLead = (LeadRecord)sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that the default filter is All in Leads sub panel
	 */
	@Test
	public void Leads_17567_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-468
		VoodooControl filter = new VoodooControl("span", "css", "span.choice-filter-label");
		
		myLead.navToRecord();
		
		// Verify that by default "All" is the default filter
		sugar().leads.recordView.getControl("relatedSubpanelChoice").assertEquals(customData.get("default_filter"), true);
		filter.assertEquals(customData.get("default_filter_label"), true);

		// In "Related Subpanels", select one of sidecar modules
		sugar().leads.recordView.setRelatedSubpanelFilter(customData.get("sidecar"));

		// Verify that in "Recent" filter, it shows "Tasks"
		sugar().leads.recordView.getControl("relatedSubpanelChoice").assertEquals(customData.get("sidecar"), true);
		filter.assertEquals(customData.get("create_filter"), true);

		// In "Related Subpanels", select one of bwc modules
		sugar().leads.recordView.setRelatedSubpanelFilter(customData.get("bwc"));

		// Verify that in related label, it shows "Camapign Logs" and in Filter label it shows "All Campaign Log"
		sugar().leads.recordView.getControl("relatedSubpanelChoice").assertEquals(customData.get("bwc"), true);
		filter.assertEquals(customData.get("all_campaign_log"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}