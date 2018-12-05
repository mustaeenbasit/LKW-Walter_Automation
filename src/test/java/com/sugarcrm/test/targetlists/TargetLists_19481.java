package com.sugarcrm.test.targetlists;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class TargetLists_19481 extends SugarTest{
	TargetRecord mytarget;
	StandardSubpanel targetsSubpanel;

	public void setup() throws Exception {
		sugar.targetlists.api.create();
		mytarget = (TargetRecord)sugar.targets.api.create();
		sugar.login();	

		// Navigating to TargetLists Record View
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);

		// Link Targets in TargetLists Subpanel	
		targetsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural);
		targetsSubpanel.linkExistingRecord(mytarget);
	}

	/**
	 * Verify that "unlink" function in "Targets" sub-panel works correctly in Target List record view
	 */
	@Test
	public void TargetLists_19481_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Unlink Targets in TargetLists Subpanel
		targetsSubpanel.unlinkRecord(1);

		// Verifying the Target is removed from the TargetList Subpanel
		Assert.assertTrue("Target Subpanel is not Empty", targetsSubpanel.isEmpty());

		// Verifying the Targets is not deleted
		sugar.targets.navToListView();
		sugar.targets.listView.verifyField(1, "fullName", mytarget.getRecordIdentifier());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}