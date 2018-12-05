package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Bugs_17334 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * Verify the long subject name should be truncated with "..." accompanied by a tooltip displaying the full name in the header row
	 * @throws Exception
	 */
	@Test
	public void Bugs_17334_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Bugs listview -> Create -> long subject name -> save
		String name = testData.get(testName).get(0).get("name");
		sugar.bugs.navToListView();
		sugar.bugs.listView.create();
		sugar.bugs.createDrawer.getEditField("name").set(name);
		sugar.bugs.createDrawer.save();
		if (sugar.alerts.getSuccess().queryVisible()) 
			sugar.alerts.getSuccess().closeAlert();

		// Verify the long subject name should be truncated with "..." & tooltip verified by attribute ([data-original-title])
		VoodooControl nameCtrl = sugar.bugs.listView.getDetailField(1, "name");
		nameCtrl.hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(name, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}