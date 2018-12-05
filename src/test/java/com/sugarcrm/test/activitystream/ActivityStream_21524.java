package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_21524 extends SugarTest {
	public void setup() throws Exception {
		sugar.targets.api.create();
		sugar.targetlists.api.create();
		
		// Login as QAUser
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * Disable Activities Stream sub panel in Targets and TargetLists 
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_21524_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customFS = testData.get(testName).get(0);
		sugar.targets.navToListView();
		
		sugar.accounts.listView.showActivityStream();

		// Remove focus from ActivityStream button
		new VoodooControl("body", "css", "body").click();
		
		sugar.accounts.listView.getControl("activityStream").hover();
		VoodooUtils.waitForReady();
		
		// Verify that A tool tip message saying: Activity Stream is not enabled. 
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(customFS.get("toolTipMsg"), true);
		
		// Go to recordView
		sugar.targets.listView.clickRecord(1);
		sugar.accounts.recordView.getControl("activityStreamButton").hover();
		VoodooUtils.waitForReady();
		
		// Verify that A tool tip message saying: Activity Stream is not enabled.
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(customFS.get("toolTipMsg"), true);
		
		// Go to target list view
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.getControl("activityStream").hover();
		VoodooUtils.waitForReady();

		// Verify that A tool tip message saying: Activity Stream is not enabled. 
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		sugar.targetlists.listView.getControl("activityStream").assertAttribute("data-original-title", customFS.get("toolTipMsg"));
		
		// Go to recordView
		sugar.targetlists.listView.clickRecord(1);
		sugar.targetlists.recordView.getControl("activityStreamButton").hover();
		VoodooUtils.waitForReady();
		
		// Verify that A tool tip message saying: Activity Stream is not enabled. 
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(customFS.get("toolTipMsg"), true);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}