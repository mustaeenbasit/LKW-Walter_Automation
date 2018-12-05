package com.sugarcrm.test.meetings;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27216 extends SugarTest {
	UserRecord myUser1, myUser2;
	ContactRecord myContact;
	LeadRecord myLead;
	DataSource userData;

	public void setup() throws Exception {
		userData = testData.get(testName);

		// Create user1 and user2 
		myUser1 = (UserRecord) sugar().users.api.create(userData.get(0));
		myUser2 = (UserRecord) sugar().users.api.create(userData.get(1));
		myContact = (ContactRecord) sugar().contacts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that meeting invitee can be removed at create meeting time
	 * @throws Exception
	 */
	@Test
	public void Meetings_27216_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on "Schedule Meeting". Fill in the meeting subject.
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(testName);

		// Click on + sign at the Guest field to add a row and select one user.
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myUser1);

		// Click on + sign at the Guest field to add a row and select another user.
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myUser2);

		// Add contact in guest list
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myContact);

		// Add Lead in guest list
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myLead);

		VoodooControl createDrawerInviteeCtrl = sugar().meetings.createDrawer.getControl("invitees");
		VoodooControl removeInviteeCtrl = sugar().meetings.createDrawer.getControl("removeInvitee03");
		VoodooControl recordViewInviteeCtrl = sugar().meetings.recordView.getControl("invitees");

		// Remove other User during meeting creation
		removeInviteeCtrl.click();
		// Confirm that other User has been removed
		createDrawerInviteeCtrl.assertContains(myUser1.getRecordIdentifier(), true);
		createDrawerInviteeCtrl.assertContains(myContact.getRecordIdentifier(), true);
		createDrawerInviteeCtrl.assertContains(myLead.getRecordIdentifier(), true);
		createDrawerInviteeCtrl.assertContains(myUser2.getRecordIdentifier(), false);

		// Remove Contact during meeting creation
		removeInviteeCtrl.click();
		// Confirm that Contact has been removed
		createDrawerInviteeCtrl.assertContains(myUser1.getRecordIdentifier(), true);
		createDrawerInviteeCtrl.assertContains(myLead.getRecordIdentifier(), true);
		createDrawerInviteeCtrl.assertContains(myContact.getRecordIdentifier(), false);

		// Remove Lead during meeting creation
		removeInviteeCtrl.click();
		// Confirm that Lead has been removed
		createDrawerInviteeCtrl.assertContains(myUser1.getRecordIdentifier(), true);
		createDrawerInviteeCtrl.assertContains(myLead.getRecordIdentifier(), false);

		// Click on "Save"
		sugar().meetings.createDrawer.save();

		// Click on the Meeting record
		sugar().meetings.listView.clickRecord(1);

		//Verify that the meeting is saved and QAUser and user1 are in the Guests list. 
		recordViewInviteeCtrl.assertContains(sugar().users.getQAUser().get("userName"), true);
		recordViewInviteeCtrl.assertContains(myUser1.getRecordIdentifier(), true);
		recordViewInviteeCtrl.assertContains(myUser2.getRecordIdentifier(), false);
		recordViewInviteeCtrl.assertContains(myContact.getRecordIdentifier(), false);
		recordViewInviteeCtrl.assertContains(myLead.getRecordIdentifier(), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}