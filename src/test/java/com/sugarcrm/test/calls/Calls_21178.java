package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_21178 extends SugarTest {
	AccountRecord myAcc;
	ContactRecord myContact;
	DataSource contactData;

	public void setup() throws Exception {
		contactData = testData.get(testName);
		// 1 Call, 1 Account, 3 Contacts records created
		sugar.calls.api.create();
		sugar.contacts.api.create(contactData);		
		myContact = (ContactRecord) sugar.contacts.api.create();
		myAcc = (AccountRecord) sugar.accounts.api.create();
		sugar.login();

		// Link 1 contact with account
		myAcc.navToRecord();
		StandardSubpanel conSub = sugar.accounts.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		conSub.linkExistingRecord(myContact);
	}

	/**
	 * Search contacts as invitees by Account during Call creation.
	 * @throws Exception
	 */
	@Test
	public void Calls_21178_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();

		// Search invitee by account record
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(myAcc);
		sugar.calls.recordView.save();

		// Verify the related contact are searched out
		sugar.calls.recordView.getControl("invitees").assertContains(myContact.getRecordIdentifier(), true);
		sugar.calls.recordView.getControl("invitees").assertContains(contactData.get(0).get("firstName")+" "+contactData.get(0).get("lastName"),false);
		sugar.calls.recordView.getControl("invitees").assertContains(contactData.get(1).get("firstName")+" "+contactData.get(1).get("lastName"),false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}