package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;

public class Cases_23271 extends SugarTest {
	CaseRecord myCase;

	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
	}

	/**
	 * Create Case_Verify that case can be created by edit view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23271_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Case via the GUI so that an Account is related to the Case
		// TODO VOOD-444 Support creating relationships via API
		myCase = (CaseRecord) sugar().cases.create();
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		myCase.verify();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
