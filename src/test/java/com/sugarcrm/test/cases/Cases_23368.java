package com.sugarcrm.test.cases;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23368 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Log Call_Verify that call is not scheduled in "Calls" sub-panel when using "Cancel" function.
	 * @throws Exception
	 */
	@Test
	public void Cases_23368_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Cases record view and Click "Create" button in "Calls" sub-panel
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		StandardSubpanel callsSubpanel = sugar().cases.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.addRecord();

		// Fill Subject of Call
		sugar().calls.createDrawer.getEditField("name").set(sugar().calls.getDefaultData().get("name"));

		// Cancel call creation for selected Case
		sugar().calls.createDrawer.cancel();

		VoodooUtils.waitForReady();

		// Verify Case record view is opened
		sugar().cases.recordView.assertVisible(true);
		sugar().cases.recordView.getControl("moduleIDLabel").hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(sugar().cases.moduleNameSingular, true);

		// Verify that no new call for the case displayed in "Calls" sub-panel
		Assert.assertTrue("The subpanel is not empty", callsSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
