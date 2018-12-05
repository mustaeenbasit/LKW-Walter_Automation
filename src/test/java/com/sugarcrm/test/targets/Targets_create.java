package com.sugarcrm.test.targets;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.test.SugarTest;

public class Targets_create extends SugarTest{
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void Targets_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		TargetRecord myTarget = (TargetRecord)sugar().targets.create();
		myTarget.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
