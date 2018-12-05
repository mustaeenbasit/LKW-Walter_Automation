package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_23289 extends SugarTest {
	FieldSet caseData;
	DataSource ds;
	CaseRecord caseRecord;

	public void setup() throws Exception {
		ds = testData.get(testName);
		caseData = sugar().accounts.api.create();
		sugar().login();
	}

	/*
	 * Test Case 23289: Create Case_Verify that case is created when entering special characters in several fields
	 * (such as entering "\","#") for the "Create Case" function.
	 */
	@Test
	public void Cases_23289_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		for (int i = 0; i < ds.size(); i++) {
			caseData = ds.get(i);
			caseRecord = (CaseRecord) sugar().cases.create(caseData);
			caseRecord.verify(caseData);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
