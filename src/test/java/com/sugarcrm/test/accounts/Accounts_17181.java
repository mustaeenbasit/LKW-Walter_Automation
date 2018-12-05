package com.sugarcrm.test.accounts;

import com.sugarcrm.sugar.views.StandardSubpanel;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Accounts_17181 extends SugarTest {

	public void setup() throws Exception {
		// Create account and contact record
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().login();

		// Relate contacts to account
		sugar().navbar.navToModule(sugar().contacts.moduleNamePlural);
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();
	}

	/**
	 * Verify inline edit on record view sub-panels
	 * @throws Exception
	 */
	@Test
	public void Accounts_17181_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);

		// Expand subpanel and edit record
		contactSubpanel.expandSubpanel();
		contactSubpanel.editRecord(1);		

		// Verify that contact sub-panel record row is in edit mode where all the available fields are in editable.
		contactSubpanel.getEditField(1, "firstName").set(customFS.get("firstName"));
		contactSubpanel.getEditField(1, "lastName").set(customFS.get("lastName"));
		sugar().contacts.getField("primaryAddressCity").editControl.set(customFS.get("primaryAddressCity"));
		sugar().contacts.getField("primaryAddressState").editControl.set(customFS.get("primaryAddressState"));
		contactSubpanel.saveAction(1); // Save contact sub-panel

		// Verify that the edited fields are saved.
		FieldSet fs = new FieldSet();
		fs.put("firstName", customFS.get("firstName"));
		fs.put("lastName", customFS.get("lastName"));
		fs.put("primaryAddressCity", customFS.get("primaryAddressCity"));
		fs.put("primaryAddressState", customFS.get("primaryAddressState"));
		contactSubpanel.verify(1, fs, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}