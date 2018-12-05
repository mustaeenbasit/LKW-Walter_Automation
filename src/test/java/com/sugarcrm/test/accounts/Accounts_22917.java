package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22917 extends SugarTest {
	FieldSet editedCaseValues;

	public void setup() throws Exception {
		editedCaseValues = testData.get(testName).get(0);		

		// Create an account
		sugar().accounts.api.create();

		// Log-In as an Admin
		sugar().login();

		// TODO: VOOD-1850 - Case Priority values are not mapped with its display labels.
		// TODO: VOOD-444 - Support creating relationships via API
		// Create a Case record related to the Account
		sugar().navbar.navToModule(sugar().cases.moduleNamePlural);
		sugar().cases.listView.create();
		sugar().cases.createDrawer.getEditField("name").set(sugar().cases.getDefaultData().get("name"));
		sugar().cases.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().cases.createDrawer.save();
	}

	/**
	 * TC 22917: Verify that editing case record related to this account can be canceled
	 * @throws Exception
	 */
	@Test
	public void Accounts_22917_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel casesSubpanel = sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		casesSubpanel.expandSubpanel();
		casesSubpanel.editRecord(1);

		// Change the values of case' fields
		casesSubpanel.getEditField(1, "name").set(editedCaseValues.get("name"));
		casesSubpanel.getEditField(1, "priority").set(editedCaseValues.get("priority"));
		casesSubpanel.getEditField(1, "status").set(editedCaseValues.get("status"));

		// Cancel the Edit action
		casesSubpanel.cancelAction(1);

		// TODO: VOOD-1424 - Make StandardSubpanel.verify() verify specified value is in correct column
		// Assert that values not changed
		casesSubpanel.getDetailField(1, "name").assertEquals(editedCaseValues.get("name"), false);
		casesSubpanel.getDetailField(1, "status").assertEquals(editedCaseValues.get("status"), false);
		casesSubpanel.getDetailField(1, "priority").assertEquals(editedCaseValues.get("priority"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}