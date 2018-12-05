package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_30063 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that application is adding regular user as an invitee with which the calendar is shared
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_30063_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Click Shared tab
		// TODO: VOOD-863 - need support for Calendar module
		new VoodooControl("input", "id", "shared-tab").click();
		VoodooUtils.waitForReady();

		// Click "User List" button in "Shared Calendar" panel.
		new VoodooControl("button", "id", "userListButtonId").click();

		// Select a team of qauser
		new VoodooControl("a", "css", "#shared_team_id").click();
		new VoodooControl("option", "css", "#shared_team_id option:nth-child(7)").click();

		// Select 'qauser' from users box
		new VoodooControl("option", "css", "#shared_ids option").click();

		// Click "Select" button in "Shared Calendar" panel.
		new VoodooControl("button", "css", ".ft [title='Select']").click();

		// Access 08:00 time slot and click Schedule Call from warning
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.getWarning().clickLink(1);

		// Assert 'qauser' is added to invitee list on calls create drawer
		String qaUser = sugar().users.getQAUser().get("userName");
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.getControl("invitees").assertContains(qaUser, true);
		sugar.calls.createDrawer.save();

		// Navigate to Calls module
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);

		// Assert 'qauser' is added to invitee list on calls record view
		sugar.calls.recordView.getControl("invitees").assertContains(qaUser, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}