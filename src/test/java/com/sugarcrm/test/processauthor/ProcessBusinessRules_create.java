package com.sugarcrm.test.processauthor;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessBusinessRulesRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessBusinessRules_create extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void ProcessBusinessRules_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ProcessBusinessRulesRecord myBusiness = (ProcessBusinessRulesRecord)sugar().processBusinessRules.create();
		myBusiness.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
