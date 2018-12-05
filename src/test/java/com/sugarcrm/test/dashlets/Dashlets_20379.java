package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20379 extends SugarTest {
	UserRecord user1;

	public void setup() throws Exception {
		sugar().login();

		// Create a new user-a
		user1 = (UserRecord)sugar().users.create();

		// Logout from Admin
		sugar().logout();

		// Login as user-a for "Planned Activites" dashlet created by user-a
		sugar().login(user1);

		// Go to Home and create a Dashboard
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().dashboard.clickCreate();
		sugar().dashboard.getControl("title").set(testName);
		sugar().dashboard.addRow();
		sugar().dashboard.addDashlet(1, 1);

		// TODO: VOOD-960 - Dashlet Selection
		// Select Planned Activities
		new VoodooControl("a", "css", ".list-view .dataTable tbody tr:nth-child(12) .list.fld_title a").click();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();
		sugar().home.dashboard.save();

		// Login from user-a
		sugar().logout();
	}

	/**
	 * Send invites for meeting: verify that invitees can see the invites in their own home's dashlet.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20379_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as Admin
		sugar().login();

		// Go to Meetings and schedule a meeting
		sugar().navbar.selectMenuItem(sugar().meetings, "create" + sugar().meetings.moduleNameSingular);

		// Invite user-a
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(user1.get("userName"));
		sugar().meetings.createDrawer.save();

		// Logout from Admin and login as user-a
		sugar().logout();
		sugar().login(user1);

		// Verify Meeting invitation is displayed in "My Meetings" "Planned Activities" dashlet
		// TODO: VOOD-1305 Dashlet: Planned Activities - Need lib support for RHS Planned Activities Dashlets
		new VoodooControl("div", "css", "div.tab-pane.active p a:nth-of-type(2)").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}