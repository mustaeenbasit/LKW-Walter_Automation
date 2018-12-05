package com.sugarcrm.test.targetlists;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19467 extends SugarTest {
	StandardSubpanel targetsSubpanel;

	public void setup() throws Exception {
		sugar.targetlists.api.create();
		sugar.login();
	}

	/**
	 * Target List - Targets management_Verify that "Cancel" function in popup select targets windows works correctly.
	 * @throws Exception
	 */
	@Test
	public void TargetLists_19467_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);
		targetsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural);

		// Clicking the "+" icon on Targets Subpanel and clicking cancel.
		targetsSubpanel.addRecord();
		sugar.targets.createDrawer.getEditField("firstName").set(testName);;
		sugar.targets.createDrawer.getEditField("lastName").set(sugar.targets.getDefaultData().get("lastName"));
		sugar.targets.createDrawer.cancel();

		// Verifying that Target Record is not added into Targets Subpanel.
		Assert.assertTrue("Records not equals ZERO", targetsSubpanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}