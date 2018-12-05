package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Cases_26636 extends SugarTest {
	CaseRecord myCase;

	public void setup() throws Exception {
		sugar().login();
		myCase = (CaseRecord) sugar().cases.api.create();
		myCase = (CaseRecord) sugar().cases.api.create();
		myCase = (CaseRecord) sugar().cases.api.create();
	}

	/**
	 * Test Case 26636: Verify cases can be search by case number under cases list view
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_26636_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Cases list view
		sugar().cases.navToListView();

		// Set filter to default value (subject and number)
		sugar().cases.listView.openFilterDropdown();
		sugar().cases.listView.selectFilterAll();

		// Get current case number and search using this number
		String caseNum = sugar().cases.listView.getDetailField(1, "caseNumber").getText();

		sugar().cases.listView.getControl("searchFilter").set(caseNum);
		sugar().alerts.waitForLoadingExpiration();

		// Verify the record matching search conditions is displayed in the list view
		sugar().cases.listView.verifyField(1, "caseNumber", caseNum);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
