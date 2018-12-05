package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22910 extends SugarTest {
	ContactRecord myContact;
	StandardSubpanel contactsSubpanel;
	DataSource contactData;

	public void setup() throws Exception {
		contactData = testData.get(testName);
		sugar().accounts.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create(contactData.get(0));

		// Login as admin user
		sugar().login();

		// Existing Contact related to an account record needed
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		contactsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecord(myContact);
	}

	/**
	 * 22910 Verify that contact record related to this account can be edited by clicking "edit" icon
	 * @throws Exception
	 */
	@Test
	public void Accounts_22910_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "edit" icon on the right edge of a contact record on "CONTACTS" sub-panel and modify values in all available fields(salutation and relAccount can't update)
		contactsSubpanel.editRecord(1);
		contactsSubpanel.getEditField(1,"firstName").set(contactData.get(1).get("firstName"));
		contactsSubpanel.getEditField(1,"lastName").set(contactData.get(1).get("lastName"));
		contactsSubpanel.getEditField(1,"phoneWork").set(contactData.get(1).get("phoneWork"));
		contactsSubpanel.saveAction(1);

		// Verify new values is displayed on "CONTACTS" sub-panel.
		contactsSubpanel.getDetailField(1,"fullName").assertContains(contactData.get(1).get("firstName"), true);
		contactsSubpanel.getDetailField(1,"fullName").assertContains(contactData.get(1).get("lastName"), true);
		contactsSubpanel.getDetailField(1,"phoneWork").assertContains(contactData.get(1).get("phoneWork"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}