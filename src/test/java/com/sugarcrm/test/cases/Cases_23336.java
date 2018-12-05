package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_23336 extends SugarTest {
	CaseRecord myCase;
	FieldSet caseDefaultData;

	public void setup() throws Exception {
		caseDefaultData = sugar().cases.getDefaultData();
		sugar().login();
		sugar().accounts.api.create();
		myCase = (CaseRecord) sugar().cases.create();
	}

	/**
	 * Test Case 23336: Edit Case_Verify that case is not changed when several mandatory fields are left empty for the "Edit" function.
	 */
	@Test
	public void Cases_23336_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl subjAlert = new VoodooControl("div", "css", ".headerpane .error.input");
		VoodooControl accAlert = new VoodooControl("div", "css", "[data-name='account_name'].error");

		// Go to the created case
		myCase.navToRecord();
		// Click Edit button
		sugar().cases.recordView.edit();

		// Set case subject empty and try to save a case
		sugar().cases.recordView.getEditField("name").set("");
		sugar().cases.recordView.save();
		// Verify that a case record is not saved and Subject field is highlighted
		subjAlert.assertExists(true);
		accAlert.assertExists(false);

		// Set Subject a valid value and remove related account
		sugar().cases.recordView.getEditField("name").set(caseDefaultData.get("name"));
		// TODO: VOOD-806
		new VoodooControl("abbr", "css", "[data-name='account_name'] .select2-search-choice-close").click();
		sugar().cases.recordView.save();
		// Verify that a case record is not saved and Account field is highlighted
		subjAlert.assertExists(false);
		accAlert.assertExists(true);

		// And cancel edition
		sugar().cases.recordView.cancel();
		// Verify that case record is not changed
		myCase.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
