package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27680 extends SugarTest {	
	FieldSet accountRecord;
	AccountRecord myAcc;
	ContactRecord myCon;
	LeadRecord myLead;
	
	public void setup() throws Exception {
		accountRecord = testData.get(testName).get(0);
		FieldSet accountName = new FieldSet();

		// Create a Contact
		myCon = (ContactRecord) sugar().contacts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();

		// Create an Account named V-Game
		accountName.put("name", accountRecord.get("accountsName"));
		myAcc = (AccountRecord) sugar().accounts.api.create(accountName);
		accountName.clear();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Link Lead rec to Acc rec
		myAcc.navToRecord();
		sugar().accounts.recordView.subpanels.get("Leads").scrollIntoViewIfNeeded(false);
		sugar().accounts.recordView.subpanels.get("Leads").linkExistingRecord(myLead);

		// Link Contact rec to Acc rec
		sugar().accounts.recordView.subpanels.get("Contacts").scrollIntoViewIfNeeded(false);
		sugar().accounts.recordView.subpanels.get("Contacts").linkExistingRecord(myCon);
	}

	/**
	 * Verify that search Account name works correctly when add invitees in Meeting.
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27680_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Meeting
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(testName);

		// TODO: VOOD-847
		// In Guests field in Meeting, click on + to add one row and click on Select to bring a search(make sure not click on "Search for more" link.
		VoodooControl inviteeCtrl = new VoodooControl("button", "css", "div.row.participant div.cell.buttons button");
		VoodooSelect inviteeSearchCtrl = new VoodooSelect("input", "css", "#select2-drop div input");
		for(int i=0; i<2; i++){
			sugar().meetings.createDrawer.clickAddInvitee();
			// Type in V- and add contact, leads associated with the Account V-Game.
			inviteeSearchCtrl.set(accountRecord.get("searchString"));
		}

		// Save meeting
		sugar().meetings.createDrawer.save();

		// Go to the Meeting record created
		sugar().meetings.listView.clickRecord(1);

		// Verify that the new meeting that has above Contact and Lead in the meeting invitees. 
		sugar().meetings.recordView.getControl("invitees").assertContains(sugar().contacts.getDefaultData().get("fullName"), true);
		sugar().meetings.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}