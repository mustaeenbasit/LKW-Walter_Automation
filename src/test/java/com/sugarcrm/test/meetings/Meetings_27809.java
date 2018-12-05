package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27809 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Reminder fields are consistent in list view and record view in Meetings
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27809_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio > Meetings > Layouts 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938 - Need library support for studio subpanel
		new VoodooControl("a", "id", "studiolink_Meetings").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();

		// Verifying Reminder fields in List View
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		// Verify Popup Reminder Time
		new VoodooControl("li", "css", ".draggable[data-name='reminder_time']").assertVisible(true);
		// Verify Email Reminder Time
		new VoodooControl("li", "css", ".draggable[data-name='email_reminder_time']").assertVisible(true);

		new VoodooControl("a", "css", "#mbtabs  div a:nth-child(5)").click();

		// Verifying Reminder fields in Record View
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();
		// Verify reminders
		new VoodooControl("div", "css", ".le_field[data-name='reminders']").assertVisible(true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}