package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_17569 extends SugarTest {
	LeadRecord myLead;
	FieldSet customData;
	
	public void setup() throws Exception {	
		customData = testData.get(testName).get(0);
		sugar().login();
		myLead = (LeadRecord)sugar().leads.api.create();
	}

	/**
	 * New filter cannot be created if all sub panels (Related All) are displayed
	 * @throws Exception
	 */	
	@Test
	public void Leads_17569_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		myLead.navToRecord();
		sugar().leads.recordView.getControl("dataViewButton").assertAttribute("class", "active", true);
		
		// TODO: VOOD-598.  Once it is fixed, please update the following steps: 
		//Open Related drop down, select All
		VoodooControl searchFilterDisabled = new VoodooControl("span", "css", ".search-filter.select2-container-disabled span");
		
		// TODO: VOOD-468, VOOD-486
		VoodooControl filterChoice = new VoodooControl("span", "css", "span.choice-filter-label");
		VoodooControl filterDropdown = new VoodooControl("a","css","span[data-voodoo-name='filter-filter-dropdown'] a");
		VoodooControl allRecords = new VoodooControl("div", "css", "[data-id='all_records']");
		
		sugar().leads.recordView.setRelatedSubpanelFilter(customData.get("default_filter"));
		//Verify that no Create New when it is All in Related
		searchFilterDisabled.assertVisible(true);
		filterChoice.assertEquals(customData.get("create_choice"), false);
		//Select Campaign module
		sugar().leads.recordView.setRelatedSubpanelFilter(customData.get("campaign_choice"));
		//Verify that Campaign module has no Create New either
		filterDropdown.click();
		allRecords.assertContains(customData.get("create_choice"), false);
		filterChoice.assertEquals(customData.get("create_choice"), false);
		
		//Verify that drop down is disabled and create is disabled too
		// TODO: VOOD-1499
		new VoodooControl("a", "css", ".layout_CampaignLog .btn.dropdown-toggle.disabled").assertExists(true);
		new VoodooControl("a", "css", ".layout_CampaignLog .rowaction.disabled.btn").assertExists(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}
		
	public void cleanup() throws Exception {}
}
