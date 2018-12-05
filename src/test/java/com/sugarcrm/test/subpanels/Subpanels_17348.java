package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17348 extends SugarTest {
	TargetRecord testTarget;	
		
	public void setup() throws Exception {
		sugar.login();
		testTarget = (TargetRecord)sugar.targets.api.create();		
	}

	/**
	 * Test Case 17348: Verify default subpanels for module - Targets
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17348_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");				
				
		testTarget.navToRecord();
		
		// TODO VOOD-1018
		new VoodooControl("a", "css", "div.main-content div.filtered.tabs-left:nth-child(1)").assertContains("Calls", true);
		new VoodooControl("a", "css", "div.main-content div.filtered.tabs-left:nth-child(2)").assertContains("Meetings", true);
		new VoodooControl("a", "css", "div.main-content div.filtered.tabs-left:nth-child(3)").assertContains("Tasks", true);
		new VoodooControl("a", "css", "div.main-content div.filtered.tabs-left:nth-child(4)").assertContains("Notes", true);		
		new VoodooControl("a", "css", "div.main-content div.filtered.tabs-left:nth-child(5)").assertContains("Campaign Log", true);
		
		// TODO VOOD-809		
		new VoodooControl("a", "css", "div.main-content div.filtered.tabs-left:nth-child(6)").assertContains("Emails", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}