package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27802 extends SugarTest {
	FieldSet fs;
	ContactRecord myCon;
	LeadRecord myLead;
	
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		myCon = (ContactRecord) sugar().contacts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
		sugar().opportunities.api.create();
		sugar().login();
		sugar().meetings.create();
	}

	/**
	 * Verify that Guest field displays correctly from preview for Meetings
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27802_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Creating a meeting that has at least 1 Contact, 1 Lead and 1 User, also make it Related To an Opportunity record.
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Selecting contact
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myCon);

		// Selecting Lead
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myLead);

		// Relating meeting to an opportunity record
		sugar().meetings.recordView.getEditField("relatedToParentType").set(sugar().opportunities.moduleNameSingular);
		sugar().meetings.recordView.getEditField("relatedToParentName").set(sugar().opportunities.getDefaultData().get("name"));

		sugar().meetings.recordView.save();
		sugar().meetings.navToListView();

		// Preview the meeting from listview.
		sugar().meetings.listView.previewRecord(1);

		// Verify that Contact is in Guests field.
		// TODO: VOOD-1223 Need library support to get records in Guests list in Meetings record view
		new VoodooControl("div", "css", ".participants").assertContains(myCon.getRecordIdentifier(), true);

		// Verify that lead is in Guests field.
		new VoodooControl("div", "css", ".participants").assertContains(myLead.getRecordIdentifier(), true);

		// Verify that user is in Guests field.
		new VoodooControl("div", "css", ".participants").assertContains(fs.get("user"), true);

		// Go to opportunity record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Preview the meeting from the meeting subpanel in opportunity record view
		sugar().opportunities.recordView.subpanels.get("Meetings").clickPreview(1);

		// TODO: VOOD-1223 Need library support to get records in Guests list in Meetings record view
		// Verify that Contact is in Guests field.
		new VoodooControl("div", "css", ".participants").assertContains(sugar().contacts.getDefaultData().get("lastName"), true);

		// Verify that lead is in Guests field.
		new VoodooControl("div", "css", ".participants").assertContains(sugar().leads.getDefaultData().get("lastName"), true);

		// Verify that user is in Guests field.
		new VoodooControl("div", "css", ".participants").assertContains(fs.get("user"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}