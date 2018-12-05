package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17347 extends SugarTest {
	LeadRecord testLead;	
		
	public void setup() throws Exception {
		sugar.login();
		testLead = (LeadRecord)sugar.leads.api.create();		
	}

	/**
	 * Test Case 17345: Verify default subpanels for module - Opportunities
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17347_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	
		
		StandardSubpanel callSP = (StandardSubpanel)sugar.leads.recordView.subpanels.get("Calls");
		StandardSubpanel meetingsSP = (StandardSubpanel)sugar.leads.recordView.subpanels.get("Meetings");
		StandardSubpanel tasksSP = (StandardSubpanel)sugar.leads.recordView.subpanels.get("Tasks");
		StandardSubpanel notesSP = (StandardSubpanel)sugar.leads.recordView.subpanels.get("Notes");		
						
		testLead.navToRecord();
		
		callSP.assertVisible(true);
		meetingsSP.assertVisible(true);
		tasksSP.assertVisible(true);
		notesSP.assertVisible(true);
				
		// TODO VOOD-809
		new VoodooControl("a", "css", "div.main-content div.filtered.tabs-left:nth-child(6)").assertContains("Emails", true);
		new VoodooControl("a", "css", "div.main-content div.filtered.tabs-left:nth-child(5)").assertContains("Campaign Log", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}