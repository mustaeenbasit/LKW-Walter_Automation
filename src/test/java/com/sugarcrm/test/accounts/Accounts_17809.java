package com.sugarcrm.test.accounts;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_17809 extends SugarTest {
	DataSource contactsData;
	protected ArrayList<Record> myContacts;

	public void setup() throws Exception {
		contactsData = testData.get(testName);
		// Create Account and Contact
		AccountRecord myAccount = (AccountRecord) sugar().accounts.api.create();
		myContacts = sugar().contacts.api.create(contactsData);

		// Login to SugarCRM
		sugar().login();

		// Relates Contacts to the Account
		for (Record con : myContacts) {
			con.navToRecord();
			sugar().contacts.recordView.edit();
			sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
			sugar().alerts.getAlert().confirmAlert();
			sugar().contacts.recordView.save();
		}
	}

	/**
	 * Verify clearing of search criteria from search field after module selection - record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17809_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Related" drop down and choose a module that contains several related records such as "Contacts".
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.setRelatedSubpanelFilter(sugar().contacts.moduleNamePlural);
		StandardSubpanel contactsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.expandSubpanel();
		String filter1 = contactsData.get(0).get("firstName");
		sugar().accounts.recordView.setSearchString(filter1);

		// Verify that the text entered in the search field
		contactsSubpanel.getDetailField(1, "fullName").assertContains(filter1, true);
		Assert.assertTrue("Filter is not search records correctly", contactsSubpanel.countRows() == contactsData.size() - 1);

		// Click "Related" drop down and choose a module that contains several related records such as "Leads".
		sugar().accounts.recordView.setRelatedSubpanelFilter(sugar().leads.moduleNamePlural);

		// Verify that the newly selected module is now displayed next to "Related" drop down
		sugar().accounts.recordView.getControl("searchFilter").assertContains(filter1, false);
		StandardSubpanel leadsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.isEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}