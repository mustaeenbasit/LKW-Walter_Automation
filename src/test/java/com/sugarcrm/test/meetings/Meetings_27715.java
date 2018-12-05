package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27715 extends SugarTest {

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().leads.api.create();
		sugar().meetings.api.create();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that when a Contact/Lead is removed before saving even though it is in "Related To" field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27715_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Selecting contact
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().contacts.getDefaultData().get("lastName"));		

		// Selecting Lead
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().leads.getDefaultData().get("lastName"));

		// TODO: VOOD-1221 Need library support to remove records from the Guests list in Meetings record view
		// Clicking on "-" beside contact
		new VoodooControl("i", "css", ".participants-schedule [data-module='Contacts'] i.fa.fa-minus").click();
		sugar().alerts.waitForLoadingExpiration();
		// Clicking on "-" beside lead
		new VoodooControl("i", "css", ".participants-schedule [data-module='Leads'] i.fa.fa-minus").click();

		sugar().meetings.recordView.save();

		// TODO: VOOD-1223 Need library support to get records in Guests list in Meetings record view 
		// Verify that the Contact is not appearing in Guests field, as a meeting invitee.
		new VoodooControl("div","css",".participants-schedule").assertContains(sugar().contacts.getDefaultData().get("lastName"), false);
		// Verify that the lead is not appearing in Guests field, as a meeting invitee.
		new VoodooControl("div","css",".participants-schedule").assertContains(sugar().leads.getDefaultData().get("lastName"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
