package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_30226 extends SugarTest {
	UserRecord userChris;

	public void setup() throws Exception {
		// Login with Admin
		sugar().login();

		// Create a chris User
		userChris= (UserRecord) sugar().users.api.create();
		sugar().logout();

		// Login as qauser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that allow Option for disabling current user as invitee for meetings.
	 * @throws Exception
	 */
	@Test
	public void Meetings_30226_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.selectMenuItem(sugar().meetings, "createMeeting");
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));

		// Assigning meeting to chris
		sugar().meetings.createDrawer.getEditField("assignedTo").set(userChris.get("fullName"));

		// Verifying chris and qauser are in guest list
		VoodooControl meetingsGuestListCtrl = sugar().meetings.createDrawer.getControl("invitees");
		meetingsGuestListCtrl.assertContains(userChris.get("fullName"), true);
		meetingsGuestListCtrl.assertContains(sugar().users.qaUser.get("userName"), true);

		// TODO: VOOD-1880
		// Verifying after clicking on qauser user remove button, qauser removes from guest list
		new VoodooControl("button", "css", ".btn[data-action='removeRow']").click();
		VoodooUtils.waitForReady();
		meetingsGuestListCtrl.assertContains(sugar().users.qaUser.get("userName"), false);

		// Verifying after clicking on chris user remove button, chris does not remove from guest list as remove button is disabled 
		new VoodooControl("button", "css", ".btn.disabled[data-action='removeRow']").click();
		meetingsGuestListCtrl.assertContains(userChris.get("fullName"), true);
		sugar().meetings.createDrawer.save();

		// Verifying meeting is saved
		sugar().meetings.listView.getDetailField(1, "name").assertEquals(sugar().meetings.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}