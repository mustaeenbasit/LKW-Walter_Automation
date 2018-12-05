package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23528 extends SugarTest {
	ContactRecord myContact;
	LeadRecord myLead;

	public void setup() throws Exception {
		myContact = (ContactRecord)sugar().contacts.api.create();
		myLead = (LeadRecord)sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that a related meeting with invitees can be scheduled from contact detail view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_23528_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to a contact detail view.
		myContact.navToRecord();

		StandardSubpanel meetingsSubPanel = (StandardSubpanel)sugar().contacts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);

		// Click "+" Create button in "Meetings" sub-panel.
		meetingsSubPanel.addRecord();

		// Add invitees by clicking "Add" button at the right edge of each record in Guests section and add a lead record.
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myLead);
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));

		// Verify that by default contact name is already filled in Related to field.
		sugar().meetings.createDrawer.getEditField("relatedToParentType").assertEquals(sugar().contacts.moduleNameSingular,true);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").assertEquals(myContact.get("fullName"), true);

		// save it
		sugar().meetings.createDrawer.save();

		// Verify that The scheduled meeting is displayed in "Meetings" sub-panel in contacts record.
		// FieldSet nameFs = new FieldSet();
		// nameFs.put("name", sugar().meetings.getDefaultData().get("name"));
		// meetingsSubPanel.verify(1, nameFs, true);
		// TODO: VOOD-1424 - Once resolved ^^ commented lines will work
		meetingsSubPanel.getDetailField(1, "name").assertEquals(sugar().meetings.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}