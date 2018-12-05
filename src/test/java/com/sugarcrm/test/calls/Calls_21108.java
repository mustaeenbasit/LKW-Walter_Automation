package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_21108 extends SugarTest {
	ContactRecord con1, con2;
	UserRecord chris, qaUser;

	public void setup() throws Exception {
		DataSource contactDS = testData.get(testName);

		// 2 contacts, 2 users, 1 call record
		con1 = (ContactRecord)sugar.contacts.api.create(contactDS.get(0));
		con2 = (ContactRecord)sugar.contacts.api.create(contactDS.get(1));
		chris = (UserRecord)sugar.users.api.create();
		qaUser = new UserRecord(sugar.users.getQAUser());
		sugar.calls.api.create();
		sugar.login();
	}

	/**
	 * Calls Create With Invitees
	 * @throws Exception
	 */
	@Test
	public void Calls_21108_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();

		// Add 2 Contacts and Users as invitee
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(con1);
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(con2);
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(chris);
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(qaUser);
		sugar.calls.recordView.save();

		// Verify contacts and users records are in guest list
		sugar.calls.recordView.getControl("invitees").assertContains(con1.getRecordIdentifier(),true);
		sugar.calls.recordView.getControl("invitees").assertContains(con2.getRecordIdentifier(),true);
		sugar.calls.recordView.getControl("invitees").assertContains(chris.getRecordIdentifier(),true);
		sugar.calls.recordView.getControl("invitees").assertContains(qaUser.getRecordIdentifier(),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}