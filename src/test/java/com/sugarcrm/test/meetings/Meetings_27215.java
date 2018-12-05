package com.sugarcrm.test.meetings;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27215 extends SugarTest {
	UserRecord myUser;
	ContactRecord myContact;
	LeadRecord myLead;

	public void setup() throws Exception {
		// Login as a QAUser
		sugar().login(sugar().users.getQAUser());

		myUser = (UserRecord) sugar().users.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
	}

	/**
	 * Verify that meeting invitee can be removed at edit meeting
	 * @throws Exception
	 */
	@Test
	public void Meetings_27215_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// navigate to Meetings module. Create a Sugar meeting.
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(testName);

		// Add contact in guest list
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().contacts.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(sugar().contacts.getDefaultData().get("lastName"));

		// Add Lead in guest list
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(sugar().leads.getDefaultData().get("lastName"));

		// Add user in guest list
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myUser);

		// save meeting record
		sugar().meetings.createDrawer.save();



		// Go to the newly created meeting record
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.showMore();

		VoodooControl recordViewInviteeCtrl = sugar().meetings.recordView.getControl("invitees");
		// create a dataset of all invities 
		DataSource inviteeData = new DataSource();
		FieldSet contactRecord = new FieldSet();
		contactRecord.put("name", sugar().contacts.getDefaultData().get("fullName"));

		FieldSet leadRecord = new FieldSet();
		leadRecord.put("name", sugar().leads.getDefaultData().get("fullName"));

		FieldSet firstUserRecord = new FieldSet();
		firstUserRecord.put("name",sugar().users.getDefaultData().get("fullName"));

		FieldSet secondUserRecord = new FieldSet();
		secondUserRecord.put("name", sugar().users.getQAUser().get("userName"));

		inviteeData.add(leadRecord);
		inviteeData.add(firstUserRecord);
		inviteeData.add(secondUserRecord);
		inviteeData.add(contactRecord);

		int i = 1;
		// Verify that all info in the meeting record are correct as you input, especially in Guests field.
		for(FieldSet fs : inviteeData){
			sugar().meetings.recordView.verifyInvitee(i, fs);
			i++;
		}

		// Edit the meeting record
		sugar().meetings.recordView.edit();

		// Verify 3 meeting participants are removed and only creator(QAUser) of the meeting remains
		VoodooControl inviteeCtrl = sugar().meetings.createDrawer.getControl("invitees");

		// Click on - sign at the end of the Contact invitee.
		sugar().meetings.createDrawer.getControl("removeInvitee04").click();
		VoodooUtils.waitForReady();

		// Confirm that Contact has been removed
		inviteeCtrl.assertContains(myContact.getRecordIdentifier(), false);
		inviteeCtrl.assertContains(myLead.getRecordIdentifier(),true);
		inviteeCtrl.assertContains(myUser.getRecordIdentifier(),true);
		inviteeCtrl.assertContains(sugar().users.getQAUser().get("userName"),true);

		// Click on - sign at the end of the Lead invitee.
		sugar().meetings.createDrawer.getControl("removeInvitee02").click();
		VoodooUtils.waitForReady();
		// Confirm that user has been removed
		inviteeCtrl.assertContains(sugar().users.getQAUser().get("userName"),true);
		inviteeCtrl.assertContains(myUser.getRecordIdentifier(),false);
		inviteeCtrl.assertContains(myLead.getRecordIdentifier(),true);

		// TODO: VOOD-1223
		new VoodooControl("span", "XPATH", "//*[@class='fld_invitees edit']/div/div[contains(.,'" + myLead.getRecordIdentifier() + "')]/div[3]//button").click();
		VoodooUtils.waitForReady();
		// Confirm that Lead has been removed
		inviteeCtrl.assertContains(sugar().users.getQAUser().get("userName"),true);
		inviteeCtrl.assertContains(myUser.getRecordIdentifier(),false);
		inviteeCtrl.assertContains(myLead.getRecordIdentifier(),false);

		// Confirm creator User has its remove button disabled
		// TODO: VOOD-1223
		new VoodooControl("button", "css", "div[data-module='Users'] > div.cell.buttons > div button[data-action='removeRow'][class*='disabled']").assertExists(true);

		// Click on Save.
		sugar().meetings.recordView.save();

		// Verify only creator of the meeting is available and rest of earlier meeting participants do not exist
		recordViewInviteeCtrl.assertContains(myContact.getRecordIdentifier(), false);
		recordViewInviteeCtrl.assertContains(myLead.getRecordIdentifier(),false);
		recordViewInviteeCtrl.assertContains(myUser.getRecordIdentifier(),false);
		recordViewInviteeCtrl.assertContains(sugar().users.getQAUser().get("userName"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}