package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19466 extends SugarTest {
	StandardSubpanel targetsSubpanel;
	TargetRecord myTarget;

	public void setup() throws Exception {
		sugar.targetlists.api.create();
		myTarget = (TargetRecord) sugar.targets.api.create();
		sugar.login();
	}

	/**
	 * Target List - Targets management_Verify that "Select" targets function in the "Targets" sub-panel works correctly.
	 * @throws Exception
	 */
	@Test
	public void TargetLists_19466_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);
		targetsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural);

		// Linking existing Target record to Targets Subpanel
		targetsSubpanel.linkExistingRecord(myTarget);

		// Verifying the TargetRecord has been Linked to Targets Subpanel successfully.
		targetsSubpanel.assertContains(myTarget.get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}