package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import static org.junit.Assert.assertEquals;

public class Cases_delete extends SugarTest {
	CaseRecord myCase;

	public void setup() throws Exception {
		myCase = (CaseRecord)sugar().cases.api.create();
		sugar().login();
	}

	@Test
	public void Cases_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the account using the UI.
		myCase.delete();

		// Verify the account was deleted.
		sugar().cases.navToListView();
		assertEquals(VoodooUtils.contains(myCase.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
