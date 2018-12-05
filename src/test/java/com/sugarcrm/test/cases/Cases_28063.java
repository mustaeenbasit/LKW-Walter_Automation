package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_28063 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Knowledge Base subpanel in Cases module should reverts back to Sugar7 UI when new record is cancelled
	 * @throws Exception
	 */
	@Test
	public void Cases_28063_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to any Case record view and notice the Sugar7 UI
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		//  Go to Knowledge Base sub panel and click the "+" button to create a new record
		sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural).addRecord();
		VoodooUtils.waitForReady();

		// Cancel the create drawer
		sugar().knowledgeBase.createDrawer.cancel();

		// Verify that the view should returned to the Case record in the Sugar7 UI
		sugar().cases.recordView.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
