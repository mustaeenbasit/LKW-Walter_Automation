package com.sugarcrm.test.projects;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Projects_29930 extends SugarTest {
	public void setup() throws Exception {
		sugar().projects.api.create();
		sugar().login();

		// Enable Projects module from Admin > Display modules and sub panels
		sugar().admin.enableModuleDisplayViaJs(sugar().projects);
	}

	/**
	 * [Projects] Verify that additional details tool tip should not show blank
	 * 
	 * @throws Exception
	 */
	@Test
	public void Projects_29930_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Navigate to projects list view
		sugar().projects.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Open additional details tool tip
		// TODO: VOOD-1987
		new VoodooControl("img", "css", ".oddListRowS1 td:nth-child(8) img").click();

		FieldSet additionalDialogData = testData.get(testName).get(0);

		// Verify the Additional Details tool tip should show the details what are to be expected as additional. (e.g. Description)
		// TODO: VOOD-1987
		VoodooControl additionalDetailDialogBoxCtrl = new VoodooControl("div", "css", ".open.ui-dialog-content.ui-widget-content");
		additionalDetailDialogBoxCtrl.assertContains(sugar().projects.getDefaultData().get("description"), true);
		additionalDetailDialogBoxCtrl.assertContains(additionalDialogData.get("description"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}