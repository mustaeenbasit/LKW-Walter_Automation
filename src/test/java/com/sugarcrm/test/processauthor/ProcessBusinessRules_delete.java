package com.sugarcrm.test.processauthor;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessBusinessRulesRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessBusinessRules_delete extends SugarTest {
	ProcessBusinessRulesRecord myBusiness;

	public void setup() throws Exception {
		myBusiness = (ProcessBusinessRulesRecord)sugar().processBusinessRules.api.create();
		sugar().login();
	}

	@Test
	public void ProcessBusinessRules_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the business rule using the UI.
		myBusiness.delete();

		// Verify the business rule was deleted
		assertEquals(VoodooUtils.contains(myBusiness.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
