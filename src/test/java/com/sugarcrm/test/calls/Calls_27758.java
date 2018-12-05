package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_27758 extends SugarTest {
	DataSource userDS;
	UserRecord chrisUser, testUser, newUser;

	public void setup() throws Exception {
		userDS = testData.get(testName);
		chrisUser = (UserRecord) sugar.users.api.create(userDS.get(0));
		testUser = (UserRecord) sugar.users.api.create(userDS.get(1));
		newUser = (UserRecord) sugar.users.api.create(userDS.get(2));
		sugar.calls.api.create();
		sugar.opportunities.api.create();
		// Login as QAUser
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * Verify that users are able to be added in the invitees 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_27758_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		
		// Add all possible users in Guests field of Calls module
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(chrisUser);
		
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(testUser);
		
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(newUser);

		// Select an Opportunity  record in "Related To". 
		sugar.calls.recordView.getEditField("relatedToParentType").set(sugar.opportunities.moduleNameSingular);
		sugar.calls.recordView.getEditField("relatedToParentName").set(sugar.opportunities.getDefaultData().get("name"));

		// Save Calls
		sugar.calls.recordView.save();

		// Verify all users are added in the calls record view
		VoodooControl inviteeCtrl = sugar.meetings.recordView.getControl("invitees");
		inviteeCtrl.assertContains(userDS.get(0).get("firstName"), true);
		inviteeCtrl.assertContains(userDS.get(1).get("firstName"), true);
		inviteeCtrl.assertContains(userDS.get(2).get("firstName"), true);

		// Verify all other fields appears with correct values.
		sugar.calls.recordView.getDetailField("name").assertContains(sugar.calls.getDefaultData().get("name"), true);
		sugar.calls.recordView.getDetailField("status").assertContains(sugar.calls.getDefaultData().get("status"), true);
		sugar.calls.recordView.getDetailField("relatedToParentName").assertContains(sugar.opportunities.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");	
	}

	public void cleanup() throws Exception {}
}