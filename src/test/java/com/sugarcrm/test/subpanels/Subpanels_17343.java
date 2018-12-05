package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17343 extends SugarTest {
	BugRecord testBug;	
		
	public void setup() throws Exception {
		sugar.login();
		testBug = (BugRecord)sugar.bugs.api.create();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * Test Case 17343: Verify default subpanels for module - Bugs
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17343_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	
		
		StandardSubpanel callSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get("Calls");
		StandardSubpanel meetingsSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get("Meetings");
		StandardSubpanel tasksSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get("Tasks");
		StandardSubpanel notesSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get("Notes");
		StandardSubpanel docsSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get("Documents");
		StandardSubpanel contactsSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get("Contacts");
		StandardSubpanel accountsSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get("Accounts");
		StandardSubpanel casesSP = (StandardSubpanel)sugar.bugs.recordView.subpanels.get("Cases");
				
		testBug.navToRecord();
		
		callSP.assertVisible(true);
		meetingsSP.assertVisible(true);
		tasksSP.assertVisible(true);
		notesSP.assertVisible(true);
		docsSP.assertVisible(true);
		contactsSP.assertVisible(true);
		accountsSP.assertVisible(true);
		casesSP.assertVisible(true);
		// TODO VOOD-809
		new VoodooControl("a", "css", "div.main-content div.filtered.tabs-left:nth-child(9)").assertContains("Emails", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}