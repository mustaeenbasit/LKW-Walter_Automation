package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Bugs_29818 extends SugarTest {

	public void setup() throws Exception {
		sugar().bugs.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
	}

	/**
	 * Verify that create article button is clickable and edit View of Article is visible.
	 * @throws Exception
	 */
	@Test
	public void Bugs_29818_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().bugs.navToListView();
		sugar().bugs.listView.clickRecord(1);

		// Click Primary Action Button near Edit Button
		sugar().bugs.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-695
		// Click "Create Article" option from the Primary Action Dropdown
		new VoodooControl("a", "css", ".detail .fld_create_button a").click();

		// Verify that Edit View of Article is getting displayed
		sugar().knowledgeBase.createDrawer.assertVisible(true);
		sugar().knowledgeBase.createDrawer.getEditField("name").assertEquals(sugar().bugs.getDefaultData().get("name"), true);

		// Cancel the Create Article Drawer
		sugar().knowledgeBase.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}