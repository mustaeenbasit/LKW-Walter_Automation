package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23023 extends SugarTest {
	FieldSet contactDefaultData = new FieldSet();

	public void setup() throws Exception {
		contactDefaultData = sugar().contacts.getDefaultData();

		// create account, contact and task
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().tasks.api.create();
		sugar().login();

		// TODO: VOOD-444 - Support creating relationships via API
		// Added related Account and contact to the Task created
		sugar().navbar.navToModule(sugar().tasks.moduleNamePlural);
		sugar().tasks.listView.clickRecord(1);
		sugar().tasks.recordView.edit();
		sugar().tasks.recordView.showMore();
		sugar().tasks.recordView.getEditField("relRelatedToParent").set(sugar().accounts.getDefaultData().get("name"));
		sugar().tasks.recordView.getEditField("contactName").set(contactDefaultData.get("lastName"));
		sugar().tasks.recordView.save();
	}

	/**
	 * Verify that corresponding contact detail view related to the selected account's task is displayed.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23023_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the Account record view
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.clickRecord(1);

		// Expand Task subpanel and click the related Contact Name link
		StandardSubpanel taskSubpanel = sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSubpanel.expandSubpanel();
		String contactFullName = contactDefaultData.get("fullName");
		taskSubpanel.clickLink(contactFullName, 1);

		// Assert the Related contact's detail view
		sugar().contacts.recordView.getDetailField("fullName").assertEquals(contactFullName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}