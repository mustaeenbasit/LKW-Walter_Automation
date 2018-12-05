package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_23290 extends SugarTest {
	DataSource ds;
	CaseRecord caseRecord;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();
	}

	/*
	* Test Case 23290: Edit Case_Verify that case is edited when entering special characters in several fields
	* (Such as entering "\","#" in "Subject" of the case) for the "Edit" function.
	*/
	@Test
	public void Cases_23290_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		for (FieldSet caseData : ds) {
			// Create a case
			caseRecord = (CaseRecord) sugar().cases.create();
			// Update the case to a custom data
			caseRecord.edit(caseData);
			// Verify the updated case
			caseRecord.verify(caseData);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
