package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27290 extends SugarTest {
	DataSource myContacts;
		
	public void setup() throws Exception {
		myContacts = testData.get(testName);
		sugar().meetings.api.create();
		sugar().contacts.api.create(myContacts);
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that only one meeting is created when select 2 contacts as meeting invitee.
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27290_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.showMore();
		
		// In Guests fields, select 2 Contacts as invitees.
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myContacts.get(0).get("firstName"));		
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myContacts.get(1).get("firstName"));
		sugar().meetings.recordView.save();
		
		sugar().meetings.navToListView();
		
		// Verify in listview, new meeting is created/saved.
		new VoodooControl("tr", "css", "div.layout_Meetings table tbody tr:nth-child(1)").assertVisible(true);
		sugar().meetings.listView.verifyField(1, "name", sugar().meetings.getDefaultData().get("name"));
		
		// Verify that only one meeting is created (i.e. 2nd row is not present in listview)
		new VoodooControl("tr", "css", "div.layout_Meetings table tbody tr:nth-child(2)").assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}