package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22908 extends SugarTest {
	AccountRecord myAccount;
	ContactRecord myContact;
	StandardSubpanel contactsSubpanel;

	public void setup() throws Exception {
		sugar().login();

		// Create a new account and a contact
		myAccount = (AccountRecord)sugar().accounts.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();

		// Add Account to Contact
		// TODO: VOOD-444 
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.recordView.save();
	}

	/**
	 * Verify that contact record related to this account can be viewed by clicking subject link on "CONTACTS" sub-panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22908_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Goto Accounts record view
		myAccount.navToRecord();
		contactsSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.expandSubpanel();

		// Click one contact's name link on "CONTACTS" sub-panel
		contactsSubpanel.clickRecord(1);
		
		// Verify that Contact recordview appears
		sugar().contacts.recordView.assertVisible(true);
		sugar().contacts.recordView.getDetailField("fullName").assertContains(myContact.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}