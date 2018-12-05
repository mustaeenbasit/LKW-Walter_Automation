package com.sugarcrm.test.targets;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.TargetRecord;

public class Targets_update extends SugarTest {
	TargetRecord myTarget;

	public void setup() throws Exception {
		myTarget = (TargetRecord)sugar().targets.api.create();
		sugar().login();
	}

	@Test
	public void Targets_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("title", "Sales VP");

		// Edit the target using the UI.
		myTarget.edit(newData);

		// Verify the target was edited.
		myTarget.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
