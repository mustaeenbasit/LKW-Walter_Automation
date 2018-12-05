package com.sugarcrm.test.targets;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import static org.junit.Assert.assertEquals;

public class Targets_delete extends SugarTest {
	TargetRecord myTarget;

	public void setup() throws Exception {
		myTarget = (TargetRecord)sugar().targets.api.create();
		sugar().login();
	}

	@Test
	public void Targets_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the target using the UI.
		myTarget.delete();

		// Verify the target was deleted.
		sugar().targets.navToListView();
		assertEquals(VoodooUtils.contains(myTarget.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
