package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_create extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	@Test
	public void Cases_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		CaseRecord myCase = (CaseRecord)sugar().cases.create();
		myCase.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
