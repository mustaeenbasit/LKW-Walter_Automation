package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_26538 extends SugarTest {
	FieldSet casesRecord;
	CaseRecord myCase;

	public void setup() throws Exception {
		casesRecord = testData.get("Cases_26538").get(0);
		sugar().login();
		sugar().accounts.api.create();
		// Create a Case via the GUI so that an Account is related to the Case
		// TODO VOOD-444 Support creating relationships via API
		myCase = (CaseRecord) sugar().cases.create(casesRecord);
	}

	/**
	 * Verify a Case can be created with special characters
	 * such as !@#$%^&*()_+/" in the 'subject' and 'description' fields
	 *
	 * @throws Exception
	 */
	@Test

	public void Cases_26538_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify fields show special chars in name and description fields
		myCase.navToRecord();
		sugar().cases.recordView.getDetailField("name").assertElementContains(casesRecord.get("name"), true);
		sugar().cases.recordView.getDetailField("description").assertElementContains(casesRecord.get("description"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
