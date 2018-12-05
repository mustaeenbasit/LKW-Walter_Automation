package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27217 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that meeting creator is able to be removed in meeting
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27217_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet qauser = sugar().users.getQAUser();
		sugar().navbar.navToModule(sugar().meetings.moduleNamePlural);
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.getEditField("assignedTo").set(qauser.get("userName"));
		sugar().meetings.createDrawer.save();

		// Logout and login as qauser
		sugar().logout();
		sugar().login(qauser);

		// Goto the newly created meeting record
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Remove original creator
		sugar().meetings.recordView.getControl("removeInvitee01").click();
		VoodooUtils.waitForReady();

		// Confirm original creator has been removed
		FieldSet qaUserName = new FieldSet();
		qaUserName.put("userName", qauser.get("userName"));
		sugar().meetings.recordView.verifyInvitee(1, qaUserName);
		sugar().meetings.recordView.save();

		// Confirm that only qauser remains as meeting participant
		sugar().meetings.recordView.verifyInvitee(1, qaUserName);
		sugar().meetings.recordView.getControl("invitees").assertContains(testData.get(testName).get(0).get("admin"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}