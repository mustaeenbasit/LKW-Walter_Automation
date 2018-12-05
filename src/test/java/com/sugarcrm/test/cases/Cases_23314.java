package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;

public class Cases_23314 extends SugarTest {
	CaseRecord myCase;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myCase = (CaseRecord) sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Verify Assigned To Me filter under Cases module
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23314_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();

		// Test selecting of pre-defined My cases filter
		sugar().cases.listView.openFilterDropdown();
		sugar().cases.listView.selectFilterAssignedToMe();
		sugar().cases.listView.verifyField(1, "name", myCase.get("name"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
