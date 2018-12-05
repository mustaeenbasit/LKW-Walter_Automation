package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22915 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().cases.api.create();
		sugar().login();

		// TODO: VOOD-444 - Support creating relationships via API
		// Link cases with an account
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.edit();
		sugar().cases.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().cases.recordView.save();
	}

	/**
	 * Verify that case record related to the account record can be inline-edited in cases sub-panel on the account record view 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22915_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to an Account Record view
		FieldSet editedCaseValues = testData.get(testName).get(0);
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Select Edit in the actions menu on the right edge of "CASES" sub-panel
		StandardSubpanel casesSubpanel = sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		casesSubpanel.expandSubpanel();
		casesSubpanel.editRecord(1);

		// Modify values in all the fields
		for (String controlName : editedCaseValues.keySet())
			sugar().cases.getField(controlName).listViewEditControl.set(editedCaseValues.get(controlName));

		casesSubpanel.saveAction(1); // Trigger save action

		// Verify the modified case information is displayed on "CASES" sub-panel
		casesSubpanel.getDetailField(1, "name").assertEquals(editedCaseValues.get("name"), true);
		casesSubpanel.getDetailField(1, "status").assertEquals(editedCaseValues.get("status"), true);
		casesSubpanel.getDetailField(1, "priority").assertEquals(editedCaseValues.get("priority"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}