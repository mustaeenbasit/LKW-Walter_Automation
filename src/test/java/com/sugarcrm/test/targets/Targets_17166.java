package com.sugarcrm.test.targets;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Targets_17166 extends SugarTest {
	public void setup() throws Exception {
		sugar().targets.api.create();
		sugar().login();
	}

	/**
	 * Verify duplicate check on office phone while creating a new target
	 * @throws Exception
	 */
	@Test
	public void Targets_17166_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a new Target Record using same phone number field as we have for first record
		sugar().targets.navToListView();
		sugar().targets.listView.create();
		sugar().targets.createDrawer.showMore();
		sugar().targets.createDrawer.getEditField("lastName").set(sugar().targets.getDefaultData().get("lastName"));
		sugar().targets.createDrawer.getEditField("phoneWork").set(sugar().targets.getDefaultData().get("phoneWork"));
		sugar().targets.createDrawer.save();

		// Verify "Ignore Duplicate and Save" button exists
		sugar().targets.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);

		// TODO: VOOD-513
		// Verify target display
		new VoodooControl("div", "css", "#drawers .list.fld_phone_work div").assertContains(sugar().targets.getDefaultData().get("phoneWork"), true);
		sugar().targets.createDrawer.ignoreDuplicateAndSave();

		VoodooUtils.voodoo.log.info(testName + " complete. ");
	}

	public void cleanup() throws Exception {}
}
