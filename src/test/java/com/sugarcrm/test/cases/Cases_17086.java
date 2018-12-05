package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.CaseRecord;

public class Cases_17086 extends SugarTest {
	FieldSet casesRecord;
	CaseRecord myCase;

	public void setup() throws Exception {
		casesRecord = testData.get("Cases_17086").get(0);
		sugar().login();
		sugar().accounts.api.create();
		// Create a Case via the GUI so that an Account is related to the Case
		// TODO VOOD-444 Support creating relationships via API
		myCase = (CaseRecord) sugar().cases.create();
	}

	@Test
	// Verify the user can Cancel the inline editing from the record level
	// action drop down on List View
	public void Cases_17086_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		sugar().cases.listView.editRecord(1);
		sugar().cases.listView.setEditFields(1, casesRecord);
		sugar().cases.listView.cancelRecord(1);
		// Assert the original default name value is present
		sugar().cases.listView.verifyField(1, "name", myCase.get("name"));
	}

	public void cleanup() throws Exception {}
}
