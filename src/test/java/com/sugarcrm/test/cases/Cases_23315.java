package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;

public class Cases_23315 extends SugarTest {
	CaseRecord myCase;

	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
		// Create a Case via the GUI so that an Account is related to the Case
		// TODO VOOD-444 Support creating relationships via API
		myCase = (CaseRecord) sugar().cases.create();
	}

	/**
	 * Verify All cases filter
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23315_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();

		// Test selecting of pre-defined My cases filter
		sugar().cases.listView.openFilterDropdown();
		sugar().cases.listView.selectFilterAssignedToMe();

		// Test selecting of All Cases filter
		sugar().cases.navToListView();
		sugar().cases.listView.openFilterDropdown();
		sugar().cases.listView.selectFilterAll();
		
		VoodooUtils.focusDefault();
		
		sugar().cases.listView.verifyField(1, "name", myCase.get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}