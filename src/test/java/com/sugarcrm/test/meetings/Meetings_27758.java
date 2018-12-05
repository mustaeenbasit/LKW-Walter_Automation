package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27758 extends SugarTest {
	DataSource userDS;
	UserRecord chris, bill, will;

	public void setup() throws Exception {
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		userDS = testData.get(testName);
		chris = (UserRecord) sugar().users.api.create();
		bill = (UserRecord) sugar().users.api.create(userDS.get(0));
		will = (UserRecord) sugar().users.api.create(userDS.get(1));
		sugar().meetings.api.create();
		sugar().opportunities.api.create();
	}

	/**
	 * Verify that users are able to be added in the invitees 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27758_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Add all possible users in Guests field of meetings module
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(chris);

		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(bill);

		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(will);

		// Select an Opportunity  record in "Related To". 
		sugar().meetings.recordView.getEditField("relatedToParentType").set(sugar().opportunities.moduleNameSingular);
		sugar().meetings.recordView.getEditField("relatedToParentName").set(sugar().opportunities.getDefaultData().get("name"));

		// Save meeting
		sugar().meetings.recordView.save();

		// Verify all users are added in the meetings record view
		VoodooControl inviteeCtrl = sugar().meetings.recordView.getControl("invitees");
		inviteeCtrl.assertContains(chris.get("firstName"), true);
		inviteeCtrl.assertContains(bill.get("firstName"), true);
		inviteeCtrl.assertContains(will.get("firstName"), true);

		// Verify all other fields appears with correct values.
		sugar().meetings.recordView.getDetailField("name").assertEquals(sugar().meetings.getDefaultData().get("name"), true);
		sugar().meetings.recordView.getDetailField("status").assertEquals(sugar().meetings.getDefaultData().get("status"), true);
		sugar().meetings.recordView.getDetailField("type").assertEquals(sugar().meetings.getDefaultData().get("type"), true);
		sugar().meetings.recordView.getDetailField("relatedToParentName").assertEquals(sugar().opportunities.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");	
	}

	public void cleanup() throws Exception {}
}