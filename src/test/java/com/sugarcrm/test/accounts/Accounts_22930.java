package com.sugarcrm.test.accounts;

import static org.junit.Assert.*;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Accounts_22930 extends SugarTest {
	AccountRecord myAccount;
	ContactRecord myContact;
	StandardSubpanel contactsSubpanel;

	public void setup() throws Exception {
		sugar().login();

		// Create a new account
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}

	/**
	 * Verify that creating new contact related to the account is canceled in full form on "CONTACTS" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22930_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Goto Accounts record view
		myAccount.navToRecord();
		contactsSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get("Contacts");
		contactsSubpanel.scrollIntoViewIfNeeded(false);
		contactsSubpanel.addRecord();

		// Fill all fields on the createDrawer
		FieldSet recordData = sugar.contacts.getDefaultData();
		sugar.contacts.createDrawer.showMore();
		sugar.contacts.createDrawer.setFields(recordData);

		// Click Cancel
		sugar.contacts.createDrawer.cancel();
		
		// Expand Subpanel and verify that it is empty
		contactsSubpanel.expandSubpanel();
		assertTrue("Contacts Subpanel is not Empty", contactsSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}