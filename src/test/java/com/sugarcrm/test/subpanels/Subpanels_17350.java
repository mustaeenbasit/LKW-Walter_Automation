package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17350 extends SugarTest {
	TargetListRecord testTL;	
		
	public void setup() throws Exception {
		sugar.login();
		testTL = (TargetListRecord)sugar.targetlists.api.create();		
	}

	/**
	 * Test Case 17350: Verify default subpanels for module - Target Lists
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17350_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");			
		
		StandardSubpanel contactsSP = (StandardSubpanel)sugar.targetlists.recordView.subpanels.get("Contacts");
		StandardSubpanel leadsSP = (StandardSubpanel)sugar.targetlists.recordView.subpanels.get("Leads");
		StandardSubpanel usersSP = (StandardSubpanel)sugar.targetlists.recordView.subpanels.get("Users");
		StandardSubpanel accountsSP = (StandardSubpanel)sugar.targetlists.recordView.subpanels.get("Accounts");
		StandardSubpanel campaignsSP = (StandardSubpanel)sugar.targetlists.recordView.subpanels.get("Campaigns");				
				
		testTL.navToRecord();
		
		// TODO VOOD-1021
		new VoodooControl("a", "css", "div.main-content div.filtered.tabs-left:nth-child(1)").assertContains("Targets", true);
		
		contactsSP.assertVisible(true);	
		leadsSP.assertVisible(true);	
		usersSP.assertVisible(true);	
		accountsSP.assertVisible(true);	
		campaignsSP.assertVisible(true);	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}