package com.sugarcrm.test.calls;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_27950 extends SugarTest {
	LeadRecord myLeadRecord;
	ContactRecord myContactRecord;

	public void setup() throws Exception {
		// Creating Lead Record
		myLeadRecord = (LeadRecord) sugar().leads.api.create();

		// Creating Contact Record
		myContactRecord = (ContactRecord) sugar().contacts.api.create();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that "Attending" is set in all children records when assignee is creator
	 * @throws Exception
	 */
	@Test
	public void Calls_27950_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Calls List View
		sugar().calls.navToListView();

		// Create a Weekly Recurring Meeting
		sugar().calls.listView.create();
		sugar().calls.createDrawer.getEditField("name").set(testName);
		FieldSet customData = testData.get(testName).get(0);
		sugar().calls.createDrawer.getEditField("repeatType").set(customData.get("repeat_type"));
		sugar().calls.createDrawer.getEditField("repeatOccurType").set(customData.get("repeatOccurType"));

		// Adding lead as invitee
		sugar().calls.createDrawer.clickAddInvitee();
		sugar().calls.createDrawer.selectInvitee(myLeadRecord);

		// Adding contact as invitee
		sugar().calls.createDrawer.clickAddInvitee();
		sugar().calls.createDrawer.selectInvitee(myContactRecord);
		sugar.calls.recordView.save();

		// Navigate to list view.
		sugar().calls.navToListView();

		// Verify 10 records exists.
		Integer recordCount = Integer.parseInt(customData.get("recordCount"));
		Assert.assertTrue("Calls list view doesn't have 10 records", sugar().calls.listView.countRows() == recordCount);

		// Open all Meetings to check acceptance status
		sugar().calls.listView.clickRecord(1);
		// TODO: VOOD-1223 Need library support to get records in Guests list in Meetings record view
		VoodooControl contactInviteeCtrl = new VoodooControl("div", "css", ".row.participant[data-module='Contacts']");
		VoodooControl qauserInviteeCtrl = new VoodooControl("div", "css", ".row.participant[data-module='Users']");
		VoodooControl leadInviteeCtrl = new VoodooControl("div", "css", ".row.participant[data-module='Leads']");

		for (int i = 0; i < recordCount; i++) {	
			// Verify Contact has "No Reply" status. 
			contactInviteeCtrl.assertContains(customData.get("call_status2"), true);

			// Verify Lead has "No Reply" status. 
			leadInviteeCtrl.assertContains(customData.get("call_status2"), true);

			// Verify user has "Attending" status
			qauserInviteeCtrl.assertContains(customData.get("call_status1"), true);

			sugar().calls.recordView.gotoNextRecord();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}