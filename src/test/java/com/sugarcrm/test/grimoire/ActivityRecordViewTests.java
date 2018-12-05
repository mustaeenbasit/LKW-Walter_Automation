package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class ActivityRecordViewTests extends SugarTest {
	CallRecord myCall;
	ContactRecord myContact;
	LeadRecord myLead;

	public void setup() throws Exception {
		myCall = (CallRecord)sugar().calls.api.create();
		myContact = (ContactRecord)sugar().contacts.api.create();
		myLead = (LeadRecord)sugar().leads.api.create();
		sugar().login();
	}

	@Test
	public void closeAndCreateNew() throws Exception {
		VoodooUtils.voodoo.log.info("Running closeAndCreateNew()...");

		myCall.navToRecord();

		sugar().calls.recordView.closeAndCreateNew(); // Test
		sugar().calls.createDrawer.getEditField("name").assertVisible(true);
		sugar().calls.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("closeAndCreateNew() complete.");
	}

	@Test
	public void close() throws Exception {
		VoodooUtils.voodoo.log.info("Running close()...");

		myCall.navToRecord();
		sugar().calls.recordView.close(); // Test

		sugar().calls.recordView.getDetailField("status").assertElementContains("Held", true);

		VoodooUtils.voodoo.log.info("close() complete.");
	}

	@Test
	public void share() throws Exception {
		VoodooUtils.voodoo.log.info("Running share()...");

		myCall.navToRecord();
		sugar().calls.recordView.share(); 

		// Verify that we're on the expected view, email compose view.
		sugar().calls.recordView.composeEmail.getControl("toAddress").assertVisible(true);
		sugar().calls.recordView.composeEmail.cancel();

		VoodooUtils.voodoo.log.info("share() complete.");
	}

	@Test
	public void verifyInviteeOnRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInviteeOnRecordView()...");

		myCall.addInvitee(myContact);
		myCall.addInvitee(myLead);
		myCall.navToRecord();

		FieldSet fs = new FieldSet();
		fs.put("name", "Administrator");
		fs.put("status", "No Reply");
		sugar().calls.recordView.verifyInvitee(2, fs);

		FieldSet fs1 = new FieldSet();
		fs1.put("name", "Ran Zhou");
		fs1.put("status", "No Reply");
		sugar().calls.recordView.verifyInvitee(4, fs1);

		VoodooUtils.voodoo.log.info("verifyInviteeOnRecordView() complete.");
	}

	@Test
	public void saveAndSendInvites() throws Exception {
		VoodooUtils.voodoo.log.info("Running saveAndSendInvites()...");

		myCall.navToRecord();
		sugar().calls.recordView.edit();
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(sugar().users.getQAUser().get("userName"));
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(myContact);
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(myLead);

		sugar().calls.recordView.saveAndSendInvites();

		// Verify that we're on record view after save and send invites
		sugar().calls.recordView.assertVisible(true);

		VoodooUtils.voodoo.log.info("saveAndSendInvites() complete.");
	}

	public void cleanup() throws Exception {}
}