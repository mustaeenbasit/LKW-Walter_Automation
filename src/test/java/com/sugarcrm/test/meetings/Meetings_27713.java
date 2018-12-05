package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27713 extends SugarTest {
	FieldSet fs;
	ContactRecord myContact;
	LeadRecord myLead;

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		myContact = (ContactRecord) sugar().contacts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that when a Contact/Lead is selected in Related to, the Contact/Lead is appearing in Guests field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27713_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(fs.get("name"));
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().contacts.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(sugar().contacts.getDefaultData().get("lastName"));

		// Verify that the Contact is automatically appearing in Guests field, as a meeting invitee.
		new VoodooControl("div","css",".participants-schedule").assertContains(sugar().contacts.getDefaultData().get("lastName"), true);

		sugar().meetings.createDrawer.save();

		// Verify that the meeting is created.
		sugar().meetings.listView.verifyField(1, "name", fs.get("name"));

		// Preview the meeting from listview.
		sugar().meetings.listView.previewRecord(1);

		// Verify that Contact is in Guests field.
		new VoodooControl("div", "css", ".participants").assertContains(sugar().contacts.getDefaultData().get("lastName"), true);

		// Go to recordView and edit the meeting by removing the Contact.
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		new VoodooControl("i", "css", ".participants-schedule [data-module='Contacts'] i.fa.fa-minus").click();

		// Selecting a lead 
		sugar().meetings.recordView.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		sugar().meetings.recordView.getEditField("relatedToParentName").set(sugar().leads.getDefaultData().get("lastName"));

		// Verify that the Lead is automatically appearing in Guests field, as a meeting invitee.
		new VoodooControl("div", "css", ".participants-schedule").assertContains(sugar().leads.getDefaultData().get("lastName"), true);

		sugar().meetings.recordView.save();

		// Verify that in meeting preview, the Contact is not in the Guests field, but Lead is one invitee. 
		sugar().meetings.navToListView();
		sugar().meetings.listView.previewRecord(1);
		new VoodooControl("div","css",".participants").assertContains(sugar().contacts.getDefaultData().get("lastName"), false);
		new VoodooControl("div","css",".participants").assertContains(sugar().leads.getDefaultData().get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}