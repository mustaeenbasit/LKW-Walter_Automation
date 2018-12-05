package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27816 extends SugarTest {
	VoodooControl meetingsButtonCtrl;

	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify that reminder time should not change (keep as default value) after inline edit it
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27816_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		// Go to Admin > Studio > Meetings > Layouts > Listview. 
		meetingsButtonCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
		meetingsButtonCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Add Popup Reminder Time to default
		VoodooControl dropHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='reminder_time']").dragNDrop(dropHere);

		// Add Email Reminder Time to default
		new VoodooControl("li", "css", ".draggable[data-name='email_reminder_time']").dragNDrop(dropHere);

		// Save and Deploy
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to meetings list view
		sugar().meetings.navToListView();

		// Click on close DashBoard
		sugar().calls.listView.toggleSidebar();
		FieldSet customData = testData.get(testName).get(0);

		// Edit Pop up reminder time
		sugar().meetings.listView.editRecord(1);
		new VoodooSelect("div", "css", ".fld_reminder_time.edit").set(customData.get("popUpReminderTime"));
		sugar().meetings.listView.saveRecord(1);

		// Go for create a meeting
		sugar().meetings.listView.create();

		// Verify Default popup reminder time should have the default value
		sugar().meetings.createDrawer.getEditField("remindersPopup").assertContains(customData.get("defaultPopUpTimeReminder"), true); // Default PopUp value should be "30 minutes prior"

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}