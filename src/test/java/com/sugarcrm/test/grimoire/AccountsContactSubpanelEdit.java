package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class AccountsContactSubpanelEdit extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		FieldSet account = new FieldSet();
		account.put("relAccountName", myAccount.getRecordIdentifier());
		ContactRecord myContact = (ContactRecord)sugar().contacts.api.create();

		sugar().login();

		// Accounts is linked to Contacts
		// TODO: VOOD-444
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.setFields(account);
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.recordView.save();
	}

	@Test
	public void verifyContactEditSubpanel() throws Exception {
		VoodooUtils.voodoo.log.info("Running  verifyContactEditSubpanel()...");

		myAccount.navToRecord();
		StandardSubpanel contactsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.assertVisible(true);
		contactsSubpanel.expandSubpanel();
		contactsSubpanel.editRecord(1);
		contactsSubpanel.getEditField(1, "lastName").assertVisible(true);
		contactsSubpanel.cancelAction(1);

		VoodooUtils.voodoo.log.info("verifyContactEditSubpanel() complete.");
	}

	public void cleanup() throws Exception {}
}