package com.sugarcrm.test.targets;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.test.SugarTest;

public class Targets_21510 extends SugarTest {
	TargetRecord myTarget;

	public void setup() throws Exception {
		myTarget = (TargetRecord)sugar().targets.api.create();
		sugar().login();
	}

	/**
	 * Target - Edit Target_Verify that "Duplicate" function in the target detail view works correctly.
	 * @throws Exception
	 */
	@Test
	public void Targets_21510_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myTarget.navToRecord();
		sugar().targets.recordView.copy();
		sugar().targets.createDrawer.save();

		// Need to click on save button again to save duplicate record
		sugar().targets.createDrawer.ignoreDuplicateAndSave();

		// Verify duplicate record in list view
		sugar().targets.navToListView();
		sugar().targets.listView.verifyField(1, "fullName", myTarget.get("firstName")+" "+ myTarget.get("lastName"));
		sugar().targets.listView.verifyField(2, "fullName", myTarget.get("firstName")+" "+ myTarget.get("lastName"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
