package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_30226 extends SugarTest {
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
	 * Verify that allow Option for disabling current user as invitee for Calls.
	 * @throws Exception
	 */
	@Test
	public void Calls_30226_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.selectMenuItem(sugar().calls, "createCall");
		sugar().calls.createDrawer.getEditField("name").set(sugar().calls.getDefaultData().get("name"));

		// Assigning call to chris
		sugar().calls.createDrawer.getEditField("assignedTo").set(userChris.get("fullName"));

		// Verifying chris and qauser are in guest list
		VoodooControl callsGuestListCtrl = sugar().calls.createDrawer.getControl("invitees");
		callsGuestListCtrl.assertContains(userChris.get("fullName"), true);
		callsGuestListCtrl.assertContains(sugar().users.qaUser.get("userName"), true);

		// TODO: VOOD-1880
		// Verifying after clicking on qauser user remove button, qauser removes from guest list
		new VoodooControl("button", "css", ".btn[data-action='removeRow']").click();
		VoodooUtils.waitForReady();
		callsGuestListCtrl.assertContains(sugar().users.qaUser.get("userName"), false);

		// Verifying after clicking on chris user remove button, chris does not remove from guest list as remove button is disabled 
		new VoodooControl("button", "css", ".btn.disabled[data-action='removeRow']").click();
		callsGuestListCtrl.assertContains(userChris.get("fullName"), true);
		sugar().calls.createDrawer.save();

		// Verifying call is saved
		sugar().calls.listView.getDetailField(1, "name").assertEquals(sugar().calls.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}