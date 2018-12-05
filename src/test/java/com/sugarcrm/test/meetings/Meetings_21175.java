package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_21175 extends SugarTest {	
	AccountRecord myAccount;
	ContactRecord myFirstContact, mySecondContact;

	public void setup() throws Exception {
		DataSource ds = testData.get(testName);

		// 2 Contacts, 1 account, 1 meeting records
		myFirstContact = (ContactRecord)sugar().contacts.api.create(ds.get(0));
		mySecondContact = (ContactRecord)sugar().contacts.api.create(ds.get(1));
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().meetings.api.create();
		sugar().login();

		// Link one of the contacts record with the Account exist.
		myAccount.navToRecord();
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myFirstContact);
	}

	/**
	 * Search contact as invitees by Account during Meeting creation.
	 * @throws Exception
	 */
	@Test
	public void Meetings_21175_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to meeting edit view.
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Search invitee by account record
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(myAccount);
		sugar().calls.recordView.save();

		// Verify the related contact are searched out
		VoodooControl inviteeCtrl = sugar().calls.recordView.getControl("invitees");
		inviteeCtrl.assertContains(myFirstContact.getRecordIdentifier(), true);
		inviteeCtrl.assertContains(mySecondContact.getRecordIdentifier(),false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}