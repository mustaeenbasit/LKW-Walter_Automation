package com.sugarcrm.test.subpanels;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17345 extends SugarTest {
	OpportunityRecord testOpp;	
		
	public void setup() throws Exception {
		sugar.login();
		testOpp = (OpportunityRecord)sugar.opportunities.api.create();		
	}

	/**
	 * Test Case 17345: Verify default subpanels for module - Opportunities
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17345_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	
		
		StandardSubpanel callSP = (StandardSubpanel)sugar.opportunities.recordView.subpanels.get("Calls");
		StandardSubpanel meetingsSP = (StandardSubpanel)sugar.opportunities.recordView.subpanels.get("Meetings");
		StandardSubpanel tasksSP = (StandardSubpanel)sugar.opportunities.recordView.subpanels.get("Tasks");
		StandardSubpanel notesSP = (StandardSubpanel)sugar.opportunities.recordView.subpanels.get("Notes");
		StandardSubpanel rliSP = (StandardSubpanel)sugar.opportunities.recordView.subpanels.get("RevenueLineItems");
		StandardSubpanel quotesSP = (StandardSubpanel)sugar.opportunities.recordView.subpanels.get("Quotes");		
		StandardSubpanel contactsSP = (StandardSubpanel)sugar.opportunities.recordView.subpanels.get("Contacts");
		StandardSubpanel leadsSP = (StandardSubpanel)sugar.opportunities.recordView.subpanels.get("Leads");
		StandardSubpanel docsSP = (StandardSubpanel)sugar.opportunities.recordView.subpanels.get("Documents");		
				
		testOpp.navToRecord();
		
		callSP.assertVisible(true);
		meetingsSP.assertVisible(true);
		tasksSP.assertVisible(true);
		notesSP.assertVisible(true);
		rliSP.assertVisible(true);	
		quotesSP.assertVisible(true);
		contactsSP.assertVisible(true);
		leadsSP.assertVisible(true);
		docsSP.assertVisible(true);		
				
		// TODO VOOD-809
		new VoodooControl("a", "css", "div.main-content div.filtered.tabs-left:nth-child(10)").assertContains("Emails", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}