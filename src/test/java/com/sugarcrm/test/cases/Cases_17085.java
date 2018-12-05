package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.CaseRecord;

public class Cases_17085 extends SugarTest {
	FieldSet casesRecord;
	CaseRecord myCase;

	public void setup() throws Exception {
		casesRecord = testData.get("Cases_17085").get(0);
		sugar().login();
		sugar().accounts.api.create();
		// Create a Case via the GUI so that an Account is related to the Case
		// TODO VOOD-444 Support creating relationships via API
		myCase = (CaseRecord)sugar().cases.create();
	}

	@Test
	// Verify that the in line edit works for Cases List View
	public void Cases_17085_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		sugar().cases.navToListView();
		// Inline Edit the record and save
		sugar().cases.listView.updateRecord(1, casesRecord);
		// Verify Record updated
		sugar().cases.listView.verifyField(1, "name", casesRecord.get("name"));
	}

	public void cleanup() throws Exception {}
}
