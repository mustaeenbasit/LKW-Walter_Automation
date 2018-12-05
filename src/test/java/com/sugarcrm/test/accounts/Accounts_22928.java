package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Assert;

public class Accounts_22928 extends SugarTest {

	public void setup() throws Exception {
		// Create an Account record
		sugar().accounts.api.create();

		// Login to system as valid user.
		sugar().login();
	}

	/**
	 * Create Verify that creating new contact related to the account is canceled in-line on "CONTACTS" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Accounts_22928_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigates to the record view of the created Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Click "Create" on "CONTACTS" sub-panel.
		StandardSubpanel contactsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.addRecord();
		sugar().contacts.createDrawer.showMore();
		
		// Fill all fields.
		sugar().contacts.createDrawer.setFields(sugar().contacts.getDefaultData());
		
		// Click "Cancel" button.
		sugar().contacts.createDrawer.cancel();
		
		// Assert No matching contact record is displayed on "CONTACTS" sub-panel.
		Assert.assertTrue("Contacts subpanel has a record.", contactsSubpanel.isEmpty());

		// Assert No matching contact record is displayed on "CONTACTS" module list view
		sugar().contacts.navToListView();
		sugar().contacts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}