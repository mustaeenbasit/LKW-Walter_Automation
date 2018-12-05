package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.test.SugarTest;

public class Bugs_create extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	@Test
	public void Bugs_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		BugRecord myBug = (BugRecord) sugar.bugs.create();
		myBug.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
